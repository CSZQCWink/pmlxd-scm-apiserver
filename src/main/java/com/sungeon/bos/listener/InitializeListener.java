package com.sungeon.bos.listener;

import com.sungeon.bos.core.constants.Constants;
import com.sungeon.bos.core.utils.FileUtils;
import com.sungeon.bos.job.QuartzJobManager;
import com.sungeon.bos.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Component
public class InitializeListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private IBaseService baseService;
	@Autowired
	private QuartzJobManager quartzJobManager;
	@Value("${Param.InitSQL.Status}")
	private String isInitSql;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent evt) {
		// 防止重复执行。
		if (evt.getApplicationContext().getParent() == null) {
			log.info("******************接口程序初始化开始******************");
			// 初始化任务列表
			quartzJobManager.initScheduleJobs();

			if (Constants.BURGEON_YES.equals(isInitSql)) {
				// 初始化SQL
				baseService.initSql(loadInitSql());
				// 初始化存储过程、方法、触发器
				baseService.initProcedure(loadInitProcedure());
				// 初始化推送时间
				initThirdTime();
			}
			log.info("******************接口程序初始化完成******************");
		}
	}

	private void initThirdTime() {
		Integer timeId = baseService.getThirdTimeId("StoragePropelTime");
		if (null == timeId) {
			timeId = baseService.initThirdTime("StoragePropelTime");
			if (null != timeId && timeId > 0) {
				log.info("****初始化库存推送时间成功（仅在第一次部署时运行）****");
			}
		}
		timeId = baseService.getThirdTimeId("StoreStoragePropelTime");
		if (null == timeId) {
			timeId = baseService.initThirdTime("StoreStoragePropelTime");
			if (null != timeId && timeId > 0) {
				log.info("***初始化门店库存推送时间成功（仅在第一次部署时运行）***");
			}
		}
		timeId = baseService.getThirdTimeId("ProductPropelTime");
		if (null == timeId) {
			timeId = baseService.initThirdTime("ProductPropelTime");
			if (null != timeId && timeId > 0) {
				log.info("***初始化商品推送时间成功（仅在第一次部署时运行）***");
			}
		}
	}

	private List<String> loadInitSql() {
		List<String> sql = new ArrayList<>();
		String path = InitializeListener.class.getResource("/").getPath().substring(1) + "sql/";
		List<File> files = FileUtils.listFiles(path);
		for (File file : files) {
			if (file.getName().endsWith(".sql")) {
				String content = FileUtils.readFileByChar(file, "gbk");
				String[] sqlArray = content.split("(;\\s*\\r\\n)|(;\\s*\\n)");
				sql.addAll(Arrays.asList(sqlArray));
			}
		}
		return sql;
	}

	private List<File> loadInitProcedure() {
		List<File> files = new ArrayList<>();
		String path = InitializeListener.class.getResource("/").getPath().substring(1) + "sql/";
		List<File> allFiles = FileUtils.listFiles(path);
		for (File file : allFiles) {
			if (file.isDirectory()) {
				continue;
			}

			if (file.getName().endsWith(".prc")) {
				files.add(file);
			} else if (file.getName().endsWith(".fnc")) {
				files.add(file);
			} else if (file.getName().endsWith(".trg")) {
				files.add(file);
			}
		}
		return files;
	}

}
