package com.cdroho.utils;

import com.cdroho.modle.dto.SickDto;

import java.util.List;

/**
 * 自定义排序类
 * @author HZL
 * @date 2020-4-17
 */
public class ComparatorQueue {
    public Integer id;
    public List<SickDto> sickDtoList;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<SickDto> getSickDtoList() {
        return sickDtoList;
    }

    public void setSickDtoList(List<SickDto> sickDtoList) {
        this.sickDtoList = sickDtoList;
    }
}
