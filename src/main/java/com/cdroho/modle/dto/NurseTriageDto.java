package com.cdroho.modle.dto;

/**
 * 分诊台管理模版类
 * @author HZL
 * @date 2020-4-10
 */
public class NurseTriageDto {
    private long nurseId;
    private long queueId;
    private long flag;
    private String queueName;
    private long wait;
    private long hasCall;

    public long getNurseId() {
        return nurseId;
    }

    public void setNurseId(long nurseId) {
        this.nurseId = nurseId;
    }

    public long getQueueId() {
        return queueId;
    }

    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }

    public long getFlag() {
        return flag;
    }

    public void setFlag(long flag) {
        this.flag = flag;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public long getWait() {
        return wait;
    }

    public void setWait(long wait) {
        this.wait = wait;
    }

    public long getHasCall() {
        return hasCall;
    }

    public void setHasCall(long hasCall) {
        this.hasCall = hasCall;
    }
}
