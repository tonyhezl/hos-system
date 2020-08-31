package com.cdroho.controller;

import com.cdroho.logicalrepository.QueueRepository;
import com.cdroho.logicalrepository.TriageRepository;
import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.QueueManger;
import com.cdroho.modle.dto.MachineDto;
import com.cdroho.modle.dto.QueueMangerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 队列控制器
 * @author HZL
 * @date 2020-3-26
 */
@RestController
@RequestMapping("queue")
public class QueueMangerController {

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private TriageRepository triageRepository;


    public static Map<String,Object> resultMap=new HashMap();


    /**
     * 新增and修改呼叫器
     * @param queueMangerDto
     * @return
     */
    @PostMapping(value = "addQueueManger")
    public Map addQueueManger(@RequestBody QueueMangerDto queueMangerDto){
        String nurseName = queueMangerDto.getNurseName();
        NurseTriage nurseTriage = triageRepository.findByName(nurseName);
        //找到当前分诊台的id
        long triageId = nurseTriage.getId();
        //Integer machineId=queueMangerDto.getId().intValue();
        if(queueMangerDto.getId()!=0){
            queueRepository.updateQueueManger(queueMangerDto.getQueueName(),queueMangerDto.getQueueProfile(),
                    queueMangerDto.getFloorProfile(),triageId,queueMangerDto.getId());
                resultMap.put("code","success");
                resultMap.put("msg","修改成功");
                return resultMap;
        }else{
            if(queueRepository.findByName(queueMangerDto.getQueueName())!=null){
                resultMap.put("msg","当前队列已存在");
                resultMap.put("code","failed");
                return resultMap;
            }else{
                resultMap.put("code","success");
                resultMap.put("msg","保存成功");
                queueRepository.insertQueueManger(queueMangerDto.getQueueName(),queueMangerDto.getQueueProfile(),
                        queueMangerDto.getFloorProfile(),triageId);
                return resultMap;
            }
        }
    }

    /**
     * 删除医生队列
     * @param queueMangerDto
     * @return
     */
    @PostMapping(value = "deleteQueue")
    public Map deleteQueue(@RequestBody QueueMangerDto queueMangerDto){
        queueRepository.deleteQueueMangerById(queueMangerDto.getId());
        resultMap.put("code","success");
        return resultMap;
    }

    /**
     * 查询队列
     * 自动转化成数组，其内为json对象
     * @return
     */
    @GetMapping("queryQueueManger")
    public List<QueueMangerDto> queryQueueManger() {
        List<QueueMangerDto> dtos = new ArrayList<>();
        List<Object> objects=queueRepository.queryAll();
        for(Object o : objects){
            QueueMangerDto dto=new QueueMangerDto();
            dto=modleToDto((Object[]) o);
            dtos.add(dto);
        }
        return  dtos;
    }

    /**
     * 穿梭框队列数据
     * @param params
     * @return
     */
    @PostMapping("transferqueue")
    public List<QueueMangerDto> getTransferQueue(@RequestBody Map<String,Long> params){
        long machineId = params.get("machineId");
        List<QueueMangerDto> dtos = new ArrayList<>();
        List<Object> objects=queueRepository.queryTransferQueue(machineId);
        for(Object o : objects){
            QueueMangerDto dto=new QueueMangerDto();
            dto=modleToDto((Object[]) o);
            dtos.add(dto);
        }
        return  dtos;
    }

    /**
     * 穿梭框已选队列数据
     * @param params
     * @return
     */
    @PostMapping("transferalready")
    public List<QueueMangerDto> transferalready(@RequestBody Map<String,String> params){
        String nurserName = params.get("nurserName");
        NurseTriage nurseTriage = triageRepository.findByName(nurserName);
        List<QueueMangerDto> dtos = new ArrayList<>();
        List<Object> objects=queueRepository.queryAlreadyTransfer(nurseTriage.getId());
        for(Object o : objects){
            QueueMangerDto dto=new QueueMangerDto();
            dto=modleToDto((Object[]) o);
            dtos.add(dto);
        }
        return  dtos;
    }

    /**
     * 更新队列与呼叫器关系
     * @param params
     * @return
     */
    @PostMapping("undatequeuewithpager")
    public Map undateQueuewithPager(@RequestBody Map<String,Long>params){
        Long machineId = params.get("machineId");
        Long queueId = params.get("queueId");
        Long type = params.get("type");
        if (type == 1) {
            queueRepository.insertMachineWithQueue(queueId,machineId);
            resultMap.put("msg","添加成功");
            return resultMap;
        } else {
            queueRepository.deleteMachineWithQueue(machineId,queueId);
            resultMap.put("msg","移除成功");
            return resultMap;
        }
    }

    /**
     * 转诊待选队列
     * @param params
     * @return
     */
    @PostMapping("swapQueue")
    public List<QueueMangerDto> swapQueue(@RequestBody Map<String,Long> params){
        long nurseId = params.get("nurseId");
        List<QueueMangerDto> dtos = new ArrayList<>();
        List<Object> objects=queueRepository.querySwapQueue(nurseId);
        for(Object o : objects){
            QueueMangerDto dto=new QueueMangerDto();
            dto=modleToDto((Object[]) o);
            dtos.add(dto);
        }
        return  dtos;
    }

    /**
     * 转诊
     * @param params
     * @return
     */
    @PostMapping("undateSwap")
    public Map undateSwap(@RequestBody Map<String,Long>params){
        Long queueId = params.get("queueId");
        Long sickId = params.get("sickId");
        Long nurseId = params.get("nurseId");
        NurseTriage nurseTriage = triageRepository.findNurseTriageById(nurseId);
        String type = nurseTriage.getTriageType();
        int maxNum=Integer.valueOf(String.valueOf(queueRepository.getMaxNum(queueId,0)));
        if (type.equals("1")) {
            List<Object> objects=queueRepository.queryDoctor(queueId);
            int doctorId=Integer.valueOf(String.valueOf(objects.get(0)));
            queueRepository.updateSwapDoctor(maxNum+1, doctorId,queueId,sickId);
            resultMap.put("msg","转诊成功");
        }else if(type.equals("2")){
            queueRepository.updateSwapMachine(maxNum+1,queueId,null,sickId);
            resultMap.put("msg","转诊成功");
        }else{
            resultMap.put("msg","转诊失败");
        }
        return resultMap;
    }

    public static QueueMangerDto modleToDto(Object[] oArray){
        QueueMangerDto dto=new QueueMangerDto();
        //object转long
        Long id=Long.valueOf(String.valueOf(oArray[0]));
        dto.setId(id);
        dto.setQueueName((String)oArray[1]);
        dto.setQueueProfile((String)oArray[2]);
        dto.setFloorProfile((String)oArray[3]);
        if(oArray.length>4){
            dto.setNurseName((String)oArray[4]);
        }
        return dto;
    }
}
