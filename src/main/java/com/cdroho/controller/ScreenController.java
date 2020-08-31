package com.cdroho.controller;


import com.cdroho.logicalrepository.DoctorRepository;
import com.cdroho.logicalrepository.ScreenRepository;
import com.cdroho.logicalrepository.TriageRepository;
import com.cdroho.modle.DoctorManger;
import com.cdroho.modle.NurseTriage;
import com.cdroho.modle.dto.MachineDto;
import com.cdroho.modle.dto.ScreenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *屏幕控制器
 * @author HZL
 * @date 2020-5-6
 */
@RestController
@RequestMapping("screen")
public class ScreenController {

    @Autowired
    ScreenRepository screenRepository;

    @Autowired
    private TriageRepository triageRepository;

    public static Map<String,Object> resultMap=new HashMap();

    /**
     * 新增屏幕
     * @param screenDto
     * @return
     */
    @PostMapping("addScreen")
    public Map addScreen(@RequestBody ScreenDto screenDto){
        String nurseName = screenDto.getNurseName();
        NurseTriage nurseTriage = triageRepository.findByName(nurseName);
        //找到当前分诊台的id
        long triageId = nurseTriage.getId();
        if(screenDto.getId()!=0){
            screenRepository.updateScreen(screenDto.getScreenName(),screenDto.getScreenIp(),screenDto.getScreenProfile(),
                    triageId,screenDto.getId());
            resultMap.put("code","success");
            resultMap.put("msg","修改成功");
            return resultMap;
        }else{
            if(screenRepository.findByName(screenDto.getScreenName())!=null){
                resultMap.put("msg","当前屏幕已存在");
                resultMap.put("code","failed");
                return resultMap;
            }else{
                resultMap.put("code","success");
                resultMap.put("msg","保存成功");
                screenRepository.insertScreen(screenDto.getScreenName(),screenDto.getScreenIp(),
                        screenDto.getScreenProfile(),triageId);
                return resultMap;
            }
        }
    }

    /**
     * 删除屏幕
     * @param param
     * @return
     */
    @PostMapping(value = "deletescreen")
    public Map deleteScreen(@RequestBody Map<String,Long> param){
        long screenId=param.get("screenId");
        screenRepository.deleteScreenBy(screenId);
        resultMap.put("code","success");
        return resultMap;
    }

    /**
     * 返回屏幕信息列表
     * @return
     */
    @GetMapping("queryscreen")
    public List<ScreenDto> queryScreen() {
        List<ScreenDto> screenDtos = new ArrayList<>();
        List<Object> objects=screenRepository.queryAll();
        for(Object o : objects){
            Object[] oArray=(Object[]) o;
            ScreenDto screenDto=new ScreenDto();
            //object转long
            Long id=Long.valueOf(String.valueOf(oArray[0]));;
            screenDto.setId(id);
            screenDto.setScreenName((String)oArray[1]);
            screenDto.setScreenIp((String)oArray[2]);
            screenDto.setScreenProfile((String)oArray[3]);
            screenDto.setNurseName((String)oArray[4]);
            screenDtos.add(screenDto);
        }
        return  screenDtos;

    }
}
