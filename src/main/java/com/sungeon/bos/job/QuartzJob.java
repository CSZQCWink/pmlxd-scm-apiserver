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
 *
 * @updateAuthor 陈苏洲
 * @date 2023-09-4
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

	@Autowired
	private SupplierTask supplierTask;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		executeInternal(context);
	}

	public void executeInternal(JobExecutionContext context) {
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		log.debug("调度任务 - [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "] 开始");
		if (scheduleJob.getStatus().equals(Constants.BURGEON_YES) && scheduleGroup.equals(scheduleJob.getGroupName())) {
			switch (scheduleJob.getJobName()) {
				// 同步Dim(正常使用)
				case "SyncPmilaDim":
					productTask.syncPmilaDim();
					break;
				// 同步颜色(正常使用)
				case "SyncPmilaColor":
					productTask.syncPmilaColor();
					break;
				// 同步尺寸(正常使用)
				case "SyncPmilaSize":
					productTask.syncPmilaSize();
					break;
				// 同步款号档案
				case "SyncPmilaProduct":
					productTask.syncPmilaProduct();
					break;
				// 同步供应商(正常使用)
				// TODO 需要在实体类上添加属性 同步的时候需要注意insert语句中添加相应的值
				case "SyncPmilaSupplier":
					supplierTask.syncPmilaSupplier();
					break;
				// 同步帕米拉采购单
				case "SyncPmilaCuspurchase":
					saleTask.syncPmilaCuspurchase();
					break;
				// 同步采购退货单
				case "SyncCuspurchaseReturn":
					purchaseTask.syncCuspurchaseReturn();
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
