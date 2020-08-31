package com.cdroho.modle.dto;

import java.io.Serializable;

/**
 * 患者模板类
 */
public class SickDto implements Serializable {
    private long id;
    /**
     * 患者姓名
     */
    private String sickName;
    /**
     * 患者序号
     */
    private int sickNumber;
    /**
     * 患者状态
     */
    private int sickState;
    /**
     * 患者卡号
     */
    private String sickCode;
    /**
     * 患者锁定状态
     */
    private int isLock;
    /**
     * HIS挂号时间
     */
    private String hisTime;
    /**
     * 呼叫时间
     */
    private String callTime;
    /**
     * 医生姓名
     */
    private String doctorName;
    /**
     * 最后操作时间
     */
    private String opr_time;
    /**
     * 午别
     */
    private int timeBreak;

    /**
     * 呼叫次数
     * @return
     */
    private int callCount;


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

    public int getSickNumber() {
        return sickNumber;
    }

    public void setSickNumber(int sickNumber) {
        this.sickNumber = sickNumber;
    }

    public int getSickState() {
        return sickState;
    }

    public void setSickState(int sickState) {
        this.sickState = sickState;
    }

    public String getSickCode() {
        return sickCode;
    }

    public void setSickCode(String sickCode) {
        this.sickCode = sickCode;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public String getHisTime() {
        return hisTime;
    }

    public void setHisTime(String hisTime) {
        this.hisTime = hisTime;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getOpr_time() {
        return opr_time;
    }

    public void setOpr_time(String opr_time) {
        this.opr_time = opr_time;
    }

    public int getTimeBreak() {
        return timeBreak;
    }

    public void setTimeBreak(int timeBreak) {
        this.timeBreak = timeBreak;
    }

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }
}
