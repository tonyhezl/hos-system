package com.cdroho.modle.dto;

/**
 * 呼叫器等候模板类
 * @author HZL
 * @date 2020-10-30
 */
public class CallDto {

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
}
