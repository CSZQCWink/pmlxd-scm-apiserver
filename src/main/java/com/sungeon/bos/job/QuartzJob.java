package com.sungeon.bos.job;

import com.sungeon.bos.core.constants.Constants;
import com.sungeon.bos.entity.ScheduleJob;
import com.sungeon.bos.service.IBaseService;
import com.sungeon.bos.task.*;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class QuartzJob implements InterruptableJob {

    private boolean interrupted = false;
    @Value("${ScheduleGroup}")
    private String scheduleGroup;
    @Autowired
    protected IBaseService baseService;
    @Autowired
    private ProductTask productTask;
    @Autowired
    private PurchaseTask purchaseTask;
    @Autowired
    private SaleTask saleTask;
    @Autowired
    private TransferTask transferTask;
    @Autowired
    private RetailTask retailTask;
    @Autowired
    private StockTask stockTask;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        executeInternal(context);
    }

    public void executeInternal(JobExecutionContext context) {
        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
        log.debug("调度任务 - [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "] 开始");
        if (scheduleJob.getStatus().equals(Constants.BURGEON_YES) && scheduleGroup.equals(scheduleJob.getGroupName())) {
            switch (scheduleJob.getJobName()) {
                case "SyncBsijaDim":
                    productTask.syncBsijaDim();
                    break;
                case "SyncBsijaColor":
                    productTask.syncBsijaColor();
                    break;
                case "SyncBsijaSize":
                    productTask.syncBsijaSize();
                    break;
                case "SyncBsijaProduct":
                    productTask.syncBsijaProduct();
                    break;
                case "SyncBsijaPurchaseReturn":
                    purchaseTask.syncBsijaPurchaseReturn();
                    break;
                case "SyncBsijaSale":
                    saleTask.syncBsijaSale();
                    break;
                case "SyncBsijaSaleReturn":
                    saleTask.syncBsijaSaleReturn();
                    break;
                case "SyncBsijaTransfer":
                    transferTask.syncBsijaTransfer();
                    break;
                case "SyncBsijaRetail":
                    retailTask.syncBsijaRetail();
                    break;
                case "SyncBsijaInventory":
                    stockTask.syncBsijaInventory();
                    break;
                default:
                    break;
            }
        }
        log.debug("调度任务 - [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "] 结束");
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        interrupted = true;
    }

}
