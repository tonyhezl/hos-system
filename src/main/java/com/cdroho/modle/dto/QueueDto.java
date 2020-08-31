package com.cdroho.modle.dto;

import java.util.List;

/**
 * 排序队列类
 * @date 2020-5-26
 * @author HZL
 */
public class QueueDto {
    /**
     * 1--初；2--复；3--过；4--优；5锁
     */
    private int tab;

    /**
     * 队列
     */
    private List<SickDto> list;

    public QueueDto(int tab){
        this.tab=tab;
    }


}
