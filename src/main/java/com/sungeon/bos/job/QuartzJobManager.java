package com.sungeon.bos.job;

import com.sungeon.bos.entity.ScheduleJob;
import com.sungeon.bos.service.IBaseService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.Trigger.TriggerState;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Slf4j
@Component("quartzJobManager")
public class QuartzJobManager {

	@Autowired
	private IBaseService baseService;
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	/**
	 * 初始化任务列表
	 */
	public void initScheduleJobs() {
		try {
			// 这里获取任务信息数据
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			List<ScheduleJob> jobList = baseService.getScheduleJobs();
			TriggerKey triggerKey;
			CronTrigger trigger;
			JobDetail jobDetail;
			CronScheduleBuilder scheduleBuilder;
			for (ScheduleJob scheduleJob : jobList) {
				triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getGroupName());
				// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
				trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
				// 不存在，创建一个
				if (null == trigger) {
					jobDetail = JobBuilder.newJob(QuartzJob.class)
							.withIdentity(scheduleJob.getJobName(), scheduleJob.getGroupName()).build();
					jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
					// 表达式调度构建器
					scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
					// 按新的cronExpression表达式构建一个新的trigger
					trigger = TriggerBuilder.newTrigger()
							.withIdentity(scheduleJob.getJobName(), scheduleJob.getGroupName())
							.withSchedule(scheduleBuilder).build();
					if ("Y".equals(scheduleJob.getStatus())) {
						scheduler.scheduleJob(jobDetail, trigger);
					}
				} else {
					// Trigger已存在，那么更新相应的定时设置
					// 表达式调度构建器
					scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
					// 按新的cronExpression表达式重新构建trigger
					trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder)
							.build();
					// 按新的trigger重新设置job执行
					if ("Y".equals(scheduleJob.getStatus())) {
						scheduler.rescheduleJob(triggerKey, trigger);
					}
				}
			}
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * <b>计划中的任务</b><br/>
	 * 指那些已经添加到quartz调度器的任务，因为quartz并没有直接提供这样的查询接口，所以我们需要结合JobKey和Trigger来实现
	 *
	 * @return scheduleJobs
	 */
	public List<ScheduleJob> getJobs() {
		List<ScheduleJob> jobList = new ArrayList<>();
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			ScheduleJob scheduleJob;
			TriggerState triggerState;
			CronTrigger cronTrigger;
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			for (JobKey jobKey : jobKeys) {
				List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
				scheduleJob = baseService.getScheduleJob(jobKey.getName(), jobKey.getGroup());
				for (Trigger trigger : triggers) {
					triggerState = scheduler.getTriggerState(trigger.getKey());
					scheduleJob.setRunStatus(triggerState.name());
					if (trigger instanceof CronTrigger) {
						cronTrigger = (CronTrigger) trigger;
						scheduleJob.setCronExpression(cronTrigger.getCronExpression());
					}
					jobList.add(scheduleJob);
				}
			}
			jobList.sort(Comparator.comparing(ScheduleJob::getId));
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
		return jobList;
	}

	/**
	 * <b>根据任务名称、分组名称查找任务</b><br/>
	 *
	 * @param jobName   任务名
	 * @param groupName 所属分组
	 * @return scheduleJob
	 */
	public ScheduleJob getJob(String jobName, String groupName) {
		ScheduleJob scheduleJob = null;
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerState triggerState;
			CronTrigger cronTrigger;
			JobKey jobKey = JobKey.jobKey(jobName, groupName);
			List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
			for (Trigger trigger : triggers) {
				scheduleJob = baseService.getScheduleJob(jobKey.getName(), jobKey.getGroup());
				triggerState = scheduler.getTriggerState(trigger.getKey());

				scheduleJob.setRunStatus(triggerState.name());
				if (trigger instanceof CronTrigger) {
					cronTrigger = (CronTrigger) trigger;
					scheduleJob.setCronExpression(cronTrigger.getCronExpression());
				}
			}
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
		return scheduleJob;
	}

