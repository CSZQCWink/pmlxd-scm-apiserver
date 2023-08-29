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
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Controller
@RequestMapping("/Product")
public class ProductController extends SungeonBaseController {

    @Autowired
    private IProductService productService;

    @RequestMapping("/Sync")
    @ResponseBody
    public ValueHolder<List<ProductEntity>> syncProduct(String productCode) {
        return ValueHolder.ok(productService.syncBsijaProduct(null, productCode, 1, 1));
    }

}
