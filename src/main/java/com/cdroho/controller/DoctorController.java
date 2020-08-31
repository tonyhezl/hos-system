package com.cdroho.controller;

import com.cdroho.logicalrepository.DoctorRepository;
import com.cdroho.modle.DoctorManger;
import com.cdroho.modle.NurseTriage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 医生控制器
 * @author HZL
 * @date 2020-3-26
 */
@RestController
@RequestMapping("doctor")
public class DoctorController {

    @Autowired
    DoctorRepository doctorRepository;

    public static Map<String,Object> resultMap=new HashMap();

    /**
     * 新增医生
     * @param doctorManger
     * @return
     */
    @PostMapping("adddoctor")
    public Map addDoctor(@RequestBody DoctorManger doctorManger){
        if(doctorRepository.findByName(doctorManger.getDoctorName())!=null){
            resultMap.put("msg","当前医生已存在");
            resultMap.put("code","failed");
            return resultMap;
        }
        resultMap.put("code","success");
        doctorRepository.save(doctorManger);
        return resultMap;
    }

    /**
     * 删除医生
     * @param doctorManger
     * @return
     */
    @PostMapping(value = "deletedoctor")
    public Map deleteTriage(@RequestBody DoctorManger doctorManger){
        DoctorManger manger = doctorRepository.findByName(doctorManger.getDoctorName());
        doctorRepository.deleteNurseTriageBy(manger.getId());
        resultMap.put("code","success");
        return resultMap;
    }

    /**
     * 返回医生数据
     * @return
     */
    @GetMapping("querydoctor")
    public Iterable<DoctorManger> queryDoctor() {

        return  doctorRepository.findAll();

    }
}
