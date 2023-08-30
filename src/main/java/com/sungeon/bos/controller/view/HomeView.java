package com.sungeon.bos.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 刘国帅
 * @date 2019-10-9
 **/
@Controller
public class HomeView {

	@RequestMapping("/index")
	public String getLogFiles(Integer page) {
		return "home";
	}

}
