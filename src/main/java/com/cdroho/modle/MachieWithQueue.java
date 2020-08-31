package com.cdroho.modle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 呼叫器与队列关系
 * @author HZL
 * @date 2020-4-14
 */
@Entity
@Table(name = "machine_queue")
public class MachieWithQueue {
    @Id
    @GeneratedValue
    private long id;
    private long queueId;
    private long machineId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getQueueId() {
        return queueId;
    }

    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }

    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }
}
