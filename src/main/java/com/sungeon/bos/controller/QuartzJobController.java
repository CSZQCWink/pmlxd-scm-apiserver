package com.sungeon.bos.controller;

import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.core.utils.DateTimeUtils;
import com.sungeon.bos.entity.ScheduleJob;
import com.sungeon.bos.job.QuartzJobManager;
import com.sungeon.bos.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @title: 定时任务Controller
 * @author: 陈苏洲
 * @date: 2023/8/30 9:34
 **/
@Slf4j
@RequestMapping("/Job")
@Controller
public class QuartzJobController {

	@Autowired
	private IBaseService baseService;
	@Autowired
	private QuartzJobManager quartzJobManager;

	@RequestMapping("/getJobs")
	@ResponseBody
	public List<ScheduleJob> getJobs() {
		List<ScheduleJob> jobList = quartzJobManager.getJobs();
		log.info("获取所有任务 " + jobList);
		return jobList;
	}

	@RequestMapping("/get")
	@ResponseBody
	public ScheduleJob get(String jobName, String groupName, HttpServletRequest req) {
		ScheduleJob job = quartzJobManager.getJob(jobName, groupName);
		log.info("获取当前任务 " + job);
		return job;
	}

	@RequestMapping("/getNextTime")
	@ResponseBody
	public JSONObject getNextTime(Integer jobId) {
		JSONObject result = new JSONObject();
		ScheduleJob scheduleJob = baseService.getScheduleJobById(jobId);
		ScheduleJob job = quartzJobManager.getJob(scheduleJob.getJobName(), scheduleJob.getGroupName());
		Date nextTime = quartzJobManager.getNextFireTime(job);
		result.fluentPut("status", true);
		result.fluentPut("statusDesc", scheduleJob.getJobName() + "下一次执行时间：" + DateTimeUtils.print(nextTime));
		return result;
	}

