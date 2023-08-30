package com.sungeon.bos.mapper;

import com.sungeon.bos.entity.*;
import com.sungeon.bos.entity.base.StoreEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Mapper
public interface IBaseMapper {

	Integer getNewId(String tableName);

	String getNewDocno(String seqName);

	String getParamValue(String name);

	StoreEntity queryStore(@Param("store") String store);

	PayWay queryPayWay(String payWay);

	VipType queryVipType(String vipType);

	Integer initSql(@Param("sql") String sql);

	Integer queryThirdTimeId(@Param("type") String type);

	String queryThirdTime(@Param("type") String type);

	Integer initThirdTime(@Param("type") String type);

	Integer insertThirdTime(ThirdTime thirdTime);

	Integer updateThirdTime(@Param("type") String type, @Param("date") Date date);

	List<ScheduleJob> queryScheduleJobs(@Param("groupName") String groupName);

	ScheduleJob queryScheduleJob(@Param("jobName") String jobName, @Param("groupName") String groupName);

	ScheduleJob queryScheduleJobById(@Param("jobId") Integer jobId);

	Integer insertScheduleJob(ScheduleJob scheduleJob);

	Integer updateScheduleJob(@Param("job") ScheduleJob scheduleJob);

	Integer insertInterfaceLog(InterfaceLog log);

	Long queryInterfaceResendId(InterfaceLog log);

	Integer insertInterfaceResend(InterfaceLog log);

	Integer updateInterfaceResend(InterfaceLog log);

	List<InterfaceResendLog> queryResendInterfaceLogs(Integer count);

	AccessToken queryAccessToken(String appId);

	Integer insertAccessToken(AccessToken accessToken);

	Map<String, Object> testProcedure(Map<String, Object> map);

}
