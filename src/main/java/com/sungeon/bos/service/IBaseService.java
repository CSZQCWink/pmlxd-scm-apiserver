package com.sungeon.bos.service;

import com.sungeon.bos.entity.*;
import com.sungeon.bos.entity.base.StoreEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Service
public interface IBaseService {

	Integer getNewId(String tableName);

	String getNewDocno(String seqName);

	String getParamValue(String name);

	StoreEntity getStore(String store);

	PayWay getPayWay(String payway);

	VipType getVipType(String viptype);

	Integer initSql(List<String> sqls);

	Integer initProcedure(List<File> files);

	Integer getThirdTimeId(String type);

	String getThirdTime(String type);

	Integer initThirdTime(String type);

	Integer insertThirdTime(ThirdTime thirdTime);

	Integer updateThirdTime(String type, Date date);

	List<ScheduleJob> getScheduleJobs();

	ScheduleJob getScheduleJob(String jobName, String groupName);

	ScheduleJob getScheduleJobById(Integer jobId);

	Integer addScheduleJob(ScheduleJob scheduleJob);

	Integer updateScheduleJob(ScheduleJob scheduleJob);

	Integer addInterfaceLog(InterfaceLog log);

	AccessToken getAccessToken(String appId);

	Integer addAccessToken(AccessToken accessToken);

	boolean verifyAccessToken(String appId, String accessToken);

	BosResult testProcedure(int id);

	void testSw();

}
