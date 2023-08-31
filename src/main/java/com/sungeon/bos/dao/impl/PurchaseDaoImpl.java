package com.sungeon.bos.dao.impl;

import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.core.annotation.SgExceptionField;
import com.sungeon.bos.core.exception.InsertFailException;
import com.sungeon.bos.core.exception.ProcedureErrorException;
import com.sungeon.bos.core.exception.QueryFailException;
import com.sungeon.bos.core.exception.UpdateFailException;
import com.sungeon.bos.dao.IPurchaseDao;
import com.sungeon.bos.entity.BosResult;
import com.sungeon.bos.entity.base.ItemEntity;
import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import com.sungeon.bos.mapper.IPurchaseMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Repository("purchaseDao")
public class PurchaseDaoImpl extends BaseDaoImpl implements IPurchaseDao {

	@Resource
	private IPurchaseMapper purchaseMapper;

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public Long queryPurchaseIdBySourceNo(String sourceNo) {
		return purchaseMapper.queryPurchaseIdBySourceNo(sourceNo);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public PurchaseEntity queryPOSupplierStore(String poDocno) {
		return purchaseMapper.queryPOSupplierStore(poDocno);
	}

	@SgExceptionField(exception = InsertFailException.class)
	@Override
	public Integer insertPurchase(PurchaseEntity purchase) {
		return purchaseMapper.insertPurchase(purchase);
	}

	@SgExceptionField(exception = InsertFailException.class)
	@Override
	public Integer insertPurchaseItem(ItemEntity item) {
		return purchaseMapper.insertPurchaseItem(item);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callPurchaseItemAm(Long itemId) {
		BosResult result = new BosResult();
		purchaseMapper.callPurchaseItemAm(itemId);
		result.setCode(1);
		result.setMessage("SUCCESS");
		return result;
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callPurchaseAm(Long purchaseId) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("id", purchaseId);
		purchaseMapper.callPurchaseAm(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callPurchaseSubmit(Long purchaseId) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("id", purchaseId);
		purchaseMapper.callPurchaseSubmit(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callPurchaseUnSubmit(Long purchaseId) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("id", purchaseId);
		purchaseMapper.callPurchaseUnSubmit(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callPurchaseInQtyCop(Long purchaseId) {
		Map<String, Object> map = new HashMap<>(4);
		map.put("id", purchaseId);
		map.put("userId", 893);
		purchaseMapper.callPurchaseInQtyCop(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callPurchaseInSubmit(Long purchaseId) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("id", purchaseId);
		purchaseMapper.callPurchaseInSubmit(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public List<PurchaseEntity> queryPurchaseInList(String docNo, int beg, int end) {
		return purchaseMapper.queryPurchaseInList(docNo, beg, end);
	}

	@SgExceptionField(exception = UpdateFailException.class)
	@Override
	public Integer updatePurchaseSyncStatus(Long purchaseId, String status, String message) {
		return purchaseMapper.updatePurchaseSyncStatus(purchaseId, status, message);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public PurchaseReturnEntity queryPurchaseReturnBySourceNo(String sourceNo) {
		return purchaseMapper.queryPurchaseReturnBySourceNo(sourceNo);
	}

	@SgExceptionField(exception = InsertFailException.class)
	@Override
	public Integer insertRetPur(PurchaseReturnEntity retPur) {
		return purchaseMapper.insertRetPur(retPur);
	}

	@SgExceptionField(exception = InsertFailException.class)
	@Override
	public Integer insertRetPurItem(ItemEntity item) {
		return purchaseMapper.insertRetPurItem(item);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callRetPurItemAcm(Long itemId) {
		BosResult result = new BosResult();
		purchaseMapper.callRetPurItemAcm(itemId);
		result.setCode(1);
		result.setMessage("SUCCESS");
		return result;
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callRetPurAm(Long retPurId) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("id", retPurId);
		purchaseMapper.callRetPurAm(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callRetPurSubmit(Long retPurId) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("id", retPurId);
		purchaseMapper.callRetPurSubmit(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callRetPurUnSubmit(Long retPurId) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("id", retPurId);
		purchaseMapper.callRetPurUnSubmit(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = UpdateFailException.class)
	@Override
	public Integer updateRetPurOutItem(ItemEntity item) {
		return purchaseMapper.updateRetPurOutItem(item);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callRetPurOutQtyCop(Long retPurId) {
		Map<String, Object> map = new HashMap<>(4);
		map.put("id", retPurId);
		map.put("userId", 893);
		purchaseMapper.callRetPurOutQtyCop(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = ProcedureErrorException.class)
	@Override
	public BosResult callRetPurOutSubmit(Long retPurId) {
		Map<String, Object> map = new HashMap<>(3);
		map.put("id", retPurId);
		purchaseMapper.callRetPurOutSubmit(map);
		return JSONObject.parseObject(new JSONObject(map).toString(), BosResult.class);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public List<PurchaseReturnEntity> queryPurchaseReturnList(String docNo, int beg, int end) {
		return purchaseMapper.queryPurchaseReturnList(docNo, beg, end);
	}

	@SgExceptionField(exception = UpdateFailException.class)
	@Override
	public Integer updatePurchaseReturnSyncStatus(Long purchaseReturnId, String status, String bsijaNo, String message) {
		return purchaseMapper.updatePurchaseReturnSyncStatus(purchaseReturnId, status, bsijaNo, message);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public List<PurchaseReturnEntity> queryPurchaseReturnOutList(String docNo, int beg, int end) {
		return purchaseMapper.queryPurchaseReturnOutList(docNo, beg, end);
	}

	@SgExceptionField(exception = UpdateFailException.class)
	@Override
	public Integer updatePurchaseReturnOutSyncStatus(Long purchaseReturnId, String status, String message) {
		return purchaseMapper.updatePurchaseReturnOutSyncStatus(purchaseReturnId, status, message);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public Long queryPurchaseReturnOrderIdByDocno(String purchaseReturnOrderNo) {
		return purchaseMapper.queryPurchaseReturnOrderIdByDocno(purchaseReturnOrderNo);
	}

	@SgExceptionField(exception = QueryFailException.class)
	@Override
	public List<PurchaseReturnEntity> queryPurchaseReturnOrderList(String docNo, int beg, int end) {
		return purchaseMapper.queryPurchaseReturnOrderList(docNo, beg, end);
	}

	@SgExceptionField(exception = UpdateFailException.class)
	@Override
	public Integer updatePurchaseReturnOrderSyncStatus(Long purchaseReturnOrderId, String status, String bsijaNo, String message) {
		return purchaseMapper.updatePurchaseReturnOrderSyncStatus(purchaseReturnOrderId, status, bsijaNo, message);
	}

	@SgExceptionField(exception = UpdateFailException.class)
	@Override
	public List<PurchaseEntity> queryPurchaseIdBydocNo() {
		return purchaseMapper.queryPurchaseIdBydoceNo();
	}

}
