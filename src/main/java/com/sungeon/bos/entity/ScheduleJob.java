package com.sungeon.bos.entity;

import org.apache.ibatis.type.Alias;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Alias("ScheduleJob")
public class ScheduleJob implements Serializable {


	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private Integer id;
	/**
	 * 任务名称
	 */
	private String jobName;
	/**
	 * 任务分组
	 */
	private String groupName;
	/**
	 * 任务状态
	 * <li>N：禁用</li>
	 * <li>Y：启用</li>
	 */
	private String status;
	/**
	 * 任务运行状态
	 * <li>NONE：无</li>
	 * <li>NORMAL：正常状态</li>
	 * <li>PAUSED：暂停状态</li>
	 * <li>COMPLETE：完成</li>
	 * <li>ERROR：错误</li>
	 * <li>BLOCKED：堵塞</li>
	 */
	private String runStatus;
	/**
	 * 任务运行时间表达式
	 */
	private String cronExpression;
	/**
	 * 任务描述
	 */
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

}
