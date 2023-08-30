package com.sungeon.bos.dao;

import com.sungeon.bos.entity.*;
import com.sungeon.bos.entity.base.StoreEntity;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Repository
public interface IBaseDao {

	Integer getNewId(String tableName);

	String getNewDocno(String seqName);

	String getParamValue(String name);

	StoreEntity queryStore(String store);

	PayWay queryPayWay(String payWay);

	VipType queryVipType(String vipType);

	Integer initSql(String sql);

	Integer initProcedure(String name, String sql);

	Integer queryThirdTimeId(String type);

	String queryThirdTime(String type);

	Integer initThirdTime(String type);

	Integer insertThirdTime(ThirdTime thirdTime);

	Integer updateThirdTime(String type, Date date);

	List<ScheduleJob> queryScheduleJobs();

	ScheduleJob queryScheduleJob(String jobName, String groupName);

	ScheduleJob queryScheduleJobById(Integer jobId);

	Integer insertScheduleJob(ScheduleJob scheduleJob);

	Integer updateScheduleJob(ScheduleJob scheduleJob);

	Integer insertInterfaceLog(InterfaceLog log);

	Long queryInterfaceResendId(InterfaceLog log);

	Integer insertInterfaceResend(InterfaceLog log);

	Integer updateInterfaceResend(InterfaceLog log);

	List<InterfaceResendLog> queryResendInterfaceLogs(Integer count);

	AccessToken queryAccessToken(String appId);

	Integer insertAccessToken(AccessToken accessToken);

	BosResult testProcedure(int id);

}
