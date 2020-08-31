package com.cdroho.controller;


import com.cdroho.logicalrepository.RoomRepository;
import com.cdroho.logicalrepository.TriageRepository;
import com.cdroho.modle.ConsultationRoom;
import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.User;
import com.cdroho.modle.dto.NurseTriageDto;
import com.cdroho.modle.dto.RoomDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分诊台管理控制器
 * @author HZL
 * @date 2020-4-10
 */
@RestController
@RequestMapping("manger")
public class NurseTriageManger {

    @Autowired
    private TriageRepository triageRepository;

    @Autowired
    private RoomRepository roomRepository;

    /**
     * 护士登陆获取分诊台
     * @return
     */
    @GetMapping("triage")
    public List<NurseTriage> getNurseByUser(){
        /*Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        long nurseId=user.getNurseId();
        NurseTriage nurseTriage = triageRepository.findNurseTriageById(nurseId);*/
        List<NurseTriage> triageList = triageRepository.findList();
        return triageList;
    }

    /**
     * 根据分诊台ID获取诊室和对应诊室队列
     * @param param
     * @return
     */
    @PostMapping("room")
    public List<NurseTriageDto> getRoomByNurseId(@RequestBody Map<String,Long> param){
        long nurseId=param.get("nurseId");
        List<Object> objects=roomRepository.findRoomByNurseId(nurseId);
        List<NurseTriageDto> roomDtos = new ArrayList<>();
        for(Object o:objects){
            NurseTriageDto roomDto=new NurseTriageDto();
            Object[] oArray=(Object[]) o;
            Long queueId=Long.valueOf(String.valueOf(oArray[0]));
            Long wait=Long.valueOf(String.valueOf(oArray[2]));
            Long hasCall=Long.valueOf(String.valueOf(oArray[3]));
            roomDto.setNurseId(nurseId);
            roomDto.setQueueId(queueId);
            roomDto.setQueueName(String.valueOf(oArray[1]));
            roomDto.setFlag(1);
            roomDto.setWait(wait);
            roomDto.setHasCall(hasCall);
            roomDtos.add(roomDto);
        }
        return roomDtos;
    }
}
