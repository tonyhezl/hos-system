/*
package com.cdroho.controller;

import com.cdroho.absservice.AbstractQueueService;
import com.cdroho.domaindto.DomainPatientDto;
import com.cdroho.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * 等候患者查询类
 * @author HZL
 *//*

@RestController
@RequestMapping("wait")
public class WaitQueue extends AbstractQueueService {

    private static  final Map<String,Object> map = new HashMap<String,Object>();

    @Autowired
    private QueueService queueService;

    @RequestMapping("queueList")
    public Map<String,Object> waitQueue(@RequestParam(value="loginId") String loginId, @RequestParam(value="queueTypeId") String queueTypeId){

        List<DomainPatientDto> dtoList = new ArrayList<>();

        dtoList = queueService.getLockedQueueForDoctor(loginId,queueTypeId);//将list集合存入局部变量数组的

        if(dtoList.size()<0){

            map.put("count",dtoList.size());

            map.put("message","暂无锁定患者");

            return map;
        }

        map.put("count",dtoList.size());

        map.put("dtoList",dtoList);

        map.put("message","查询成功");

        return map;
    }
    @Override
    public void sortPatientQueue() {
        List<String> list=new ArrayList();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        list.remove(0);

    }
}
*/
