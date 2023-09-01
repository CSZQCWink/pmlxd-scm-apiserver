package com.sungeon.bos.controller;

import com.sungeon.bos.core.application.SungeonBaseController;
import com.sungeon.bos.core.model.ValueHolder;
import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import com.sungeon.bos.entity.pmila.PmilaCuspurchase;
import com.sungeon.bos.entity.pmila.PmilaCuspurchaseReturn;
import com.sungeon.bos.service.IPurchaseService;
import com.sungeon.bos.service.ISaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @title: 采购单Controller
 * @author: 陈苏洲
 * @date: 2023/8/30 9:35
 **/
@Slf4j
@Controller
@RequestMapping("/Purchase")
public class PurchaseController extends SungeonBaseController {

	@Autowired
	private ISaleService saleService;
	@Autowired
	private IPurchaseService purchaseService;

	/**
	 * @title: syncCuspurchase
	 * @author: 陈苏洲
	 * @description: 根据经销商采购单同步采购单
	 * @param: [docNo]
	 * @return: com.sungeon.bos.core.model.ValueHolder<java.util.List<com.sungeon.bos.entity.base.PurchaseEntity>>
	 * @date: 2023/9/1 9:17
	 **/
	@RequestMapping("/Cuspurchase")
	@ResponseBody
	public ValueHolder<List<PurchaseEntity>> syncCuspurchase(String docNo) {
		return ValueHolder.ok(saleService.syncPmilaCuspurchase(null, docNo, 1, 1));
	}

	/**
	 * @title: syncWbCuspurchase
	 * @author: 陈苏洲
	 * @description: TODO 目前已经将所有的已入库的采购单同步 就需要最后的提交给品牌方 但是现在还是不知道到底使用哪个方法
	 * @param: []
	 * @return: com.sungeon.bos.core.model.ValueHolder<java.util.List<com.sungeon.bos.entity.pmila.PmilaCuspurchase>>
	 * @throws: 
	 * @date: 2023/8/31 15:59
	 **/
	@RequestMapping("/WbCuspurchase")
	@ResponseBody
	public ValueHolder<List<PmilaCuspurchase>> syncWbCuspurchase() {
		return ValueHolder.ok(purchaseService.syncPmilaWbCuspurchase());
	}

	/**
	 * @title: syncCuspurchaseReturn
	 * @author: 陈苏洲
	 * @description: 根据经销商采购退货单单号同步采购退货单
	 * @param: [docNo]
	 * @return: com.sungeon.bos.core.model.ValueHolder<java.util.List<com.sungeon.bos.entity.base.PurchaseReturnEntity>>
	 * @date: 2023/9/1 9:16
	 **/
	@RequestMapping("/Return/syncCuspurchaseReturn")
	@ResponseBody
	public ValueHolder<List<PurchaseReturnEntity>> syncCuspurchaseReturn(String docNo) {
		return ValueHolder.ok(purchaseService.syncCuspurchaseReturn(null, docNo, 1, 1));
	}

	/**
	 * @title: syncWbCuspurchaseReturn
	 * @author: 陈苏洲
	 * @description: 将帕米拉已出库的采购退货单传到品牌方系统,生成已出库未入库的经销商采购退货单
	 * @param: []
	 * @return: com.sungeon.bos.core.model.ValueHolder<java.util.List<com.sungeon.bos.entity.pmila.PmilaCuspurchaseReturn>>
	 * @date: 2023/9/1 14:19
	 **/
	@RequestMapping("/WbCuspurchaseReturn")
	@ResponseBody
	public ValueHolder<List<PmilaCuspurchaseReturn>> syncWbCuspurchaseReturn() {
		return ValueHolder.ok(purchaseService.syncWbCuspurchaseReturn());
	}

	@RequestMapping("/Sync")
	@ResponseBody
	public ValueHolder<List<PurchaseEntity>> syncPurchase(String docNo) {
		return ValueHolder.ok(saleService.syncPmilaSale(null, docNo, 1, 1));
	}

	@RequestMapping("/Return/Sync")
	@ResponseBody
	public ValueHolder<List<PurchaseReturnEntity>> syncPurchaseReturn(String docNo) {
		return ValueHolder.ok(saleService.syncPmilaSaleReturn(null, docNo, 1, 1));
	}

	@RequestMapping("/ReturnOrder/Sync")
	@ResponseBody
	public ValueHolder<List<PurchaseReturnEntity>> syncPurchaseReturnOrder(String docNo) {
		return ValueHolder.ok(purchaseService.syncPmilaPurchaseReturnOrder(docNo, 1, 1));
	}

}
