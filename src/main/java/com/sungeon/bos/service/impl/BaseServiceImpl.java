package com.sungeon.bos.service.impl;

import com.sungeon.bos.core.constants.ValueCode;
import com.sungeon.bos.core.exception.AuthFailException;
import com.sungeon.bos.core.utils.FileUtils;
import com.sungeon.bos.dao.IBaseDao;
import com.sungeon.bos.entity.*;
import com.sungeon.bos.entity.base.StoreEntity;
import com.sungeon.bos.service.IBaseService;
import com.sungeon.bos.util.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author : 刘国帅
 * @date : 2016-12-6
 */
@Service("baseService")
public class BaseServiceImpl implements IBaseService {

	@Autowired
	private IBaseDao baseDao;

	@Override
	public Integer getNewId(String tableName) {
		return baseDao.getNewId(tableName);
	}

	@Override
	public String getNewDocno(String seqName) {
		return baseDao.getNewDocno(seqName);
	}

	@Override
	public String getParamValue(String name) {
		return baseDao.getParamValue(name);
	}

	@Override
	public StoreEntity getStore(String store) {
		return baseDao.queryStore(store);
	}

	@Override
	public PayWay getPayWay(String payWay) {
		return baseDao.queryPayWay(payWay);
	}

	@Override
	public VipType getVipType(String vipType) {
		return baseDao.queryVipType(vipType);
	}

	@Override
	public Integer initSql(List<String> sqls) {
		for (String sql : sqls) {
			baseDao.initSql(sql);
		}
		return sqls.size();
	}

	@Override
	public Integer initProcedure(List<File> files) {
		for (File file : files) {
			baseDao.initProcedure(file.getName(), FileUtils.readFileByChar(file, "gbk"));
		}
		return files.size();
	}

	@Override
	public Integer getThirdTimeId(String type) {
		return baseDao.queryThirdTimeId(type);
	}

	@Override
	public String getThirdTime(String type) {
		return baseDao.queryThirdTime(type);
	}

	@Override
	public Integer initThirdTime(String type) {
		return baseDao.initThirdTime(type);
	}

	@Override
	public Integer insertThirdTime(ThirdTime thirdTime) {
		return baseDao.insertThirdTime(thirdTime);
	}

	@Override
	public Integer updateThirdTime(String type, Date date) {
		return baseDao.updateThirdTime(type, date);
	}

	@Override
	public List<ScheduleJob> getScheduleJobs() {
		return baseDao.queryScheduleJobs();
	}

	@Override
	public ScheduleJob getScheduleJob(String jobName, String groupName) {
		return baseDao.queryScheduleJob(jobName, groupName);
	}

	@Override
	public ScheduleJob getScheduleJobById(Integer jobId) {
		return baseDao.queryScheduleJobById(jobId);
	}

	@Override
	public Integer addScheduleJob(ScheduleJob scheduleJob) {
		return baseDao.insertScheduleJob(scheduleJob);
	}

	@Override
	public Integer updateScheduleJob(ScheduleJob scheduleJob) {
		return baseDao.updateScheduleJob(scheduleJob);
	}

	@Override
	public Integer addInterfaceLog(InterfaceLog log) {
		baseDao.insertInterfaceLog(log);
		if (log.getType().equals(InterfaceType.SEND)) {
			Long resendId = baseDao.queryInterfaceResendId(log);
			if (null == resendId) {
				baseDao.insertInterfaceResend(log);
			} else {
				log.setId(resendId);
				baseDao.updateInterfaceResend(log);
			}
		}
		return 1;
	}

	@Override
	public AccessToken getAccessToken(String appId) {
		return baseDao.queryAccessToken(appId);
	}

	@Override
	public Integer addAccessToken(AccessToken accessToken) {
		return baseDao.insertAccessToken(accessToken);
	}

	@Override
	public boolean verifyAccessToken(String appId, String accessToken) {
		AccessToken token = baseDao.queryAccessToken(appId);
		if (null == token) {
			throw new AuthFailException(ValueCode.AUTH_ACCESS_TOKEN_ERROR, "本应用appId[" + appId + "]不存在Access-Token，请先获取");
		}
		if (token.getAccessToken().equals(accessToken)) {
			if (token.getDateEnd().getTime() < System.currentTimeMillis()) {
				throw new AuthFailException(ValueCode.AUTH_ACCESS_TOKEN_EXPIRE, "Access-Token已过期，请重新获取");
			}
			return true;
		} else {
			throw new AuthFailException(ValueCode.AUTH_ACCESS_TOKEN_ERROR, "Access-Token错误");
		}
	}

	@Override
	public BosResult testProcedure(int id) {
		return baseDao.testProcedure(id);
	}

	@Autowired
	private TransactionHandler transactionHandler;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void testSw() {
		// 执行update
		Integer i = baseDao.updateThirdTime("WMSProductSyncTime", new Date());
		// 1.调用内部私有方法：同一事务
		// transactionHandler.transactional(this::testSw2);
		// transactionHandler.transactional(TransactionDefinition.PROPAGATION_REQUIRED, this::testSw2);
		// 2.调用内部私有方法：独立事务
		transactionHandler.transactional(TransactionDefinition.PROPAGATION_REQUIRES_NEW, this::testSw2);
		// 抛出除数为0异常
		System.out.println(i / 0);
	}

	// 内部私有方法
	private void testSw2() {
		// 执行update
		Integer i = baseDao.updateThirdTime("WMSSupplierSyncTime", new Date());
		// 抛出除数为0异常
		// System.out.println(i / 0);
	}

}
