package com.sungeon.bos.task;

import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.SupplierEntity;
import com.sungeon.bos.service.ISupplierService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @BelongsPackage: com.sungeon.bos.task
 * @ClassName: SupplierTask
 * @Author: 陈苏洲
 * @Description: 供应商任务
 * @CreateTime: 2023-09-04 10:01
 * @Version: 1.0
 */

@Slf4j
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Component
public class SupplierTask extends BaseTask{

	@Autowired
	private ISupplierService supplierService;

	public void syncPmilaSupplier(){
		try {
			List<SupplierEntity> supplierEntityList;
			String startTime = baseService.getThirdTime("BSIJA_SALE_SYNC_TIME");
			Date now = new Date();
			int page = 1;
			int pageSize = 30;
			do {
				supplierEntityList = supplierService.syncPmilaSupplier(startTime, null, page++, pageSize);
			} while (supplierEntityList.size() == pageSize);
			baseService.updateThirdTime("BSIJA_SALE_SYNC_TIME", now);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
