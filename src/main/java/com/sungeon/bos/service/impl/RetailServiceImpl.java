package com.sungeon.bos.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.model.ObjectOperateType;
import com.burgeon.framework.restapi.request.QueryFilterCombine;
import com.burgeon.framework.restapi.request.QueryFilterParam;
import com.burgeon.framework.restapi.response.ProcessOrderResponse;
import com.sungeon.bos.core.constants.Constants;
import com.sungeon.bos.dao.IRetailDao;
import com.sungeon.bos.entity.base.ItemEntity;
import com.sungeon.bos.entity.base.RetailEntity;
import com.sungeon.bos.entity.pmila.PmilaRetail;
import com.sungeon.bos.entity.pmila.PmilaRetailItem;
import com.sungeon.bos.entity.pmila.PmilaRetailPayItem;
import com.sungeon.bos.service.IRetailService;
import com.sungeon.bos.util.BurgeonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Service("retailService")
public class RetailServiceImpl implements IRetailService {

	@Autowired
	private IRetailDao retailDao;
	@Autowired
	private BurgeonRestClient burgeonRestClient;

	@Override
	public List<RetailEntity> syncBsijaRetail(String docNo, int page, int pageSize) {
		int beg = (page - 1) * pageSize + 1;
		int end = page * pageSize;
		List<RetailEntity> retails = retailDao.queryRetailList(docNo, beg, end);
		for (RetailEntity retail : retails) {
			List<ItemEntity> items = retailDao.queryRetailItemList(retail.getId());
			PmilaRetail bsijaRetail = new PmilaRetail();
			bsijaRetail.setSourceNo(retail.getSourceNo());
			bsijaRetail.setBillDate(retail.getBillDate());
			bsijaRetail.setStoreName(retail.getStoreName());
			bsijaRetail.setDescription(retail.getDescription());
			List<PmilaRetailItem> bsijaRetailItems = new ArrayList<>();
			for (ItemEntity item : items) {
				PmilaRetailItem bsijaRetailItem = new PmilaRetailItem();
				bsijaRetailItem.setProductAdd(item.getSku());
				bsijaRetailItem.setQty(item.getQty());
				bsijaRetailItem.setPriceActual(item.getPriceActual());
				bsijaRetailItem.setAmtActual(item.getAmtActual());
				bsijaRetailItems.add(bsijaRetailItem);
			}
			bsijaRetail.setItems(bsijaRetailItems);
			Double totAmount = items.stream().mapToDouble(ItemEntity::getAmtActual).sum();
			List<PmilaRetailPayItem> bsijaRetailPayItems = new ArrayList<>();
			PmilaRetailPayItem bsijaRetailPayItem = new PmilaRetailPayItem();
			bsijaRetailPayItem.setPayWayName("现金");
			bsijaRetailPayItem.setPayAmount(totAmount);
			bsijaRetailPayItem.setBasePayAmount(totAmount);
			bsijaRetailPayItems.add(bsijaRetailPayItem);
			bsijaRetail.setPayItems(bsijaRetailPayItems);

			log.info("同步毕厶迦零售单参数：{}", JSONObject.toJSONString(bsijaRetail));
			ProcessOrderResponse response = burgeonRestClient.processOrder(bsijaRetail, ObjectOperateType.CREATE);
			log.info("同步毕厶迦零售单响应：{}", JSONObject.toJSONString(response));
			String bsijaNo = "";
			if (response.isRequestSuccess()) {
				List<QueryFilterParam> filterParamList = new ArrayList<>();
				filterParamList.add(new QueryFilterParam("ID", String.valueOf(response.getObjectid()), QueryFilterCombine.AND));

				bsijaRetail = burgeonRestClient.queryObject(PmilaRetail.class, filterParamList);
				bsijaNo = null == bsijaRetail ? "" : bsijaRetail.getDocNo();
			}
			retailDao.updateRetailSyncStatus(retail.getId(), response.isRequestSuccess() ? Constants.BURGEON_YES
					: Constants.BURGEON_FAIL, bsijaNo, response.getMessage());
		}
		return retails;
	}

}
