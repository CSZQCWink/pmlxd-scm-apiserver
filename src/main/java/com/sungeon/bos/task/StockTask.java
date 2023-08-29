package com.sungeon.bos.task;

import com.sungeon.bos.entity.base.InventoryEntity;
import com.sungeon.bos.service.IStockService;
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
public class StockTask extends BaseTask {

    @Autowired
    private IStockService stockService;

    public void syncBsijaInventory() {
        try {
            List<InventoryEntity> inventories;
            int page = 1;
            do {
                inventories = stockService.syncBsijaInventory(null, page++, SystemProperties.ParamDataCount);
            } while (!CollectionUtils.isEmpty(inventories));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
