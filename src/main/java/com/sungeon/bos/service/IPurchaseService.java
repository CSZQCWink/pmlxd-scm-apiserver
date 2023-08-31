package com.sungeon.bos.service;

import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import com.sungeon.bos.entity.pmila.PmilaCuspurchase;
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

	// 根据单据编号查询
	List<PmilaCuspurchase> syncPmilaWbCuspurchase();

}
