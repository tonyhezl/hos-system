package com.cdroho.controller;

import com.cdroho.logicalrepository.UserRespository;
import com.cdroho.modle.User;
import com.cdroho.utils.PasswordHelper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * 登陆控制器
 * @author HZL
 * @date 2020-4-7
 */
@RestController
@RequestMapping("testone")
public class HomeController {
    @Autowired
    private UserRespository userService;
    @Autowired
    private PasswordHelper passwordHelper;
    public static Map<String,Object> resultMap=new HashMap();

    @GetMapping("login")
    public Object login() {
        return "Here is Login page";
    }

    @GetMapping("unauthc")
    public Object unauthc() {
        return "Here is Unauthc page";
    }

    @PostMapping("doLogin")
    public Map doLogin(@RequestBody Map<String,String > param) {
        Map<String,Object> resultMap=new HashMap();
        String usename=param.get("usename");
        String password=param.get("password");
        UsernamePasswordToken token = new UsernamePasswordToken(usename, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (IncorrectCredentialsException ice) {
           // return "password error!";
        } catch (UnknownAccountException uae) {
            //return "username error!";
        }

        User user = userService.findUserByName(usename);
        subject.getSession().setAttribute("user", user);
        resultMap.put("msg","success");
        return resultMap;
    }

    @GetMapping("register")
    public Object register(@RequestParam String username, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        passwordHelper.encryptPassword(user);

        userService.save(user);
        return "SUCCESS";
    }

    /**
     * 认证失败返回的标识
     * 无权限返回的标识
     * @return
     */
    @GetMapping("failed")
    public Map failed() {
        resultMap.put("code","refused");
        return resultMap;
    }
    /**
     * 认证失败返回的标识
     * 没登陆/登陆失效返回的标识
     * @return
     */
    @GetMapping("loinFailed")
    public Map loinFailed() {
        resultMap.put("code","login");
        return resultMap;
    }
}
