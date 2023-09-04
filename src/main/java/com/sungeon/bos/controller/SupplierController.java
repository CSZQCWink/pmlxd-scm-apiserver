package com.sungeon.bos.controller;

import com.sungeon.bos.core.application.SungeonBaseController;
import com.sungeon.bos.core.model.ValueHolder;
import com.sungeon.bos.entity.base.ProductEntity;
import com.sungeon.bos.entity.base.SupplierEntity;
import com.sungeon.bos.service.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @BelongsPackage: com.sungeon.bos.controller
 * @ClassName: SupplierController
 * @Author: 陈苏洲
 * @Description: 供应商Controller
 * @CreateTime: 2023-08-30 13:12
 * @Version: 1.0
 */

@Controller
@RequestMapping("/Supplier/")
public class SupplierController extends SungeonBaseController {

	@Autowired
	private ISupplierService supplierService;

	/**
	 * @title: syncProduct
	 * @author: 陈苏洲
	 * @description: 供应商同步
	 * @param: [code]
	 * @return: com.sungeon.bos.core.model.ValueHolder<java.util.List<com.sungeon.bos.entity.base.SupplierEntity>>
	 * @date: 2023/9/4 9:54
	 **/
	@RequestMapping("/Sync")
	@ResponseBody
	public ValueHolder<List<SupplierEntity>> syncProduct(String code) {
		return ValueHolder.ok(supplierService.syncPmilaSupplier(null, code, 1, 1));
	}

}
