package com.cdroho.modle.dto;


/**
 * @author HZL
 * @date 2020-4-16
 */
public class SortRuleDto {
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

    /**
     * 复诊偏移
     */
    private int againBeCompared;

    /**
     * 过号偏移
     */
    private int passBeCompared;

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

    public int getAgainBeCompared() {
        return againBeCompared;
    }

    public void setAgainBeCompared(int againBeCompared) {
        this.againBeCompared = againBeCompared;
    }

    public int getPassBeCompared() {
        return passBeCompared;
    }

    public void setPassBeCompared(int passBeCompared) {
        this.passBeCompared = passBeCompared;
    }
}
