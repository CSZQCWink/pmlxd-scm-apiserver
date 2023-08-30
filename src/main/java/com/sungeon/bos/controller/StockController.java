package com.sungeon.bos.controller;

import com.sungeon.bos.core.application.SungeonBaseController;
import com.sungeon.bos.core.model.ValueHolder;
import com.sungeon.bos.entity.base.InventoryEntity;
import com.sungeon.bos.service.IStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @title: 盘点单Controller
 * @author: 陈苏洲
 * @date: 2023/8/30 9:31
 **/
@Slf4j
@Controller
@RequestMapping("/Stock")
public class StockController extends SungeonBaseController {

	@Autowired
	private IStockService stockService;

	@RequestMapping("/Inventory/Sync")
	@ResponseBody
	public ValueHolder<List<InventoryEntity>> syncTransfer(String docNo) {
		return ValueHolder.ok(stockService.syncBsijaInventory(docNo, 1, 1));
	}

}
