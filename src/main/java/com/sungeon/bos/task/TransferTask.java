package com.sungeon.bos.task;

import com.sungeon.bos.entity.base.TransferEntity;
import com.sungeon.bos.service.ITransferService;
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
public class TransferTask extends BaseTask {

	@Autowired
	private ITransferService transferService;

	public void syncBsijaTransfer() {
		try {
			List<TransferEntity> transfers;
			int page = 1;
			do {
				transfers = transferService.syncBsijaTransfer(null, page++, SystemProperties.ParamDataCount);
			} while (!CollectionUtils.isEmpty(transfers));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