	@RequestMapping(value = "/pause", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject pause(Integer jobId) {
		JSONObject result = new JSONObject();
		ScheduleJob scheduleJob = baseService.getScheduleJobById(jobId);
		if (null == scheduleJob) {
			return null;
		}

		log.info("暂停任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]");
		boolean success = quartzJobManager.pauseJob(scheduleJob);
		scheduleJob = quartzJobManager.getJob(scheduleJob.getJobName(), scheduleJob.getGroupName());

		result.fluentPut("status", success);
		if (success) {
			result.fluentPut("statusDesc",
					"暂停任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]成功");
		} else {
			result.fluentPut("statusDesc",
					"暂停任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]失败");
		}
		result.fluentPut("loadType", "SINGLE");
		result.fluentPut("job", scheduleJob);
		return result;
	}

	@RequestMapping(value = "/pauseAll", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject pauseAll() {
		JSONObject result = new JSONObject();
		boolean success = quartzJobManager.pauseAllJob();

		result.fluentPut("status", success);
		if (success) {
			result.fluentPut("statusDesc", "暂停所有任务成功");
		} else {
			result.fluentPut("statusDesc", "暂停所有任务失败");
		}
		return result;
	}

	@RequestMapping(value = "/interrupt", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject interrupt(Integer jobId) {
		JSONObject result = new JSONObject();
		ScheduleJob scheduleJob = baseService.getScheduleJobById(jobId);
		if (null == scheduleJob) {
			return null;
		}

		log.info("中断任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]");
		boolean success = quartzJobManager.interruptJob(scheduleJob);
		scheduleJob = quartzJobManager.getJob(scheduleJob.getJobName(), scheduleJob.getGroupName());

		result.fluentPut("status", success);
		if (success) {
			result.fluentPut("statusDesc",
					"中断任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]成功");
		} else {
			result.fluentPut("statusDesc",
					"中断任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]失败");
		}
		result.fluentPut("loadType", "SINGLE");
		result.fluentPut("job", scheduleJob);
		return result;
	}

	@RequestMapping(value = "/resume", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject resume(Integer jobId) {
		JSONObject result = new JSONObject();
		ScheduleJob scheduleJob = baseService.getScheduleJobById(jobId);
		if (null == scheduleJob) {
			return null;
		}

		log.info("恢复任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]");
		boolean success = quartzJobManager.resumeJob(scheduleJob);
		scheduleJob = quartzJobManager.getJob(scheduleJob.getJobName(), scheduleJob.getGroupName());

		result.fluentPut("status", success);
		if (success) {
			result.fluentPut("statusDesc",
					"恢复任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]成功");
		} else {
			result.fluentPut("statusDesc",
					"恢复任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]失败");
		}
		result.fluentPut("loadType", "SINGLE");
		result.fluentPut("job", scheduleJob);
		return result;
	}

	@RequestMapping(value = "/resumeAll", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject resumeAll() {
		JSONObject result = new JSONObject();
		boolean success = quartzJobManager.resumeAllJob();

		result.fluentPut("status", success);
		if (success) {
			result.fluentPut("statusDesc", "恢复所有任务成功");
		} else {
			result.fluentPut("statusDesc", "恢复所有任务失败");
		}
		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject delete(Integer jobId) {
		JSONObject result = new JSONObject();
		ScheduleJob scheduleJob = baseService.getScheduleJobById(jobId);
		if (null == scheduleJob) {
			return null;
		}

		log.info("删除任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]");
		scheduleJob.setStatus("N");
		boolean success = quartzJobManager.deleteJob(scheduleJob);
		if (success) {
			baseService.updateScheduleJob(scheduleJob);
		}

		result.fluentPut("status", success);
		result.fluentPut("statusDesc", "删除任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]成功");
		return result;
	}

	@RequestMapping(value = "/trigger", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject trigger(Integer jobId) {
		JSONObject result = new JSONObject();
		ScheduleJob scheduleJob = baseService.getScheduleJobById(jobId);
		if (null == scheduleJob) {
			return null;
		}

		log.info("立即执行任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]");
		boolean success = quartzJobManager.triggerJob(scheduleJob);
		scheduleJob = quartzJobManager.getJob(scheduleJob.getJobName(), scheduleJob.getGroupName());

		result.fluentPut("status", success);
		if (success) {
			result.fluentPut("statusDesc",
					"立即执行任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]成功");
		} else {
			result.fluentPut("statusDesc",
					"立即执行任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]失败");
		}
		result.fluentPut("loadType", "SINGLE");
		result.fluentPut("job", scheduleJob);
		return result;
	}

	@RequestMapping("/update")
	@ResponseBody
	public JSONObject update(Integer jobId, String description, String cronExpression) {
		JSONObject result = new JSONObject();
		ScheduleJob scheduleJob = baseService.getScheduleJobById(jobId);
		if (null == scheduleJob) {
			return null;
		}

		log.info("修改任务 前[" + scheduleJob + "]");
		scheduleJob.setCronExpression(cronExpression);
		scheduleJob.setDescription(description);
		log.info("修改任务 后[" + scheduleJob + "]");
		boolean success = quartzJobManager.rescheduleJob(scheduleJob);
		scheduleJob = quartzJobManager.getJob(scheduleJob.getJobName(), scheduleJob.getGroupName());

		result.fluentPut("status", success);
		if (success) {
			scheduleJob.setDescription(description);
			baseService.updateScheduleJob(scheduleJob);
			result.fluentPut("statusDesc",
					"修改任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]成功");
		} else {
			result.fluentPut("statusDesc",
					"修改任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]失败");
		}
		result.fluentPut("loadType", "SINGLE");
		result.fluentPut("job", scheduleJob);
		return result;
	}

	@RequestMapping("/add")
	@ResponseBody
	public JSONObject add(String jobName, String description, String cronExpression, String groupName) {
		JSONObject result = new JSONObject();
		ScheduleJob scheduleJob = new ScheduleJob();
		scheduleJob.setJobName(jobName);
		scheduleJob.setDescription(description);
		scheduleJob.setCronExpression(cronExpression);
		scheduleJob.setGroupName(groupName);
		baseService.addScheduleJob(scheduleJob);

		boolean success = quartzJobManager.scheduleJob(scheduleJob);

		result.fluentPut("status", success);
		result.fluentPut("statusDesc", "新增任务 [" + scheduleJob.getGroupName() + "." + scheduleJob.getJobName() + "]成功");
		result.fluentPut("job", scheduleJob);
		return result;
	}

}
