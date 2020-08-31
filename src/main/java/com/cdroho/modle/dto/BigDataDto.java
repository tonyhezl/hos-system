package com.cdroho.modle.dto;

/**
 * 大屏数据模板类
 * @author HZL
 * @date 2020-5-15
 */
public class BigDataDto {


    private long queueId;
    private String queueName;
    private String zsmc;
    private String machineIp;
    private String loginId;

    public long getQueueId() {
        return queueId;
    }

    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getZsmc() {
        return zsmc;
    }

    public void setZsmc(String zsmc) {
        this.zsmc = zsmc;
    }

    public String getMachineIp() {
        return machineIp;
    }

    public void setMachineIp(String machineIp) {
        this.machineIp = machineIp;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
}
