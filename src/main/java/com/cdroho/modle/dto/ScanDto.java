package com.cdroho.modle.dto;

/**
 * 报到模板类
 * @date 2020-7-21
 * @author HZL
 */
public class ScanDto {
    private long id;

    private String sickName;

    private String queueName;

    private int sickState;

    private int sickNumber;

    private int timerBreak;

    private int isReport;

    private long queueId;

    private String beginTime;

    private String sickCode;

    private String lastTime;

    private String hisDate;

    public String getSickCode() {
        return sickCode;
    }

    public void setSickCode(String sickCode) {
        this.sickCode = sickCode;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getHisDate() {
        return hisDate;
    }

    public void setHisDate(String hisDate) {
        this.hisDate = hisDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSickName() {
        return sickName;
    }

    public void setSickName(String sickName) {
        this.sickName = sickName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public int getSickState() {
        return sickState;
    }

    public void setSickState(int sickState) {
        this.sickState = sickState;
    }

    public int getSickNumber() {
        return sickNumber;
    }

    public void setSickNumber(int sickNumber) {
        this.sickNumber = sickNumber;
    }

    public int getTimerBreak() {
        return timerBreak;
    }

    public void setTimerBreak(int timerBreak) {
        this.timerBreak = timerBreak;
    }

    public int getIsReport() {
        return isReport;
    }

    public void setIsReport(int isReport) {
        this.isReport = isReport;
    }

    public long getQueueId() {
        return queueId;
    }

    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }
}
