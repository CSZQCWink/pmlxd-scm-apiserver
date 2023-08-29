package com.sungeon.bos.mapper;

import com.sungeon.bos.entity.base.ItemEntity;
import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Mapper
public interface IPurchaseMapper {

    Long queryPurchaseIdBySourceNo(String sourceNo);

    PurchaseEntity queryPOSupplierStore(String poDocno);

    Integer insertPurchase(PurchaseEntity purchase);

    Integer insertPurchaseItem(ItemEntity item);

    void callPurchaseItemAm(@Param("itemId") Long itemId);

    void callPurchaseAm(Map<String, Object> map);

    void callPurchaseSubmit(Map<String, Object> map);

    void callPurchaseUnSubmit(Map<String, Object> map);

    void callPurchaseInQtyCop(Map<String, Object> map);

    void callPurchaseInSubmit(Map<String, Object> map);

    List<PurchaseEntity> queryPurchaseInList(@Param("docNo") String docNo, @Param("beg") int beg, @Param("end") int end);

    Integer updatePurchaseSyncStatus(@Param("purchaseId") Long purchaseId, @Param("status") String status,
                                     @Param("message") String message);

    PurchaseReturnEntity queryPurchaseReturnBySourceNo(String sourceNo);

    Integer insertRetPur(PurchaseReturnEntity retPur);

    Integer insertRetPurItem(ItemEntity item);

    void callRetPurItemAcm(@Param("itemId") Long itemId);

    void callRetPurAm(Map<String, Object> map);

    void callRetPurSubmit(Map<String, Object> map);

    void callRetPurUnSubmit(Map<String, Object> map);

    Integer updateRetPurOutItem(ItemEntity item);

    void callRetPurOutQtyCop(Map<String, Object> map);

    void callRetPurOutSubmit(Map<String, Object> map);

    List<PurchaseReturnEntity> queryPurchaseReturnList(@Param("docNo") String docNo, @Param("beg") int beg,
                                                       @Param("end") int end);

    Integer updatePurchaseReturnSyncStatus(@Param("purchaseReturnId") Long purchaseReturnId, @Param("status") String status,
                                           @Param("bsijaNo") String bsijaNo, @Param("message") String message);

    List<PurchaseReturnEntity> queryPurchaseReturnOutList(@Param("docNo") String docNo, @Param("beg") int beg,
                                                          @Param("end") int end);

    Integer updatePurchaseReturnOutSyncStatus(@Param("purchaseReturnId") Long purchaseReturnId, @Param("status") String status,
                                              @Param("message") String message);

    Long queryPurchaseReturnOrderIdByDocno(String purchaseReturnOrderNo);

    List<PurchaseReturnEntity> queryPurchaseReturnOrderList(@Param("docNo") String docNo, @Param("beg") int beg, @Param("end") int end);

    Integer updatePurchaseReturnOrderSyncStatus(@Param("purchaseReturnOrderId") Long purchaseReturnOrderId,
                                                @Param("status") String status, @Param("bsijaNo") String bsijaNo,
                                                @Param("message") String message);

}
