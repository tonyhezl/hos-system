package com.cdroho.controller;


import com.cdroho.modle.dto.LoginDto;
import com.cdroho.modle.dto.UserInfo;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
public class LoginController {

    public static Map<String,Object> resultMap=new HashMap();

    @PostMapping("login")
    public Map doLogin(@RequestBody LoginDto loginDto){
        if(loginDto.getUsename().equals("admin")&&loginDto.getPassword().equals("123456")){
            resultMap.put("code","success");
        }else if(loginDto.getUsename().equals("nurse")){
            resultMap.put("code","success");
        }
        return resultMap;
    }

    @GetMapping("info")
    public UserInfo getUserInfo(){
        UserInfo loginDto = new UserInfo();
        String[] roles=new String[2];
        roles[0]="admin";
        roles[1]="nurse";
        loginDto.setRoles(roles);
        return loginDto;
    }
}
