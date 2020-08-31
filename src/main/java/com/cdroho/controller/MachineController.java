package com.cdroho.controller;

import com.cdroho.logicalrepository.MachineRepository;
import com.cdroho.logicalrepository.TriageRepository;
import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.dto.MachineDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 呼叫器控制器
 * @author HZL
 * @date 2020-3-26
 */
@RestController
@RequestMapping("machine")
public class MachineController {
    private Logger log = LoggerFactory.getLogger(MachineController.class);
    @Autowired
    MachineRepository machineRepository;
    @Autowired
    private TriageRepository triageRepository;
    public static Map<String,Object> resultMap=new HashMap();

    /**
     * 新增and修改呼叫器
     * @param machineDto
     * @return
     */
    @PostMapping(value = "addMchine")
    public Map addMchine(@RequestBody MachineDto machineDto){
        String nurseName = machineDto.getNurseName();
        NurseTriage nurseTriage = triageRepository.findByName(nurseName);
        //找到当前分诊台的id
        long triageId = nurseTriage.getId();
        //Integer machineId=machineDto.getId().intValue();
        if(machineDto.getId()!=null){
                machineRepository.updateMachine(machineDto.getMachineName(),
                        machineDto.getMachineIp(),machineDto.getMachineProfile(),machineDto.getRoomName(),
                        triageId,machineDto.getId());
                resultMap.put("code","success");
                resultMap.put("msg","修改成功");
                return resultMap;
        }else{
            if(machineRepository.findByName(machineDto.getMachineName())!=null){
                resultMap.put("msg","当前呼叫器已存在");
                resultMap.put("code","failed");
                return resultMap;
            }else{
                resultMap.put("code","success");
                resultMap.put("msg","保存成功");
                machineRepository.insertMachine(machineDto.getMachineName(),
                        machineDto.getMachineIp(),machineDto.getMachineProfile(),machineDto.getRoomName(),0,0,triageId);
                return resultMap;
            }
        }
    }

    /**
     * 删除呼叫器
     * @param machineDto
     * @return
     */
    @PostMapping(value = "deletemachine")
    public Map deleteMachine(@RequestBody MachineDto machineDto){
        machineRepository.deleteQueuingMachineById(machineDto.getId());
        resultMap.put("code","success");
        return resultMap;
    }

    /**
     * 查询呼叫器
     * 自动转化成数组，其内为json对象
     * @return
     */
    @GetMapping("querymachine")
    public List<MachineDto> queryMachine() {
        List<MachineDto> machineDtos = new ArrayList<>();
        List<Object> objects=machineRepository.queryAll();
        for(Object o : objects){
            MachineDto machineDto=new MachineDto();
            Object[] oArray=(Object[]) o;
            //object转long
            Long id=Long.valueOf(String.valueOf(oArray[0]));;
            machineDto.setId(id);
            machineDto.setMachineName((String)oArray[1]);
            machineDto.setRoomName((String)oArray[2]);
            machineDto.setMachineIp((String)oArray[3]);
            machineDto.setMachineProfile((String)oArray[4]);
            machineDto.setNurseName((String)oArray[5]);
            machineDtos.add(machineDto);
        }
        return  machineDtos;

    }

    @Scheduled(cron = "0 0 13 * * ?")
    public void clearMachineDoctor(){
        try {
            machineRepository.updateMachineDoctor();
            log.info("清除叫号器医生登陆信息成功！");
        } catch (Exception e) {
            log.info("清除叫号器医生登陆信息失败！");
        }
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void clearDoctor(){
        try {
            machineRepository.updateMachineDoctor();
            log.info("清除叫号器医生登陆信息成功！");
        } catch (Exception e) {
            log.info("清除叫号器医生登陆信息失败！");
        }
    }
}
