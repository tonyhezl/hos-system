package com.cdroho.modle.dto;

/**
 * 针对一个区不同分诊台下的诊室共同显示在大屏上
 * 模板类
 * @author HZL
 * @date 2020-7-14
 */
public class BigDifDto {
    private long queueId;
    private String queueName;
    private String zsmc;
    private String machineIp;
    private String loginId;
    private long nursId;

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

    public long getNursId() {
        return nursId;
    }

    public void setNursId(long nursId) {
        this.nursId = nursId;
    }
}
