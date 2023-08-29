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
        // 跳转到服务器内部的一个功能处理方法
        // return new ModelAndView("forward:/job/index.jsp");
        // 重定向一个功能方法
        // return new ModelAndView("redirect:/dispather/b");
        // 跳转到服务器内部的一个页面
        return "home";
    }

}
