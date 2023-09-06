package com.sungeon.bos.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.burgeon.framework.restapi.model.ObjectOperateType;
import com.burgeon.framework.restapi.request.QueryFilterCombine;
import com.burgeon.framework.restapi.request.QueryFilterParam;
import com.burgeon.framework.restapi.request.QueryOrderByParam;
import com.burgeon.framework.restapi.response.ObjectSubmitResponse;
import com.burgeon.framework.restapi.response.ProcessOrderResponse;
import com.sungeon.bos.core.constants.Constants;
import com.sungeon.bos.core.exception.AlreadyExistsException;
import com.sungeon.bos.core.exception.ParamNotMatchException;
import com.sungeon.bos.core.exception.ParamNullException;
import com.sungeon.bos.core.handler.SynchronizedHandler;
import com.sungeon.bos.core.utils.CollectionUtils;
import com.sungeon.bos.core.utils.DateTimeUtils;
import com.sungeon.bos.core.utils.StringUtils;
import com.sungeon.bos.dao.*;
import com.sungeon.bos.entity.base.*;
import com.sungeon.bos.entity.pmila.*;
import com.sungeon.bos.service.IPurchaseService;
import com.sungeon.bos.service.IStockService;
import com.sungeon.bos.util.BurgeonRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Service("purchaseService")
public class PurchaseServiceImpl implements IPurchaseService {

