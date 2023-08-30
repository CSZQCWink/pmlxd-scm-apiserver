package com.sungeon.bos.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.dao.IBaseDao;
import com.sungeon.bos.entity.*;
import com.sungeon.bos.entity.base.StoreEntity;
import com.sungeon.bos.mapper.IBaseMapper;
import com.sungeon.bos.util.SystemProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Repository("baseDao")
public class BaseDaoImpl implements IBaseDao {

	@Autowired
	private IBaseMapper baseMapper;

	@Override
	public Integer getNewId(String tableName) {
		return baseMapper.getNewId(tableName);
	}

	@Override
	public String getNewDocno(String seqName) {
		return baseMapper.getNewDocno(seqName);
	}

	@Override
	public String getParamValue(String name) {
		return baseMapper.getParamValue(name);
	}

	@Override
	public StoreEntity queryStore(String store) {
		return baseMapper.queryStore(store);
	}

	@Override
	public PayWay queryPayWay(String payWay) {
		return baseMapper.queryPayWay(payWay);
	}

	@Override
	public VipType queryVipType(String vipType) {
		return baseMapper.queryVipType(vipType);
	}

	@Override
	public Integer initSql(String sql) {
		log.info("Initialize SQL:" + sql);
		baseMapper.initSql(sql);
		log.info("Initialize SQL Success");
		return 1;
	}

	@Override
	public Integer initProcedure(String name, String sql) {
		log.info("Initialize Procedure/Function/Trigger:" + name);
		baseMapper.initSql(sql);
		log.info("Initialize Procedure/Function/Trigger Success");
		return 1;
	}

	@Override
	public Integer queryThirdTimeId(String type) {
		return baseMapper.queryThirdTimeId(type);
	}

	@Override
	public String queryThirdTime(String type) {
		return baseMapper.queryThirdTime(type);
	}

	@Override
	public Integer initThirdTime(String type) {
		return baseMapper.initThirdTime(type);
	}

	@Override
	public Integer insertThirdTime(ThirdTime thirdTime) {
		return baseMapper.insertThirdTime(thirdTime);
	}

	@Override
	public Integer updateThirdTime(String type, Date date) {
		return baseMapper.updateThirdTime(type, date);
	}

	@Override
	public List<ScheduleJob> queryScheduleJobs() {
		return baseMapper.queryScheduleJobs(SystemProperties.ScheduleGroup);
	}

	@Override
	public ScheduleJob queryScheduleJob(String jobName, String groupName) {
		return baseMapper.queryScheduleJob(jobName, groupName);
	}

	@Override
	public ScheduleJob queryScheduleJobById(Integer jobId) {
		return baseMapper.queryScheduleJobById(jobId);
	}

	@Override
	public Integer insertScheduleJob(ScheduleJob scheduleJob) {
		return baseMapper.insertScheduleJob(scheduleJob);
	}

	@Override
	public Integer updateScheduleJob(ScheduleJob scheduleJob) {
		return baseMapper.updateScheduleJob(scheduleJob);
	}

	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	@Override
	public Integer insertInterfaceLog(InterfaceLog log) {
		return baseMapper.insertInterfaceLog(log);
	}

	@Override
	public Long queryInterfaceResendId(InterfaceLog log) {
		return baseMapper.queryInterfaceResendId(log);
	}

	@Override
	public Integer insertInterfaceResend(InterfaceLog log) {
		return baseMapper.insertInterfaceResend(log);
	}

	@Override
	public Integer updateInterfaceResend(InterfaceLog log) {
		return baseMapper.updateInterfaceResend(log);
	}

	@Override
	public List<InterfaceResendLog> queryResendInterfaceLogs(Integer count) {
		return baseMapper.queryResendInterfaceLogs(count);
	}

	@Override
	public AccessToken queryAccessToken(String appId) {
		return baseMapper.queryAccessToken(appId);
	}

	@Override
	public Integer insertAccessToken(AccessToken accessToken) {
		return baseMapper.insertAccessToken(accessToken);
	}

	@Override
	public BosResult testProcedure(int id) {
		Map<String, Object> map = new HashMap<String, Object>();
		BosResult result = new BosResult();
		try {
			map.put("id", id);
			baseMapper.testProcedure(map);
			JSONObject res = (JSONObject) JSON.toJSON(map);
			result = JSONObject.parseObject(res.toString(), BosResult.class);
		} catch (Exception e) {
			result.setCode(-1);
			result.setMessage(e.getMessage());
		}
		return result;
	}

}
