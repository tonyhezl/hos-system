package com.cdroho.modle.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 呼叫器与屏幕关系
 * @author HZL
 * @date 2020-5-13
 */
@Entity
@Table(name = "machine_room")
public class MachieWithRoom {
    @Id
    @GeneratedValue
    private long id;
    private long roomId;
    private long machineId;
}
