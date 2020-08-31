package com.cdroho.modle.dto;

public class ReportDto {
    private long id;

    private String sickName;

    private String queueName;

    private int sickState;

    private int sickNumber;

    private int timerBreak;

    private int isReport;

    private long queueId;

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
