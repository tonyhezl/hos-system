package com.cdroho.controller;


import com.cdroho.logicalrepository.RoomRepository;
import com.cdroho.logicalrepository.TriageRepository;
import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.dto.QueueMangerDto;
import com.cdroho.modle.dto.RoomDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 诊室控制器
 * @author HZL
 * @date 2020-3-31
 */
@RestController
@RequestMapping("room")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TriageRepository triageRepository;

    public static Map<String,Object> resultMap=new HashMap();

    /**
     * 新增and修改诊室
     * @param dto
     * @return
     */
    @PostMapping(value = "addRoom")
    public Map addRoom(@RequestBody RoomDto dto){
        String nurseName = dto.getNurseName();
        NurseTriage nurseTriage = triageRepository.findByName(nurseName);
        //找到当前分诊台的id
        long triageId = nurseTriage.getId();
        //Integer machineId=machineDto.getId().intValue();
        if(dto.getId()!=0){
            roomRepository.updateRoom(dto.getRoomName(),dto.getRoomIp(),
                    dto.getRoomProfile(),triageId,dto.getId());
            resultMap.put("code","success");
            resultMap.put("msg","修改成功");
            return resultMap;
        }else{
            if(roomRepository.findByName(dto.getRoomName())!=null){
                resultMap.put("msg","当前诊室已存在");
                resultMap.put("code","failed");
                return resultMap;
            }else{
                resultMap.put("code","success");
                resultMap.put("msg","保存成功");
                roomRepository.insertRoom(dto.getRoomName(),dto.getRoomIp(),dto.getRoomProfile(),triageId);
                return resultMap;
            }
        }
    }

    /**
     * 删除呼叫器
     * @param dto
     * @return
     */
    @PostMapping(value = "deleteRoom")
    public Map deleteRoom(@RequestBody RoomDto dto){
        roomRepository.deleteConsultationRoomById(dto.getId());
        resultMap.put("code","success");
        return resultMap;
    }

    /**
     * 查询诊室
     * 自动转化成数组，其内为json对象
     * @return
     */
    @GetMapping("queryRoom")
    public List<RoomDto> queryRoom() {
        List<RoomDto> roomDtos = new ArrayList<>();
        List<Object> objects=roomRepository.queryAll();
        for(Object o : objects){
            RoomDto roomDto=new RoomDto();
            Object[] oArray=(Object[]) o;
            //object转long
            Long id=Long.valueOf(String.valueOf(oArray[0]));
            roomDto.setId(id);
            roomDto.setRoomName((String)oArray[1]);
            roomDto.setRoomIp((String)oArray[2]);
            roomDto.setRoomProfile((String)oArray[3]);
            roomDto.setNurseName((String)oArray[4]);
            roomDtos.add(roomDto);
        }
        return  roomDtos;

    }

    /**
     * 穿梭框屏幕数据
     * @param params
     * @return
     */
    @PostMapping("transferroom")
    public List<RoomDto> transferRoom(@RequestBody Map<String,String> params){
        String nurserName = params.get("nurserName");
        NurseTriage nurseTriage = triageRepository.findByName(nurserName);
        List<RoomDto> roomDtos = new ArrayList<>();
        List<Object> objects=roomRepository.findTriageRoom(nurseTriage.getId());
        for(Object o: objects){
            RoomDto dto = new RoomDto();
            dto=modleToDto((Object[]) o);
            roomDtos.add(dto);
        }
        return roomDtos;
    }

    /**
     * 穿梭框已选屏幕数据
     * @param params
     * @return
     */
    @PostMapping("transferedroom")
    public List<RoomDto> transferedroom(@RequestBody Map<String,Long> params){
        long machineId=params.get("machineId");
        List<RoomDto> dtos = new ArrayList<>();
        List<Object> objects=roomRepository.findRoomWithMachine(machineId);
        for(Object o : objects){
            RoomDto dto=new RoomDto();
            dto=modleToDto((Object[]) o);
            dtos.add(dto);
        }
        return  dtos;
    }

    /**
     * 更新屏幕与呼叫器关系
     * @param params
     * @return
     */
    @PostMapping("updateroomwithpager")
    public Map updateroomwithpager(@RequestBody Map<String,Long>params){
        Long machineId = params.get("machineId");
        Long roomId = params.get("roomId");
        Long type = params.get("type");
        if (type == 1) {
            roomRepository.insertRoomWithPager(machineId,roomId);
            resultMap.put("msg","添加成功");
            return resultMap;
        } else {
            roomRepository.deleteMachineRoom(machineId,roomId);
            resultMap.put("msg","移除成功");
            return resultMap;
        }
    }

    private RoomDto modleToDto(Object[] oArray){
        RoomDto dto=new RoomDto();
        Long id=Long.valueOf(String.valueOf(oArray[0]));
        dto.setId(id);
        dto.setRoomIp((String)oArray[1]);
        dto.setRoomName((String)oArray[2]);
        dto.setRoomProfile((String)oArray[3]);
        dto.setNurseName((String)oArray[4]);
        return dto;
    }
}
