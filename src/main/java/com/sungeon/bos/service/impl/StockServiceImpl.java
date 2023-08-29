package com.sungeon.bos.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.model.ObjectOperateType;
import com.burgeon.framework.restapi.request.QueryFilterCombine;
import com.burgeon.framework.restapi.request.QueryFilterParam;
import com.burgeon.framework.restapi.response.ExecuteWebActionResponse;
import com.burgeon.framework.restapi.response.ObjectSubmitResponse;
import com.burgeon.framework.restapi.response.ProcessOrderResponse;
import com.sungeon.bos.core.constants.Constants;
import com.sungeon.bos.core.utils.CollectionUtils;
import com.sungeon.bos.dao.IStockDao;
import com.sungeon.bos.entity.base.InventoryEntity;
import com.sungeon.bos.entity.base.ItemEntity;
import com.sungeon.bos.entity.pmila.*;
import com.sungeon.bos.service.IStockService;
import com.sungeon.bos.util.BurgeonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Service("stockService")
public class StockServiceImpl implements IStockService {

    @Autowired
    private IStockDao stockDao;
    @Autowired
    private BurgeonRestClient burgeonRestClient;

    @Override
    public ObjectSubmitResponse dealBsijaOut(String bsijaDocNo, String billType, List<ItemEntity> items) {
        List<QueryFilterParam> filterParamList = new ArrayList<>();
        filterParamList.add(new QueryFilterParam("DOCNO", bsijaDocNo, QueryFilterCombine.AND));
        filterParamList.add(new QueryFilterParam("BILLTYPE", billType, QueryFilterCombine.AND));

        PmilaOut out = burgeonRestClient.queryObject(PmilaOut.class, filterParamList);
        log.info("出库单数据：" + JSONObject.toJSONString(out));

        if (null != out && "未提交".equals(out.getStatus())) {
            ObjectSubmitResponse submitResponse;
            if (CollectionUtils.isEmpty(items)) {
                // 自动匹配出库数量
                ExecuteWebActionResponse webActionResponse = burgeonRestClient.executeWebAction("M_OUT_QTYCOP",
                        out.getId());
                log.info("自动匹配出库数量结果：{}", JSONObject.toJSONString(webActionResponse));
                if (!webActionResponse.isRequestSuccess()) {
                    submitResponse = new ObjectSubmitResponse();
                    submitResponse.setCode(webActionResponse.getCode());
                    submitResponse.setMessage(webActionResponse.getMessage());
                }
            } else {
                // 根据items更新出库明细
                for (PmilaOutItem bsijaOutItem : out.getItems()) {
                    Iterator<ItemEntity> iterator = items.iterator();
                    while (iterator.hasNext()) {
                        ItemEntity item = iterator.next();
                        if (item.getSku().equals(bsijaOutItem.getSku())) {
                            bsijaOutItem.setQtyOut(item.getQtyOut());
                            iterator.remove();
                            break;
                        }
                    }
                }
                burgeonRestClient.processOrder(out, ObjectOperateType.MODIFY);
            }
            // 出库提交
            submitResponse = burgeonRestClient.objectSubmit(out);
            log.info("出库单提交结果：{}", JSONObject.toJSONString(submitResponse));
            return submitResponse;
        } else if (null != out && "提交".equals(out.getStatus())) {
            ObjectSubmitResponse submitResponse = new ObjectSubmitResponse();
            submitResponse.setCode(0);
            submitResponse.setMessage("已出库提交");
            return submitResponse;
        }
        return null;
    }

    @Override
    public ObjectSubmitResponse dealBsijaIn(String bsijaDocNo, String billType) {
        List<QueryFilterParam> filterParamList = new ArrayList<>();
        filterParamList.add(new QueryFilterParam("DOCNO", bsijaDocNo, QueryFilterCombine.AND));
        filterParamList.add(new QueryFilterParam("BILLTYPE", billType, QueryFilterCombine.AND));

        PmilaIn in = burgeonRestClient.queryObject(PmilaIn.class, filterParamList);

        if (null != in) {
            // 自动匹配入库数量
            ExecuteWebActionResponse webActionResponse = burgeonRestClient.executeWebAction("M_IN_QTYCOP",
                    in.getId());
            log.info("自动匹配入库数量结果：{}", JSONObject.toJSONString(webActionResponse));
            if (webActionResponse.isRequestSuccess()) {
                // 入库提交
                ObjectSubmitResponse submitResponse = burgeonRestClient.objectSubmit(in);
                log.info("入库单提交结果：{}", JSONObject.toJSONString(submitResponse));
                return submitResponse;
            }
        }
        return null;
    }

    @Override
    public List<InventoryEntity> syncBsijaInventory(String docNo, int page, int pageSize) {
        int beg = (page - 1) * pageSize + 1;
        int end = page * pageSize;
        List<InventoryEntity> inventories = stockDao.queryInventoryList(docNo, beg, end);
        for (InventoryEntity inventory : inventories) {
            PmilaInOut bsijaOtherInOut = new PmilaInOut();
            BeanUtils.copyProperties(inventory, bsijaOtherInOut);
            bsijaOtherInOut.setDescription("盘点差异，" + inventory.getDescription());
            bsijaOtherInOut.setId(null);
            bsijaOtherInOut.setDocNo(null);
            List<PmilaInOutItem> bsijaOtherInOutItems = new ArrayList<>();
            for (ItemEntity item : inventory.getItems()) {
                PmilaInOutItem bsijaOtherInOutItem = new PmilaInOutItem();
                bsijaOtherInOutItem.setProductAdd(item.getSku());
                bsijaOtherInOutItem.setQty(item.getQty());
                bsijaOtherInOutItems.add(bsijaOtherInOutItem);
            }
            bsijaOtherInOut.setItems(bsijaOtherInOutItems);

            log.info("同步毕厶迦盘点单参数：{}", JSONObject.toJSONString(bsijaOtherInOut));
            ProcessOrderResponse response = burgeonRestClient.processOrder(bsijaOtherInOut, ObjectOperateType.CREATE);
            log.info("同步毕厶迦盘点单响应：{}", JSONObject.toJSONString(response));
            String bsijaNo = "";
            if (response.isRequestSuccess()) {
                List<QueryFilterParam> filterParamList = new ArrayList<>();
                filterParamList.add(new QueryFilterParam("ID", String.valueOf(response.getObjectid()), QueryFilterCombine.AND));

                bsijaOtherInOut = burgeonRestClient.queryObject(PmilaInOut.class, filterParamList);
                bsijaNo = null == bsijaOtherInOut ? "" : bsijaOtherInOut.getDocNo();
            }
            stockDao.updateInventorySyncStatus(inventory.getId(), response.isRequestSuccess()
                    ? Constants.BURGEON_YES : Constants.BURGEON_FAIL, bsijaNo, response.getMessage());
        }
        return inventories;
    }

}
