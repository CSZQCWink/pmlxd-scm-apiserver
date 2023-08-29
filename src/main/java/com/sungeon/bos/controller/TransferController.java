package com.sungeon.bos.controller;

import com.sungeon.bos.core.application.SungeonBaseController;
import com.sungeon.bos.core.model.ValueHolder;
import com.sungeon.bos.entity.base.TransferEntity;
import com.sungeon.bos.service.ITransferService;
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
@RequestMapping("/Transfer")
public class TransferController extends SungeonBaseController {

    @Autowired
    private ITransferService transferService;

    @RequestMapping("/Sync")
    @ResponseBody
    public ValueHolder<List<TransferEntity>> syncTransfer(String docNo) {
        return ValueHolder.ok(transferService.syncBsijaTransfer(docNo, 1, 1));
    }

}
