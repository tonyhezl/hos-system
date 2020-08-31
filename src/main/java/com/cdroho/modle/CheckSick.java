package com.cdroho.modle;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @date 2020-6-28
 * @author HZL
 */
@Entity
@Table(name = "check_sick")
public class CheckSick {

    @Id
    @GeneratedValue
    private int id;
    /**
     * 窗口标识
     */
    private int flag;
    /**
     * 状态
     */
    private int state;
    /**
     * 姓名
     */
    private String name;
    /**
     * 号序
     */
    private int number;
    /**
     * 业务标识(1、抽血 2、药房)
     */
    private int business;

    /**
     * 是否签到（1、未签到 2、签到）
     */
    private int isReport;

    /**
     * 患者编码
     */
    private String code;

    /**
     * 是否锁定
     */
    private int isLock;

    /**
     * 最后操作时间
     */
    private String oprTime;

    /**
     * 呼叫次数
     * @return
     */
    private int callCount;

    /**
     * 呼叫时间
     */
    private String callTime;

    /**
     * 药房处方号
     */
    private String keyNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBusiness() {
        return business;
    }

    public void setBusiness(int business) {
        this.business = business;
    }

    public int getIsReport() {
        return isReport;
    }

    public void setIsReport(int isReport) {
        this.isReport = isReport;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public String getOprTime() {
        return oprTime;
    }

    public void setOprTime(String oprTime) {
        this.oprTime = oprTime;
    }

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(String keyNum) {
        this.keyNum = keyNum;
    }
}