	@Autowired
	private IPurchaseDao purchaseDao;
	@Autowired
	private IStoreDao storeDao;
	@Autowired
	private ISupplierDao supplierDao;
	@Autowired
	private IBaseDao baseDao;
	@Autowired
	private IProductDao productDao;
	@Autowired
	private IStockService stockService;
	@Autowired
	private SynchronizedHandler synchronizedHandler;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Integer addPurchase(PurchaseEntity purchase) {
//		if (StringUtils.isEmpty(purchase.getSourceNo())) {
//			throw new ParamNullException("sourceNo", "第三方平台单号不能为空");
//		}
		synchronizedHandler.execute("Burgeon.Bos.Purchase.Add", purchase.getDocNo(), () -> {
			if (CollectionUtils.isEmpty(purchase.getItems())) {
				throw new ParamNullException("items", "明细不能为空");
			}
//			if (!StringUtils.isEmpty(purchase.getDocNo())) {
//				PurchaseEntity po = purchaseDao.queryPOSupplierStore(purchase.getDocNo());
//				if (null == po) {
//					throw new ParamNotMatchException("poDocno", "采购订单[" + purchase.getDocNo() + "]不存在");
//				}
//				purchase.setPoId(po.getId());
//				purchase.setSupplierId(po.getSupplierId());
//				purchase.setStoreId(po.getStoreId());
//				purchase.setDocType("POO");
//			}
			if(StringUtils.isEmpty(purchase.getDocNo())){
				if (StringUtils.isEmpty(purchase.getSupplierCode())) {
					throw new ParamNullException("supplierCode", "供应商不能为空");
				}
				if (StringUtils.isEmpty(purchase.getStoreCode())) {
					throw new ParamNullException("storeCode", "采购店仓不能为空");
				}
				Long purchaseId = purchaseDao.queryPurchaseIdByDocNo(purchase.getDocNo());
				if (null != purchaseId) {
					throw new AlreadyExistsException("单号[" + purchase.getDocNo() + "]已存在，不允许重复新增");
				}
				Long supplierId = supplierDao.querySupplierIdByCode(purchase.getSupplierCode());
				if (null == supplierId) {
					throw new ParamNotMatchException("supplierCode", "供应商[" + purchase.getSupplierCode() + "]不存在");
				}
				StoreEntity store = storeDao.queryStoreByCode(purchase.getStoreCode());
				if (null == store) {
					throw new ParamNotMatchException("storeCode", "采购店仓[" + purchase.getStoreCode() + "]不存在");
				}
				purchase.setSupplierId(supplierId);
				purchase.setStoreId(store.getId());
				if (StringUtils.isEmpty(purchase.getDocType())) {
					purchase.setDocType("PUO");
				}
			}
			if (null == purchase.getBillDate()) {
				purchase.setBillDate(DateTimeUtils.todayNumber());
			}
			purchase.setDocNo(baseDao.getNewDocno("PU"));
			purchaseDao.insertPurchase(purchase);

			for (ItemEntity item : purchase.getItems()) {
				//dealItem(purchase.getId(), item);
				purchaseDao.insertPurchaseItem(item);
				//purchaseDao.callPurchaseItemAm(item.getId());
			}
			//purchaseDao.callPurchaseAm(purchase.getId());
			//purchaseDao.callPurchaseSubmit(purchase.getId());
//			if (purchase.getIsAutoIn()) {
//				// purchaseDao.callPurchaseInQtyCop(purchase.getId());
//				purchaseDao.callPurchaseInSubmit(purchase.getId());
//			}
		});
		return 1;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Integer addPurchaseReturn(PurchaseReturnEntity purchaseReturn) {
		if (StringUtils.isEmpty(purchaseReturn.getSourceNo())) {
			throw new ParamNullException("sourceNo", "第三方平台单号不能为空");
		}
		synchronizedHandler.execute("Burgeon.Bos.RetPur.Add", purchaseReturn.getSourceNo(), () -> {
			if (CollectionUtils.isEmpty(purchaseReturn.getItems())) {
				throw new ParamNullException("items", "items参数不能为空");
			}
			if (StringUtils.isEmpty(purchaseReturn.getSupplierCode())) {
				throw new ParamNullException("supplierCode", "supplierCode参数不能为空");
			}
			if (StringUtils.isEmpty(purchaseReturn.getStoreCode())) {
				throw new ParamNullException("storeCode", "storeCode参数不能为空");
			}
			PurchaseReturnEntity retPur = purchaseDao.queryPurchaseReturnBySourceNo(purchaseReturn.getSourceNo());
			if (null == retPur) {
				Long supplierId = supplierDao.querySupplierIdByCode(purchaseReturn.getSupplierCode());
				if (null == supplierId) {
					throw new ParamNotMatchException("supplierCode", "供应商[" + purchaseReturn.getSupplierCode() + "]不存在");
				}
				StoreEntity store = storeDao.queryStoreByCode(purchaseReturn.getStoreCode());
				if (null == store) {
					throw new ParamNotMatchException("storeCode", "退货店仓[" + purchaseReturn.getStoreCode() + "]不存在");
				}
				if (StringUtils.isNotEmpty(purchaseReturn.getPurchaseReturnOrderNo())) {
					purchaseReturn.setPurchaseReturnOrderId(purchaseDao.queryPurchaseReturnOrderIdByDocno(purchaseReturn.getPurchaseReturnOrderNo()));
				}
				purchaseReturn.setSupplierId(supplierId);
				purchaseReturn.setStoreId(store.getId());
				if (null == purchaseReturn.getBillDate()) {
					purchaseReturn.setBillDate(DateTimeUtils.todayNumber());
				}
				purchaseReturn.setDocNo(baseDao.getNewDocno("PR"));
				purchaseDao.insertRetPur(purchaseReturn);

				for (ItemEntity item : purchaseReturn.getItems()) {
					dealItem(purchaseReturn.getId(), item);
					purchaseDao.insertRetPurItem(item);
					purchaseDao.callRetPurItemAcm(item.getId());
				}
				purchaseDao.callRetPurAm(purchaseReturn.getId());
				purchaseDao.callRetPurSubmit(purchaseReturn.getId());
				if (purchaseReturn.getIsAutoOut()) {
					// purchaseDao.callRetPurOutQtyCop(purchaseReturn.getId());
					purchaseDao.callRetPurOutSubmit(purchaseReturn.getId());
				}
			} else {
				for (ItemEntity item : purchaseReturn.getItems()) {
					dealItem(retPur.getId(), item);
					purchaseDao.updateRetPurOutItem(item);
				}
				purchaseDao.callRetPurAm(retPur.getId());
				if (purchaseReturn.getIsAutoOut() && retPur.getOutStatus().equals("1")) {
					purchaseDao.callRetPurOutQtyCop(purchaseReturn.getId());
					purchaseDao.callRetPurOutSubmit(retPur.getId());
				}
			}
		});
		return 1;
	}

	@Autowired
	private BurgeonRestClient burgeonRestClient;

	@Override
	public List<PurchaseReturnEntity> syncPmilaPurchaseReturn(String docNo, int page, int pageSize) {
		int beg = (page - 1) * pageSize + 1;
		int end = page * pageSize;
		List<PurchaseReturnEntity> purchases = purchaseDao.queryPurchaseReturnList(docNo, beg, end);
		for (PurchaseReturnEntity purchase : purchases) {
			PmilaSaleReturn bsijaSaleReturn = new PmilaSaleReturn();
			bsijaSaleReturn.setBillDate(purchase.getBillDate());
			bsijaSaleReturn.setRetSaleType("NOR");
			bsijaSaleReturn.setSourceNo(purchase.getDocNo());
			bsijaSaleReturn.setOrigAddName(purchase.getStoreName());
			bsijaSaleReturn.setDestAddName(purchase.getSupplierName());
			bsijaSaleReturn.setDescription(purchase.getDescription());
			List<PmilaSaleReturnItem> bsijaSaleReturnItems = new ArrayList<>();
			for (ItemEntity item : purchase.getItems()) {
				PmilaSaleReturnItem bsijaSaleReturnItem = new PmilaSaleReturnItem();
				bsijaSaleReturnItem.setProductAdd(item.getSku());
				bsijaSaleReturnItem.setQty(item.getQty());
				bsijaSaleReturnItem.setPriceActual(item.getPriceActual());
				bsijaSaleReturnItems.add(bsijaSaleReturnItem);
			}
			bsijaSaleReturn.setItems(bsijaSaleReturnItems);

			log.info("同步毕厶迦采购退货单（销售退货单）参数：{}", JSONObject.toJSONString(bsijaSaleReturn));
			ProcessOrderResponse response = burgeonRestClient.processOrder(bsijaSaleReturn, ObjectOperateType.CREATE);
			log.info("同步毕厶迦采购退货单（销售退货单）响应：{}", JSONObject.toJSONString(response));
			String bsijaNo = "";
			if (response.isRequestSuccess()) {
				List<QueryFilterParam> filterParamList = new ArrayList<>();
				filterParamList.add(new QueryFilterParam("ID", String.valueOf(response.getObjectid()), QueryFilterCombine.AND));

				bsijaSaleReturn = burgeonRestClient.queryObject(PmilaSaleReturn.class, filterParamList);
				if (null != bsijaSaleReturn) {
					bsijaNo = bsijaSaleReturn.getDocNo();
				}
			}
			purchaseDao.updatePurchaseReturnSyncStatus(purchase.getId(), response.isRequestSuccess()
					? Constants.BURGEON_YES : Constants.BURGEON_FAIL, bsijaNo, response.getMessage());
		}
		return purchases;
	}

	@Override
	public List<PurchaseReturnEntity> syncPmilaPurchaseReturnOut(String docNo, int page, int pageSize) {
		int beg = (page - 1) * pageSize + 1;
		int end = page * pageSize;
		List<PurchaseReturnEntity> purchases = purchaseDao.queryPurchaseReturnOutList(docNo, beg, end);
		for (PurchaseReturnEntity purchase : purchases) {
			ObjectSubmitResponse response = stockService.dealBsijaOut(purchase.getSourceNo(), "M_RET_SALEOUT",
					purchase.getItems());
			purchaseDao.updatePurchaseReturnOutSyncStatus(purchase.getId(), response.isRequestSuccess()
					? Constants.BURGEON_YES : Constants.BURGEON_FAIL, response.getMessage());
		}
		return purchases;
	}

	@Deprecated
	@Override
	public List<PurchaseReturnEntity> syncPmilaPurchaseReturnOrder(String docNo, int page, int pageSize) {
		int beg = (page - 1) * pageSize + 1;
		int end = page * pageSize;
		List<PurchaseReturnEntity> purchases = purchaseDao.queryPurchaseReturnOrderList(docNo, beg, end);
		for (PurchaseReturnEntity purchase : purchases) {
			PmilaSaleReturn pmilaSaleReturn = new PmilaSaleReturn();
			pmilaSaleReturn.setBillDate(purchase.getBillDate());
			pmilaSaleReturn.setRetSaleType("NOR");
			pmilaSaleReturn.setSourceNo(purchase.getDocNo());
			pmilaSaleReturn.setOrigAddName(purchase.getStoreName());
			pmilaSaleReturn.setDestAddName(purchase.getSupplierName());
			pmilaSaleReturn.setDescription(purchase.getDescription());
			List<PmilaSaleReturnItem> bsijaSaleReturnItems = new ArrayList<>();
			for (ItemEntity item : purchase.getItems()) {
				PmilaSaleReturnItem pmilaSaleReturnItem = new PmilaSaleReturnItem();
				pmilaSaleReturnItem.setProductAdd(item.getSku());
				pmilaSaleReturnItem.setQty(item.getQty());
				pmilaSaleReturnItem.setPriceActual(item.getPriceActual());
				bsijaSaleReturnItems.add(pmilaSaleReturnItem);
			}
			pmilaSaleReturn.setItems(bsijaSaleReturnItems);

			log.info("同步毕厶迦采购退货申请（销售退货单）参数：{}", JSONObject.toJSONString(pmilaSaleReturn));
			ProcessOrderResponse response = burgeonRestClient.processOrder(pmilaSaleReturn, ObjectOperateType.CREATE);
			log.info("同步毕厶迦采购退货申请（销售退货单）响应：{}", JSONObject.toJSONString(response));
			String bsijaNo = "";
			if (response.isRequestSuccess()) {
				List<QueryFilterParam> filterParamList = new ArrayList<>();
				filterParamList.add(new QueryFilterParam("ID", String.valueOf(response.getObjectid()), QueryFilterCombine.AND));

				pmilaSaleReturn = burgeonRestClient.queryObject(PmilaSaleReturn.class, filterParamList);
				if (null != pmilaSaleReturn) {
					bsijaNo = pmilaSaleReturn.getDocNo();

					// 执行出库
					// if (StringUtils.isNotEmpty(bsijaNo) && "未提交".equals(pmilaSaleReturn.getOutStatus())) {
					//     stockService.dealBsijaOut(bsijaNo, "M_RET_SALEOUT", null);
					// }
				}
			}
			purchaseDao.updatePurchaseReturnOrderSyncStatus(purchase.getId(), response.isRequestSuccess()
					? Constants.BURGEON_YES : Constants.BURGEON_FAIL, bsijaNo, response.getMessage());
		}
		return purchases;
	}

	@Override
	public List<PmilaCuspurchase> syncPmilaWbCuspurchase() {
		List<PurchaseEntity> purchaseEntityList = purchaseDao.queryPurchase();
		List<PmilaCuspurchase> pmilaCuspurchaseList = new ArrayList<>();
		for (PurchaseEntity purchase : purchaseEntityList) {
			PmilaCuspurchase pmilaCuspurchase = new PmilaCuspurchase();
			pmilaCuspurchase.setDocNo(purchase.getDocNo());
			pmilaCuspurchase.setBillDate(purchase.getBillDate());
			pmilaCuspurchase.setOrigCode(purchase.getSupplierCode());
			pmilaCuspurchase.setDestCode(purchase.getStoreCode());
			pmilaCuspurchase.setInDate(purchase.getInDate());
			pmilaCuspurchase.setDescription(purchase.getDescription());

			List<ItemEntity> items = new ArrayList<>();
			List<PmilaCuspurchaseItem> pmilaCuspurchaseItemList = new ArrayList<>();
			purchase.getItems().forEach(i -> {
				ItemEntity item = new ItemEntity();
				item.setSku(i.getSku());
				item.setQtyOut(i.getQty());
				item.setQtyIn(i.getQtyIn());
				item.setPriceActual(i.getPriceActual());
				items.add(item);
			});
			for (ItemEntity item : items) {
				PmilaCuspurchaseItem pmilaCuspurchaseItem = new PmilaCuspurchaseItem();
				pmilaCuspurchaseItem.setSku(item.getSku());
				pmilaCuspurchaseItem.setQtyOut(item.getQty());
				pmilaCuspurchaseItem.setQtyIn(item.getQtyIn());
				pmilaCuspurchaseItem.setPriceActual(item.getPriceActual());
				pmilaCuspurchaseItemList.add(pmilaCuspurchaseItem);
			}
			pmilaCuspurchase.setItems(pmilaCuspurchaseItemList);

			pmilaCuspurchaseList.add(pmilaCuspurchase);
		}
		return pmilaCuspurchaseList;
	}

	@Override
	public List<PurchaseReturnEntity> syncCuspurchaseReturn(String startTime, String docNo, int page, int pageSize) {
		int start = (page - 1) * pageSize;
		List<QueryFilterParam> filterParamList = new ArrayList<>();
		filterParamList.add(new QueryFilterParam("IN_STATUS", "2", QueryFilterCombine.AND));
		if (StringUtils.isNotEmpty(docNo)) {
			filterParamList.add(new QueryFilterParam("DOCNO", docNo, QueryFilterCombine.AND));
		}
		if (StringUtils.isNotEmpty(startTime)) {
			Date date = DateTimeUtils.offsetMinute(DateTimeUtils.convert(startTime), -1);
			filterParamList.add(new QueryFilterParam("", "M_V_RET_CUSPUR.INTIME > to_date('"
					+ DateTimeUtils.print(date) + "', 'yyyy-mm-dd hh24:mi:ss')", QueryFilterCombine.AND));
		}
		List<QueryOrderByParam> orderByParamList = new ArrayList<>();
		orderByParamList.add(new QueryOrderByParam("ID", true));

		List<PmilaCuspurchaseReturn> purchases = burgeonRestClient.query(PmilaCuspurchaseReturn.class, start, pageSize, filterParamList, orderByParamList);
		List<PurchaseReturnEntity> purchaseReturnList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(purchases)) {
			log.info("获取帕米拉销售退货单响应：{}", purchases);
			for (PmilaCuspurchaseReturn pmilaCuspurchaseReturn : purchases) {
//				PurchaseReturnEntity retPur = purchaseDao.queryPurchaseReturnBySourceNo(pmilaCuspurchaseReturn.getDocNo());
//				if (null != retPur) {
//					continue;
//				}
				PurchaseReturnEntity purchaseReturn = new PurchaseReturnEntity();
				purchaseReturn.setDocNo(pmilaCuspurchaseReturn.getDocNo());
				purchaseReturn.setBillDate(pmilaCuspurchaseReturn.getBillDate());
//				purchaseReturn.setSupplierCode(pmilaCuspurchaseReturn.getDestCode());
//				purchaseReturn.setStoreCode(pmilaCuspurchaseReturn.getOrigCode());
				purchaseReturn.setOutDate(pmilaCuspurchaseReturn.getOutDate());
				purchaseReturn.setOutStatus(pmilaCuspurchaseReturn.getOutStatus());
				purchaseReturn.setInStatus(pmilaCuspurchaseReturn.getInStatus());
				purchaseReturn.setInDate(pmilaCuspurchaseReturn.getInDate());
				purchaseReturn.setIsAutoOut("提交".equals(pmilaCuspurchaseReturn.getInStatus()));
//				if (StringUtils.isNotEmpty(pmilaCuspurchaseReturn.getSourceNo())) {
//					purchaseReturn.setPurchaseReturnOrderNo(pmilaCuspurchaseReturn.getSourceNo().startsWith("PRO")
//							? pmilaCuspurchaseReturn.getSourceNo() : null);
//				}
				purchaseReturn.setDescription(pmilaCuspurchaseReturn.getDescription());
				List<ItemEntity> items = new ArrayList<>();
				pmilaCuspurchaseReturn.getItems().forEach(i -> {
					ItemEntity item = new ItemEntity();
					item.setSku(i.getSku());
					item.setQty(i.getQty());
					item.setQtyOut(i.getQtyOut());
					item.setPriceActual(i.getPriceActual());
					items.add(item);
				});
				purchaseReturn.setItems(items);
				try {
					addPurchaseReturn(purchaseReturn);
				} catch (AlreadyExistsException e) {
					log.info(e.getMessage());
				}
				purchaseReturnList.add(purchaseReturn);
			}
		}
		return purchaseReturnList;
	}

