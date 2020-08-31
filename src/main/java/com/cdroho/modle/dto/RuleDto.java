package com.cdroho.modle.dto;

import org.springframework.context.annotation.Configuration;

/**
 * 规则bean
 * @author HZL
 */

public class RuleDto {
    //过号次数
    private int passNum;

    //过号间隔
    private int passFlag;

    //复诊间隔
    private int returnFlag;

    //迟到次数
    private int lateNum;

    //迟到间隔
    private int lateFlag;

    private int return_flag_step;

    private int late_flag_step;

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }

    public void setPassFlag(int passFlag) {
        this.passFlag = passFlag;
    }

    public void setReturnFlag(int returnFlag) {
        this.returnFlag = returnFlag;
    }

    public void setLateNum(int lateNum) {
        this.lateNum = lateNum;
    }

    public void setLateFlag(int lateFlag) {
        this.lateFlag = lateFlag;
    }

    public void setLate_flag_step(int late_flag_step) {
        this.late_flag_step = late_flag_step;
    }

    public void setReturn_flag_step(int return_flag_step) {
        this.return_flag_step = return_flag_step;
    }

    public int getPassNum() {
        return passNum;
    }

    public int getPassFlag() {
        return passFlag;
    }

    public int getReturnFlag() {
        return returnFlag;
    }

    public int getLateNum() {
        return lateNum;
    }

    public int getLateFlag() {
        return lateFlag;
    }

    public int getLate_flag_step() {
        return late_flag_step;
    }

    public int getReturn_flag_step() {
        return return_flag_step;
    }
}
