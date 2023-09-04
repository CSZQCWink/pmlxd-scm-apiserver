package com.sungeon.bos.controller;

import com.sungeon.bos.core.application.SungeonBaseController;
import com.sungeon.bos.core.model.ValueHolder;
import com.sungeon.bos.entity.base.ProductEntity;
import com.sungeon.bos.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @title: 款号档案Controller
 * @author: 陈苏洲
 * @date: 2023/8/30 9:35
 **/
@Slf4j
@Controller
@RequestMapping("/Product")
public class ProductController extends SungeonBaseController {

	@Autowired
	private IProductService productService;

	/**
	 * @title: syncProduct
	 * @author: 陈苏洲
	 * @description: 同步帕米拉款号商品
	 * @param: [productCode]
	 * @return: com.sungeon.bos.core.model.ValueHolder<java.util.List<com.sungeon.bos.entity.base.ProductEntity>>
	 * @date: 2023/9/4 9:46
	 **/
	@RequestMapping("/Sync")
	@ResponseBody
	public ValueHolder<List<ProductEntity>> syncProduct(String productCode) {
		return ValueHolder.ok(productService.syncPmilaProduct(null, productCode, 1, 1));
	}

}
