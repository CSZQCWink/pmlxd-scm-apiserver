package com.sungeon.bos.task;

import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import com.sungeon.bos.service.ISaleService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author : 刘国帅
 * @date : 2016-12-6
 */
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class SaleTask extends BaseTask {

    @Autowired
    private ISaleService saleService;

    public void syncBsijaSale() {
        try {
            List<PurchaseEntity> purchases;
            String startTime = baseService.getThirdTime("BSIJA_SALE_SYNC_TIME");
            Date now = new Date();
            int page = 1;
            int pageSize = 30;
            do {
                purchases = saleService.syncBsijaSale(startTime, null, page++, pageSize);
            } while (purchases.size() == pageSize);
            baseService.updateThirdTime("BSIJA_SALE_SYNC_TIME", now);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void syncBsijaSaleReturn() {
        try {
            List<PurchaseReturnEntity> purchases;
            String startTime = baseService.getThirdTime("BSIJA_SALE_RETURN_SYNC_TIME");
            Date now = new Date();
            int page = 1;
            int pageSize = 30;
            do {
                purchases = saleService.syncBsijaSaleReturn(startTime, null, page++, pageSize);
            } while (purchases.size() == pageSize);
            baseService.updateThirdTime("BSIJA_SALE_RETURN_SYNC_TIME", now);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
