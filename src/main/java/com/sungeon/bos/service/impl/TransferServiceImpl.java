package com.sungeon.bos.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.model.ObjectOperateType;
import com.burgeon.framework.restapi.request.QueryFilterCombine;
import com.burgeon.framework.restapi.request.QueryFilterParam;
import com.burgeon.framework.restapi.response.ObjectSubmitResponse;
import com.burgeon.framework.restapi.response.ProcessOrderResponse;
import com.sungeon.bos.core.constants.Constants;
import com.sungeon.bos.dao.ITransferDao;
import com.sungeon.bos.entity.base.ItemEntity;
import com.sungeon.bos.entity.base.TransferEntity;
import com.sungeon.bos.entity.pmila.PmilaTransfer;
import com.sungeon.bos.entity.pmila.PmilaTransferItem;
import com.sungeon.bos.service.IStockService;
import com.sungeon.bos.service.ITransferService;
import com.sungeon.bos.util.BurgeonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Service("transferService")
public class TransferServiceImpl implements ITransferService {

	@Autowired
	private ITransferDao transferDao;
	@Autowired
	private IStockService stockService;
	@Autowired
	private BurgeonRestClient burgeonRestClient;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<TransferEntity> syncBsijaTransfer(String docNo, int page, int pageSize) {
		int beg = (page - 1) * pageSize + 1;
		int end = page * pageSize;
		List<TransferEntity> transfers = transferDao.queryTransferOutList(docNo, beg, end);
		for (TransferEntity transfer : transfers) {
			PmilaTransfer bsijaTransfer = new PmilaTransfer();
			BeanUtils.copyProperties(transfer, bsijaTransfer);
			if ("仓库".equals(transfer.getOrigKind()) && !"仓库".equals(transfer.getDestKind())) {
				bsijaTransfer.setTransferType("物料发货");
			} else if (!"仓库".equals(transfer.getOrigKind()) && "仓库".equals(transfer.getDestKind())) {
				bsijaTransfer.setTransferType("退货回总部");
			} else if (!"仓库".equals(transfer.getOrigKind()) && !"仓库".equals(transfer.getDestKind())) {
				bsijaTransfer.setTransferType("门店与门店调拨");
			}
			bsijaTransfer.setOrigNameAdd(transfer.getOrigName());
			bsijaTransfer.setDestNameAdd(transfer.getDestName());
			bsijaTransfer.setId(null);
			bsijaTransfer.setDocNo(null);
			List<PmilaTransferItem> bsijaTransferItems = new ArrayList<>();
			for (ItemEntity item : transfer.getItems()) {
				PmilaTransferItem bsijaTransferItem = new PmilaTransferItem();
				bsijaTransferItem.setProductAdd(item.getSku());
				bsijaTransferItem.setQty(item.getQtyIn());
				bsijaTransferItems.add(bsijaTransferItem);
			}
			bsijaTransfer.setItems(bsijaTransferItems);

			log.info("同步毕厶迦调拨单参数：{}", JSONObject.toJSONString(bsijaTransfer));
			ProcessOrderResponse response = burgeonRestClient.processOrder(bsijaTransfer, ObjectOperateType.CREATE);
			log.info("同步毕厶迦调拨单响应：{}", JSONObject.toJSONString(response));
			String bsijaNo = "";
			if (response.isRequestSuccess()) {
				List<QueryFilterParam> filterParamList = new ArrayList<>();
				filterParamList.add(new QueryFilterParam("ID", String.valueOf(response.getObjectid()), QueryFilterCombine.AND));

				bsijaTransfer = burgeonRestClient.queryObject(PmilaTransfer.class, filterParamList);
				bsijaNo = (null == bsijaTransfer) ? "" : bsijaTransfer.getDocNo();

				if (null != bsijaTransfer) {
					ObjectSubmitResponse outResponse = new ObjectSubmitResponse();
					outResponse.setCode(0);
					if ("未提交".equals(bsijaTransfer.getOutStatus())) {
						// 出库
						outResponse = stockService.dealBsijaOut(bsijaNo, "M_TRANSFEROUT", null);
					}
					if ("未提交".equals(bsijaTransfer.getInStatus()) && outResponse.isRequestSuccess()) {
						// 入库
						stockService.dealBsijaIn(bsijaNo, "M_TRANSFERIN");
					}
				}
			}
			transferDao.updateTransferSyncStatus(transfer.getId(), response.isRequestSuccess()
					? Constants.BURGEON_YES : Constants.BURGEON_FAIL, bsijaNo, response.getMessage());
		}
		return transfers;
	}

}
