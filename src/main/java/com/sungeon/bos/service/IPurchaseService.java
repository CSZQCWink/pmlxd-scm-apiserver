package com.sungeon.bos.service;

import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import com.sungeon.bos.entity.pmila.PmilaCuspurchase;
import com.sungeon.bos.entity.pmila.PmilaCuspurchaseReturn;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Service
public interface IPurchaseService {

	Integer addPurchase(PurchaseEntity purchase);

	Integer addPurchaseReturn(PurchaseReturnEntity purchaseReturn);

	List<PurchaseReturnEntity> syncPmilaPurchaseReturn(String docNo, int page, int pageSize);

	List<PurchaseReturnEntity> syncPmilaPurchaseReturnOut(String docNo, int page, int pageSize);

	List<PurchaseReturnEntity> syncPmilaPurchaseReturnOrder(String docNo, int page, int pageSize);

	// 查询所有的采购单
	List<PmilaCuspurchase> syncPmilaWbCuspurchase();

	// 根据经销商采购退货单单号同步采购退货单
	List<PurchaseReturnEntity> syncCuspurchaseReturn(String startTime, String docNo, int page, int pageSize);

	// 查询所有的采购退货单
	List<PmilaCuspurchaseReturn> syncWbCuspurchaseReturn();


}
