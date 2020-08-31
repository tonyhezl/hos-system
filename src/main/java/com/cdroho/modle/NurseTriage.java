package com.cdroho.modle;

import java.io.Serializable;

import javax.persistence.*;

/**
 * 分诊台
 * @author HZL
 * @date 2020 3-24
 */
@Entity
@Table(name = "nursetriage")
public class NurseTriage {

    @Id
    @GeneratedValue
    private long id;
    /**
     * 分诊台名称
     */
    private String name;

    /**
     * 分诊台ip
     */
    private String triageIp;

    /**
     * 分诊台类型
     */
    private String triageType;

    /**
     * '过号次数'
     */
    private int passCount;
    /**
     * '过号间隔'
     */
    private int passWeight;

    /**
     * '复诊间隔'
     */
    private int returnWeight;
    /**
     * '锁定数量'
     */
    private int lockCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTriageIp() {
        return triageIp;
    }

    public void setTriageIp(String triageIp) {
        this.triageIp = triageIp;
    }

    public String getTriageType() {
        return triageType;
    }

    public void setTriageType(String triageType) {
        this.triageType = triageType;
    }

    public int getPassCount() {
        return passCount;
    }

    public void setPassCount(int passCount) {
        this.passCount = passCount;
    }

    public int getPassWeight() {
        return passWeight;
    }

    public void setPassWeight(int passWeight) {
        this.passWeight = passWeight;
    }

    public int getReturnWeight() {
        return returnWeight;
    }

    public void setReturnWeight(int returnWeight) {
        this.returnWeight = returnWeight;
    }

    public int getLockCount() {
        return lockCount;
    }

    public void setLockCount(int lockCount) {
        this.lockCount = lockCount;
    }
}
