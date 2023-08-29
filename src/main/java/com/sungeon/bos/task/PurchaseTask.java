package com.sungeon.bos.task;

import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import com.sungeon.bos.service.IPurchaseService;
import com.sungeon.bos.util.SystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author : 刘国帅
 * @date : 2016-12-6
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class PurchaseTask extends BaseTask {

    @Autowired
    private IPurchaseService purchaseService;

    public void syncBsijaPurchaseReturn() {
        try {
            List<PurchaseReturnEntity> purchases;
            int page = 1;
            // do {
            //     purchases = purchaseService.syncBsijaPurchaseReturn(null, page++, SystemProperties.ParamDataCount);
            // } while (!CollectionUtils.isEmpty(purchases));
            // page = 1;
            // do {
            //     purchases = purchaseService.syncBsijaPurchaseReturnOut(null, page++, SystemProperties.ParamDataCount);
            // } while (!CollectionUtils.isEmpty(purchases));
            // page = 1;
            do {
                purchases = purchaseService.syncBsijaPurchaseReturnOrder(null, page++, SystemProperties.ParamDataCount);
            } while (!CollectionUtils.isEmpty(purchases));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
