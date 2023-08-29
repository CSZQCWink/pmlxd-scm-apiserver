package com.sungeon.bos.controller;

import com.sungeon.bos.core.application.SungeonBaseController;
import com.sungeon.bos.core.model.ValueHolder;
import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import com.sungeon.bos.service.IPurchaseService;
import com.sungeon.bos.service.ISaleService;
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
@RequestMapping("/Purchase")
public class PurchaseController extends SungeonBaseController {

    @Autowired
    private ISaleService saleService;
    @Autowired
    private IPurchaseService purchaseService;

    @RequestMapping("/Sync")
    @ResponseBody
    public ValueHolder<List<PurchaseEntity>> syncPurchase(String docNo) {
        return ValueHolder.ok(saleService.syncBsijaSale(null, docNo, 1, 1));
    }

    @RequestMapping("/Return/Sync")
    @ResponseBody
    public ValueHolder<List<PurchaseReturnEntity>> syncPurchaseReturn(String docNo) {
        return ValueHolder.ok(saleService.syncBsijaSaleReturn(null, docNo, 1, 1));
    }

    @RequestMapping("/ReturnOrder/Sync")
    @ResponseBody
    public ValueHolder<List<PurchaseReturnEntity>> syncPurchaseReturnOrder(String docNo) {
        return ValueHolder.ok(purchaseService.syncBsijaPurchaseReturnOrder(docNo, 1, 1));
    }

}
