package com.sungeon.bos.task;

import com.sungeon.bos.entity.base.RetailEntity;
import com.sungeon.bos.service.IRetailService;
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
public class RetailTask extends BaseTask {

	@Autowired
	private IRetailService retailService;

	public void syncBsijaRetail() {
		try {
			List<RetailEntity> retails;
			int page = 1;
			do {
				retails = retailService.syncBsijaRetail(null, page++, SystemProperties.ParamDataCount);
			} while (!CollectionUtils.isEmpty(retails));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
