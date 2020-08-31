package com.cdroho.utils;

import com.cdroho.modle.CheckSick;
import com.cdroho.modle.dto.SickDto;
import org.springsource.loaded.C;

import java.util.List;

/**
 * 自定义排序类
 * @author HZL
 * @date 2020-4-17
 */
public class ComparatorQueueOne {
    public Integer id;
    public List<CheckSick> sickDtoList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CheckSick> getSickDtoList() {
        return sickDtoList;
    }

    public void setSickDtoList(List<CheckSick> sickDtoList) {
        this.sickDtoList = sickDtoList;
    }
}
