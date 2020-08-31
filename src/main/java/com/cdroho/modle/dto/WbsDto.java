package com.cdroho.modle.dto;

/**
 * 微信患者查询模板类
 * @date 2020-7-02
 * @author HZL
 */
public class WbsDto {
    private String resultCode;
    private String resultMessage;
    /**
     * 当前序号
     */
    private int idqxh;
    /**
     * 前面就诊人数剩余
     */
    private int iqmjzrssy;
    /**
     * 队列序号
     */
    private int ldlxhl;
    /**
     * 姓名
     */
    private String cxm;
    private String cksmc;
    private String cysmc;
    private String czsmc;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public int getIdqxh() {
        return idqxh;
    }

    public void setIdqxh(int idqxh) {
        this.idqxh = idqxh;
    }

    public int getIqmjzrssy() {
        return iqmjzrssy;
    }

    public void setIqmjzrssy(int iqmjzrssy) {
        this.iqmjzrssy = iqmjzrssy;
    }

    public int getLdlxhl() {
        return ldlxhl;
    }

    public void setLdlxhl(int ldlxhl) {
        this.ldlxhl = ldlxhl;
    }

    public String getCxm() {
        return cxm;
    }

    public void setCxm(String cxm) {
        this.cxm = cxm;
    }

    public String getCksmc() {
        return cksmc;
    }

    public void setCksmc(String cksmc) {
        this.cksmc = cksmc;
    }

    public String getCysmc() {
        return cysmc;
    }

    public void setCysmc(String cysmc) {
        this.cysmc = cysmc;
    }

    public String getCzsmc() {
        return czsmc;
    }

    public void setCzsmc(String czsmc) {
        this.czsmc = czsmc;
    }
}
