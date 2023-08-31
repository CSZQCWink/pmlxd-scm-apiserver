package com.sungeon.bos.service;

import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Service
public interface ISaleService {


	// 根据经销商采购单同步采购单
	List<PurchaseEntity> syncPmilaCuspurchase(String startTime, String docNo, int page, int pageSize);

	// 根据销售单同步采购单
	List<PurchaseEntity> syncPmilaSale(String startTime, String docNo, int page, int pageSize);

	// 根据销售退货单同步采购退货单
	List<PurchaseReturnEntity> syncPmilaSaleReturn(String startTime, String docNo, int page, int pageSize);

}