	/**
	 * <b>获取运行中的任务</b><br/>
	 *
	 * @return scheduleJobs
	 */
	public List<ScheduleJob> getCurrentlyExecutingJobs() {
		List<ScheduleJob> jobList = new ArrayList<>();
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			ScheduleJob job;
			JobKey jobKey;
			Trigger trigger;
			TriggerState triggerState;
			CronTrigger cronTrigger;
			List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
			for (JobExecutionContext executingJob : executingJobs) {
				jobKey = executingJob.getJobDetail().getKey();
				job = baseService.getScheduleJob(jobKey.getName(), jobKey.getGroup());
				trigger = executingJob.getTrigger();
				triggerState = scheduler.getTriggerState(trigger.getKey());
				job.setRunStatus(triggerState.name());
				if (trigger instanceof CronTrigger) {
					cronTrigger = (CronTrigger) trigger;
					job.setCronExpression(cronTrigger.getCronExpression());
				}
				jobList.add(job);
			}
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
		}
		return jobList;
	}

	/**
	 * <b>开启任务</b><br/>
	 *
	 * @param scheduleJob scheduleJob
	 */
	public boolean scheduleJob(ScheduleJob scheduleJob) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
					.withIdentity(scheduleJob.getJobName(), scheduleJob.getGroupName()).build();
			jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
			// 按新的cronExpression表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(scheduleJob.getJobName(), scheduleJob.getGroupName()).withSchedule(scheduleBuilder)
					.build();

			// scheduler.addJob(jobDetail, true);
			scheduler.scheduleJob(jobDetail, trigger);
			return true;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <b>重载任务</b><br/>
	 *
	 * @param scheduleJob scheduleJob
	 */
	public boolean rescheduleJob(ScheduleJob scheduleJob) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getGroupName());

			// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
			return true;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <b>取消任务</b><br/>
	 *
	 * @param scheduleJob scheduleJob
	 */
	public boolean unScheduleJob(ScheduleJob scheduleJob) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getGroupName());
			scheduler.unscheduleJob(triggerKey);
			return true;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <b>获取下一次执行时间</b><br/>
	 *
	 * @param scheduleJob scheduleJob
	 * @return java.util.Date
	 */
	public Date getNextFireTime(ScheduleJob scheduleJob) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getGroupName());
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			return trigger.getNextFireTime();
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * <b>暂停任务</b><br/>
	 *
	 * @param scheduleJob scheduleJob
	 * @return boolean
	 */
	public boolean pauseJob(ScheduleJob scheduleJob) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getGroupName());
			scheduler.pauseJob(jobKey);
			return true;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <b>暂停所有任务</b><br/>
	 *
	 * @return boolean
	 */
	public boolean pauseAllJob() {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			scheduler.pauseAll();
			return true;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <b>中断任务</b><br/>
	 *
	 * @return boolean
	 */
	public boolean interruptJob(ScheduleJob scheduleJob) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getGroupName());
			scheduler.interrupt(jobKey);
			return true;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <b>恢复任务</b><br/>
	 * 和暂停任务相对
	 *
	 * @param scheduleJob scheduleJob
	 * @return boolean
	 */
	public boolean resumeJob(ScheduleJob scheduleJob) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getGroupName());
			scheduler.resumeJob(jobKey);
			return true;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <b>恢复所有任务</b><br/>
	 * 和暂停任务相对
	 *
	 * @return boolean
	 */
	public boolean resumeAllJob() {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			scheduler.resumeAll();
			return true;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <b>删除任务</b><br/>
	 * 删除任务后，所对应的trigger也将被删除
	 *
	 * @param scheduleJob scheduleJob
	 * @return boolean
	 */
	public boolean deleteJob(ScheduleJob scheduleJob) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getGroupName());
			scheduler.deleteJob(jobKey);
			return true;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	/**
	 * <b>立即运行任务</b><br/>
	 * 这里的立即运行，只会运行一次，方便测试时用。quartz是通过临时生成一个trigger的方式来实现的，这个trigger将在本次任务运行完成之后自动删除。trigger的key是随机生成的
	 *
	 * @param scheduleJob scheduleJob
	 * @return boolean
	 */
	public boolean triggerJob(ScheduleJob scheduleJob) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getGroupName());
			scheduler.triggerJob(jobKey);
			return true;
		} catch (SchedulerException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

}
