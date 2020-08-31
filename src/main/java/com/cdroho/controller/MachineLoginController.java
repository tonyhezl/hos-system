package com.cdroho.controller;


import com.cdroho.logicalrepository.DoctorRepository;
import com.cdroho.logicalrepository.MachineRepository;
import com.cdroho.logicalrepository.QueueRepository;
import com.cdroho.modle.DoctorManger;
import com.cdroho.modle.QueuingMachine;
import com.cdroho.modle.dto.BigDataDto;
import com.cdroho.utils.Tool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 叫号器登陆控制器
 * @author HZL
 * @date 2020-5-18
 */
@RestController
@CrossOrigin
@RequestMapping("maclogin")
public class MachineLoginController {

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    MachineRepository machineRepository;

    @Autowired
    QueueRepository queueRepository;

    @Autowired
    Tool tool;

    private Logger log = LoggerFactory.getLogger(MachineLoginController.class);

    @PostMapping("machinelogin")
    public Map machineLogin(@RequestParam(value = "longId") String longId, @RequestParam(value = "type") String type,
                            HttpServletRequest request){
        Map<String, Object> map = new HashMap<String, Object>();
        DoctorManger doctor=new DoctorManger();
        QueuingMachine machine=new QueuingMachine();
        List<Object> objects =new ArrayList<>();
        List<BigDataDto> dtos = new ArrayList<>();
        String flag="1";
        String ip="";
        ip=tool.getIpAddr(request);
        //查询出医生by logid
        doctor=doctorRepository.findByCode(longId);
        if(doctor==null||null==doctor){
            map.put("code", "fail");
            map.put("msg", "登陆失败,请确认工号是否正确！");
            return map;
        }
        //查询出叫号器by ip
        if (ip!="") {
            machine=machineRepository.getMachineByIp(ip);
            log.info("当前叫号器===========================>"+machine);
            if(machine==null||null==machine){
                map.put("code", "fail");
                map.put("msg", "登陆失败,此电脑未配置呼叫器！");
                return map;
            }
            //清除此医生登陆其他叫号器的信息
            machineRepository.updateMachineForClean(ip,longId);
        }
        if(flag.equals(type)){
            objects=queueRepository.queryAllByDoctor(longId);
            for(Object o : objects){
                BigDataDto dto=new BigDataDto();
                dto= modleToDto((Object[]) o);
                dtos.add(dto);
            }
            if(dtos.size()>0){
                //更新叫号器doctorId
                if(null!=doctor){
                    String oprTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    machineRepository.updateMachineForDoctor(doctor.getId(),oprTime,machine.getId());
                }
                map.put("code", "success");
                map.put("msg", "操作成功");
                map.put("queueId", dtos.get(0).getQueueId());
                map.put("loginId", longId);
                map.put("machineIp", ip);
            }else if(dtos.size()==0){
                map.put("code", "fail");
                map.put("msg", "该医生没有分配队列");
            }
        }else{
            objects=queueRepository.queryAllByMachine(ip);
            for(Object o : objects){
                BigDataDto dto=new BigDataDto();
                dto= modleToDto((Object[]) o);
                dtos.add(dto);
            }
            if(dtos.size()>0){
                //更新叫号器doctorId
                if(null!=doctor){
                    String oprTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    machineRepository.updateMachineForDoctor(doctor.getId(),oprTime,machine.getId());
                }
                map.put("code", "success");
                map.put("msg", "操作成功");
                map.put("loginId", longId);
                map.put("queueId", dtos.get(0).getQueueId());
            }else if(dtos.size()==0){
                map.put("code", "fail");
                map.put("msg", "该医生没有分配队列");
            }
        }
        return map;
    }

    private BigDataDto modleToDto(Object[] oArray){

        BigDataDto dto=new BigDataDto();
        //object转long
        Long queueid= null;
        if (oArray[0]!=null) {
            queueid = Long.valueOf(String.valueOf(oArray[0]));
            dto.setQueueId(queueid);
        }else{
            dto.setQueueId(0);
        }

        if (oArray[1]!=null) {
            dto.setQueueName((String)oArray[1]);
        }else{
            dto.setQueueName("");
        }
        dto.setZsmc((String)oArray[2]);
        dto.setMachineIp((String)oArray[3]);
        if (oArray[4]!=null) {
            dto.setLoginId((String)oArray[4]);
        }else{
            dto.setLoginId("");
        }
        return dto;
    }
}
