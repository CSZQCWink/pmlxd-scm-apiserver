package com.sungeon.bos.controller.view;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sungeon.bos.core.constants.Constants;
import com.sungeon.bos.core.utils.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据JSON配置自定义功能页面实现手动获取并执行接口内容
 *
 * @author 刘国帅
 * @date 2022-3-2
 **/
@RequestMapping("/custom")
@Controller
public class CustomPageView {

	@RequestMapping("/list")
	public String index(HttpServletRequest req) {
		String filePath = req.getServletContext().getRealPath("/WEB-INF/classes/custom/");
		List<File> files = FileUtils.listFiles(filePath, false);
		files = files.stream().sorted(Comparator.comparing(File::getName)).collect(Collectors.toList());
		JSONObject json;
		JSONArray resp = new JSONArray();
		JSONObject one;
		for (File file : files) {
			if (file.getName().endsWith(".json")) {
				json = JSONObject.parseObject(FileUtils.readFileByChar(file, Constants.CHARSET_UTF8));
				one = new JSONObject();
				one.put("name", json.getString("name"));
				one.put("url", "View/custom/page/" + file.getName().substring(0, file.getName().length() - 5));
				resp.add(one);
			}
		}
		req.setAttribute("resp", resp);
		return "/custom/index";
	}

	@RequestMapping("/page/{page}")
	public String page(@PathVariable("page") String page, HttpServletRequest req) {
		String filePath = req.getServletContext().getRealPath("/WEB-INF/classes/custom/" + page + ".json");
		JSONObject json = JSONObject.parseObject(FileUtils.readFileByChar(FileUtils.getFile(filePath), Constants.CHARSET_UTF8));

		req.setAttribute("json", json);
		return "/custom/page";
	}
}
