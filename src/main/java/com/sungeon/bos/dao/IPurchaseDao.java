package com.sungeon.bos.dao;

import com.sungeon.bos.entity.BosResult;
import com.sungeon.bos.entity.base.ItemEntity;
import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Repository
public interface IPurchaseDao {

	Long queryPurchaseIdBySourceNo(String sourceNo);

	PurchaseEntity queryPOSupplierStore(String poDocno);

	Integer insertPurchase(PurchaseEntity purchase);

	Integer insertPurchaseItem(ItemEntity item);

	BosResult callPurchaseItemAm(Long itemId);

	BosResult callPurchaseAm(Long purchaseId);

	BosResult callPurchaseSubmit(Long purchaseId);

	BosResult callPurchaseUnSubmit(Long purchaseId);

	BosResult callPurchaseInQtyCop(Long purchaseId);

	BosResult callPurchaseInSubmit(Long purchaseId);

	List<PurchaseEntity> queryPurchaseInList(String docNo, int beg, int end);

	Integer updatePurchaseSyncStatus(Long purchaseId, String status, String message);

	PurchaseReturnEntity queryPurchaseReturnBySourceNo(String sourceNo);

	Integer insertRetPur(PurchaseReturnEntity retPur);

	Integer insertRetPurItem(ItemEntity item);

	Integer updateRetPurOutItem(ItemEntity item);

	BosResult callRetPurItemAcm(Long itemId);

	BosResult callRetPurAm(Long retPurId);

	BosResult callRetPurSubmit(Long retPurId);

	BosResult callRetPurUnSubmit(Long retPurId);

	BosResult callRetPurOutQtyCop(Long retPurId);

	BosResult callRetPurOutSubmit(Long retPurId);

	List<PurchaseReturnEntity> queryPurchaseReturnList(String docNo, int beg, int end);

	Integer updatePurchaseReturnSyncStatus(Long purchaseReturnId, String status, String bsijaNo, String message);

	List<PurchaseReturnEntity> queryPurchaseReturnOutList(String docNo, int beg, int end);

	Integer updatePurchaseReturnOutSyncStatus(Long purchaseReturnId, String status, String message);

	Long queryPurchaseReturnOrderIdByDocno(String purchaseReturnOrderNo);

	List<PurchaseReturnEntity> queryPurchaseReturnOrderList(String docNo, int beg, int end);

	Integer updatePurchaseReturnOrderSyncStatus(Long purchaseReturnOrderId, String status, String bsijaNo, String message);

	List<PurchaseEntity> queryPurchase();

	List<PurchaseReturnEntity> queryPurchaseReturn();
}
