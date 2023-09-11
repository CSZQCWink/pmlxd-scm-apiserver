package com.sungeon.bos.task;

import com.sungeon.bos.entity.base.PurchaseEntity;
import com.sungeon.bos.entity.base.PurchaseReturnEntity;
import com.sungeon.bos.entity.pmila.PmilaCuspurchase;
import com.sungeon.bos.entity.pmila.PmilaCuspurchaseReturn;
import com.sungeon.bos.service.IPurchaseService;
import com.sungeon.bos.util.SystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
public class PurchaseTask extends BaseTask {

	@Autowired
	private IPurchaseService purchaseService;

	public void syncPmilaPurchaseReturn() {
		try {
			List<PurchaseReturnEntity> purchases;
			int page = 1;
			do {
				purchases = purchaseService.syncPmilaPurchaseReturnOrder(null, page++, SystemProperties.ParamDataCount);
			} while (!CollectionUtils.isEmpty(purchases));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void syncCuspurchaseReturn() {
		try {
			List<PurchaseReturnEntity> purchaseReturnEntityList;
			String startTime = baseService.getThirdTime("BSIJA_SALE_RETURN_SYNC_TIME");
			Date now = new Date();
			int page = 1;
			int pageSize = 30;
			do {
				purchaseReturnEntityList = purchaseService.syncCuspurchaseReturn(startTime, null, page++, pageSize);
			} while (purchaseReturnEntityList.size() == pageSize);
			baseService.updateThirdTime("BSIJA_SALE_RETURN_SYNC_TIME", now);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void syncPmilaWbCuspurchase() {
		try {
			List<PmilaCuspurchase> wbCuspurchase;
			String startTime = baseService.getThirdTime("BSIJA_SALE_RETURN_SYNC_TIME");
			Date now = new Date();
			int page = 1;
			int pageSize = 30;
			do {
				wbCuspurchase = purchaseService.syncPmilaWbCuspurchase();
			} while (wbCuspurchase.size() == pageSize);
			baseService.updateThirdTime("BSIJA_SALE_RETURN_SYNC_TIME", now);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	public void syncWbCuspurchaseReturn() {
		try {
			List<PmilaCuspurchaseReturn> wbCuspurchaseReturn;
			String startTime = baseService.getThirdTime("BSIJA_SALE_RETURN_SYNC_TIME");
			Date now = new Date();
			int page = 1;
			int pageSize = 30;
			do {
				wbCuspurchaseReturn = purchaseService.syncWbCuspurchaseReturn();
			} while (wbCuspurchaseReturn.size() == pageSize);
			baseService.updateThirdTime("BSIJA_SALE_RETURN_SYNC_TIME", now);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}


}
