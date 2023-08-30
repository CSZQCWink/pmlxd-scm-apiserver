package com.sungeon.bos.controller.view;

import com.sungeon.bos.entity.ScheduleJob;
import com.sungeon.bos.job.QuartzJobManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@RequestMapping("/JobView")
@Controller
public class QuartzJobView {

	@Autowired
	private QuartzJobManager quartzJobManager;
	@Value("${ScheduleGroup}")
	private String scheduleGroup;

	@RequestMapping("/index")
	public String getJobs(Integer page, HttpServletRequest req) {
		dealRequest(page, req);
		return "/job/index";
	}

	private void dealRequest(Integer page, HttpServletRequest req) {
		if (null == page) {
			page = 1;
		}

		int pageAllCount = 20;

		List<ScheduleJob> jobList = quartzJobManager.getJobs();

		int jobSize = jobList.size();
		int pageSize = (jobSize % pageAllCount) == 0 ? (jobSize / pageAllCount) : (jobSize / pageAllCount + 1);
		String countDesc = "";
		if (jobSize <= pageAllCount) {
			countDesc = "1-" + jobSize + "  /  " + jobSize;
			req.setAttribute("jobs", jobList);
		} else if (page >= pageSize) {
			countDesc = ((pageSize - 1) * pageAllCount + 1) + "-" + jobSize + "  /  " + jobSize;
			req.setAttribute("jobs", jobList.subList((pageSize - 1) * pageAllCount, jobSize));
		} else {
			countDesc = ((page - 1) * pageAllCount + 1) + "-" + (page * pageAllCount) + "  /  " + jobSize;
			req.setAttribute("jobs", jobList.subList((page - 1) * pageAllCount, page * pageAllCount));
		}

		req.setAttribute("page", page);
		if (page == 1) {
			req.setAttribute("lastPage", page);
		} else {
			req.setAttribute("lastPage", page - 1);
		}
		if (page == pageSize) {
			req.setAttribute("nextPage", page);
		} else {
			req.setAttribute("nextPage", page + 1);
		}
		req.setAttribute("countDesc", countDesc);
		req.setAttribute("pageSize", pageSize);
		req.setAttribute("status", true);
		req.setAttribute("scheduleGroup", scheduleGroup);
	}

}
