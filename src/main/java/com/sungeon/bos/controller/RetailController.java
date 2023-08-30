package com.sungeon.bos.controller;

import com.sungeon.bos.core.application.SungeonBaseController;
import com.sungeon.bos.core.model.ValueHolder;
import com.sungeon.bos.entity.base.RetailEntity;
import com.sungeon.bos.service.IRetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @title: 零售单Controller
 * @author: 陈苏洲
 * @date: 2023/8/30 9:34
 **/
@Slf4j
@Controller
@RequestMapping("/Retail")
public class RetailController extends SungeonBaseController {

	@Autowired
	private IRetailService retailService;

	@RequestMapping("/Sync")
	@ResponseBody
	public ValueHolder<List<RetailEntity>> syncTransfer(String docNo) {
		return ValueHolder.ok(retailService.syncBsijaRetail(docNo, 1, 1));
	}

}