	@Override
	public List<PmilaCuspurchaseReturn> syncWbCuspurchaseReturn() {
		List<PurchaseReturnEntity> purchaseReturnEntityList = purchaseDao.queryPurchaseReturn();
		List<PmilaCuspurchaseReturn> pmilaCuspurchaseReturnList = new ArrayList<>();
		for (PurchaseReturnEntity purchaseReturn : purchaseReturnEntityList) {
			PmilaCuspurchaseReturn pmilaCuspurchaseReturn = new PmilaCuspurchaseReturn();
			pmilaCuspurchaseReturn.setDocNo(purchaseReturn.getSourceNo());
			pmilaCuspurchaseReturn.setBillDate(purchaseReturn.getBillDate());
			pmilaCuspurchaseReturn.setDescription(purchaseReturn.getDescription());

			List<ItemEntity> items = new ArrayList<>();
			List<PmilaCuspurchaseReturnItem> pmilaCuspurchaseReturnItemList = new ArrayList<>();
			purchaseReturn.getItems().forEach(i -> {
				ItemEntity item = new ItemEntity();
				item.setSku(i.getSku());
				item.setQtyOut(i.getQty());
				item.setQtyIn(i.getQtyIn());
				item.setPriceActual(i.getPriceActual());
				items.add(item);
			});
			for (ItemEntity item : items) {
				PmilaCuspurchaseReturnItem pmilaCuspurchaseReturnItem = new PmilaCuspurchaseReturnItem();
				pmilaCuspurchaseReturnItem.setSku(item.getSku());
				pmilaCuspurchaseReturnItem.setQtyOut(item.getQty());
				pmilaCuspurchaseReturnItem.setQtyIn(item.getQtyIn());
				pmilaCuspurchaseReturnItem.setPriceActual(item.getPriceActual());
				pmilaCuspurchaseReturnItemList.add(pmilaCuspurchaseReturnItem);
			}
			pmilaCuspurchaseReturn.setItems(pmilaCuspurchaseReturnItemList);
			pmilaCuspurchaseReturnList.add(pmilaCuspurchaseReturn);
		}
		return pmilaCuspurchaseReturnList;
	}

	private void dealItem(Long mainId, ItemEntity item) {
		item.setMainId(mainId);
		if (null == item.getSku()) {
			throw new ParamNullException("sku", "sku参数不能为空");
		}
		SkuEntity sku = productDao.queryProductBySku(item.getSku());
		if (null == sku) {
			sku = productDao.queryProductByForCode(item.getSku());
			if (null == sku) {
				throw new ParamNotMatchException("sku", "sku[" + item.getSku() + "]不存在");
			}
		}
		item.setProductId(sku.getProductId());
		item.setSkuId(sku.getId());
		item.setAsiId(sku.getAsiId());
	}

}
