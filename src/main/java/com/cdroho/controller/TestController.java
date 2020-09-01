package com.cdroho.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author HZL
 *	想返回json字符串就用@RestController
 *	想返回页面就用@Controller
 *	原因：@RestController注解相当于@ResponseBody和@Controller的结合
 *	使用控制器跳转页面还需要在application.properties里配置访问前缀和后缀
 *  {spring.mvc.view.prefix=/view/
 *   spring.mvc.view.suffix=.html}
 *	另外一种方式：在src/main/resources目录下新建public文件夹，将静态页面放入里面；
 *  再在application.properties进行配置，并导入thymeleaf依赖（重点：controller里面
 *  必须加上静态页面的后缀，猜想原因是配置文件里没有指明后缀则不添加就导致找不到）\
 *  websocket相关的js，必须被页面引入，然后请求页面也就打开了websocket连接
 */
@Controller
@RequestMapping("/aaa")
public class TestController{


    /**
     * websocket测试界面
     * @return
     */
    @RequestMapping("queryFor")
    public String queryFor() {
        return "/websocket.html";
    }



    @RequestMapping("queryOne")
    public String queryOne() {
        return "/1.html";
    }

    @RequestMapping("querySmall")
    public String querySmall() {
        return "/index.html";
    }

    @RequestMapping("queryBig")
    public String queryBig() {
        return "/zhxs.html";
    }

    @RequestMapping("queryIndex")
    public String queryIndex() {
        return "/index_blue_pager.html";
    }

    @RequestMapping("machinelogin")
    public String machineLogin() {
        return "/login.html";
    }

    @RequestMapping("machinedetail")
    public String machinedetail(@RequestParam(value = "longId") String longId,
                                @RequestParam(value = "queueId") long queueId ) {
        return "/calling.html";
    }

    @RequestMapping("windowLogin")
    public String windowLogin() {
        return "/windowlogin.html";
    }

    @RequestMapping("windowdetail")
    public String windowdetail(@RequestParam(value = "flag") int flag,@RequestParam(value = "loginCode") long loginCode
                ,@RequestParam(value = "loginType") long loginType) {
        return "/window.html";
    }

    @RequestMapping("queryPha")
    public String queryPha() {
        return "/pha.html";
    }

    @RequestMapping("queryC")
    public String queryC() {
        return "/chouxue.html";
    }

    @RequestMapping("report")
    public String queryReport() {
        return "/report.html";
    }

    @RequestMapping("reporthelath")
    public String queryReportHelath() {
        return "/reportHelath.html";
    }

    @RequestMapping("reportCx")
    public String queryReportCx() {
        return "/reportCx.html";
    }

    @RequestMapping("inspect")
    public String queryInspect() {
        return "/inspect.html";
    }

    @RequestMapping("queryf")
    public String queryYf() {
        return "/yf.html";
    }

}
