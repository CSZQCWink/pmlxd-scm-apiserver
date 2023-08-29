package com.sungeon.bos.service.impl;

import com.burgeon.framework.restapi.request.QueryFilterCombine;
import com.burgeon.framework.restapi.request.QueryFilterParam;
import com.burgeon.framework.restapi.request.QueryOrderByParam;
import com.sungeon.bos.core.exception.AlreadyExistsException;
import com.sungeon.bos.core.utils.CollectionUtils;
import com.sungeon.bos.core.utils.DateTimeUtils;
import com.sungeon.bos.core.utils.StringUtils;
import com.sungeon.bos.dao.IPurchaseDao;
import com.sungeon.bos.entity.base.ItemEntity;
import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import com.sungeon.bos.entity.pmila.PmilaSale;
import com.sungeon.bos.entity.pmila.PmilaSaleReturn;
import com.sungeon.bos.service.IPurchaseService;
import com.sungeon.bos.service.ISaleService;
import com.sungeon.bos.util.BurgeonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Service("saleService")
public class SaleServiceImpl implements ISaleService {

    @Autowired
    private IPurchaseService purchaseService;
    @Autowired
    private IPurchaseDao purchaseDao;
    @Autowired
    private BurgeonRestClient burgeonRestClient;

    @Override
    public List<PurchaseEntity> syncBsijaSale(String startTime, String docNo, int page, int pageSize) {
        int start = (page - 1) * pageSize;
        List<QueryFilterParam> filterParamList = new ArrayList<>();
        filterParamList.add(new QueryFilterParam("IN_STATUS", "2", QueryFilterCombine.AND));
        if (StringUtils.isNotEmpty(docNo)) {
            filterParamList.add(new QueryFilterParam("DOCNO", docNo, QueryFilterCombine.AND));
        }
        if (StringUtils.isNotEmpty(startTime)) {
            Date date = DateTimeUtils.offsetMinute(DateTimeUtils.convert(startTime), -1);
            filterParamList.add(new QueryFilterParam("", "M_SALE.INTIME > to_date('"
                    + DateTimeUtils.print(date) + "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
        }
        List<QueryOrderByParam> orderByParamList = new ArrayList<>();
        orderByParamList.add(new QueryOrderByParam("ID", true));

        List<PmilaSale> purchases = burgeonRestClient.query(PmilaSale.class, start, pageSize, filterParamList,
                orderByParamList);
        List<PurchaseEntity> purchaseList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(purchases)) {
            log.info("获取毕厶迦销售单响应：{}", purchases);
            for (PmilaSale bsijaSale : purchases) {
                Long purchaseId = purchaseDao.queryPurchaseIdBySourceNo(bsijaSale.getDocNo());
                if (null != purchaseId) {
                    continue;
                }
                PurchaseEntity purchase = new PurchaseEntity();
                purchase.setSourceNo(bsijaSale.getDocNo());
                purchase.setBillDate(bsijaSale.getBillDate());
                purchase.setSupplierCode(bsijaSale.getOrigCode());
                purchase.setStoreCode(bsijaSale.getDestCode());
                purchase.setInDate(bsijaSale.getInDate());
                purchase.setIsAutoIn(false);
                purchase.setDescription(bsijaSale.getDescription());
                List<ItemEntity> items = new ArrayList<>();
                bsijaSale.getItems().forEach(i -> {
                    ItemEntity item = new ItemEntity();
                    item.setSku(i.getSku());
                    item.setQty(i.getQtyOut());
                    item.setQtyIn(i.getQtyIn());
                    item.setPriceActual(i.getPriceActual());
                    items.add(item);
                });
                purchase.setItems(items);
                try {
                    purchaseService.addPurchase(purchase);
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
                purchaseList.add(purchase);
            }
        }
        return purchaseList;
    }

    @Override
    public List<PurchaseReturnEntity> syncBsijaSaleReturn(String startTime, String docNo, int page, int pageSize) {
        int start = (page - 1) * pageSize;
        List<QueryFilterParam> filterParamList = new ArrayList<>();
        filterParamList.add(new QueryFilterParam("IN_STATUS", "2", QueryFilterCombine.AND));
        if (StringUtils.isNotEmpty(docNo)) {
            filterParamList.add(new QueryFilterParam("DOCNO", docNo, QueryFilterCombine.AND));
        }
        if (StringUtils.isNotEmpty(startTime)) {
            Date date = DateTimeUtils.offsetMinute(DateTimeUtils.convert(startTime), -1);
            // filterParamList.add(new QueryFilterParam("", "(M_RET_SALE.STATUSTIME > to_date('"
            //         + DateTimeUtils.print(date) + "', 'yyyy-mm-dd hh24:mi:ss') OR M_RET_SALE.OUTTIME > to_date('"
            //         + DateTimeUtils.print(date) + "', 'yyyy-mm-dd hh24:mi:ss'))", QueryFilterCombine.AND));
            filterParamList.add(new QueryFilterParam("", "M_RET_SALE.INTIME > to_date('"
                    + DateTimeUtils.print(date) + "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
        }
        List<QueryOrderByParam> orderByParamList = new ArrayList<>();
        orderByParamList.add(new QueryOrderByParam("ID", true));

        List<PmilaSaleReturn> purchases = burgeonRestClient.query(PmilaSaleReturn.class, start, pageSize,
                filterParamList, orderByParamList);
        List<PurchaseReturnEntity> purchaseReturnList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(purchases)) {
            log.info("获取毕厶迦销售退货单响应：{}", purchases);
            for (PmilaSaleReturn bsijaSaleReturn : purchases) {
                PurchaseReturnEntity retPur = purchaseDao.queryPurchaseReturnBySourceNo(bsijaSaleReturn.getDocNo());
                if (null != retPur) {
                    continue;
                }
                PurchaseReturnEntity purchaseReturn = new PurchaseReturnEntity();
                purchaseReturn.setSourceNo(bsijaSaleReturn.getDocNo());
                purchaseReturn.setBillDate(bsijaSaleReturn.getInDate());
                purchaseReturn.setSupplierCode(bsijaSaleReturn.getDestCode());
                purchaseReturn.setStoreCode(bsijaSaleReturn.getOrigCode());
                purchaseReturn.setOutDate(bsijaSaleReturn.getInDate());
                purchaseReturn.setIsAutoOut("提交".equals(bsijaSaleReturn.getInStatus()));
                if (StringUtils.isNotEmpty(bsijaSaleReturn.getSourceNo())) {
                    purchaseReturn.setPurchaseReturnOrderNo(bsijaSaleReturn.getSourceNo().startsWith("PRO")
                            ? bsijaSaleReturn.getSourceNo() : null);
                }
                purchaseReturn.setDescription(bsijaSaleReturn.getDescription());
                List<ItemEntity> items = new ArrayList<>();
                bsijaSaleReturn.getItems().forEach(i -> {
                    ItemEntity item = new ItemEntity();
                    item.setSku(i.getSku());
                    item.setQty(i.getQty());
                    item.setQtyOut(i.getQtyOut());
                    item.setPriceActual(i.getPriceActual());
                    items.add(item);
                });
                purchaseReturn.setItems(items);
                try {
                    purchaseService.addPurchaseReturn(purchaseReturn);
                } catch (AlreadyExistsException e) {
                    log.info(e.getMessage());
                }
                purchaseReturnList.add(purchaseReturn);
            }
        }
        return purchaseReturnList;
    }

}
