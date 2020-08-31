package com.cdroho.modle;

import javax.persistence.*;
import java.util.Date;

/**
 * 患者信息
 * @author HZL
 * @date 2020-4-3
 */
@Entity
@Table(name = "sickqueue")
public class SickMans {
    @Id
    @GeneratedValue
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
     * 是否签到
     */
    private int isReport;
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
     * 呼叫次数
     */
    private int callCount;
    /**
     * 呼叫器IP
     */
    private String machineIp;
    /**
     * 小屏呼叫状态
     */
    private int smallScreenState;
    /**
     * 大屏呼叫状态
     */
    private int bigScreenState;
    /**
     * 备用序号
     */
    private int rowNumber;
    /**
     * 备用状态
     */
    private int rowState;
    /**
     * 分时段就诊开始时间
     */
    private String beginTime;
    /**
     * 分时段就诊结束时间
     */
    private String lastTime;
    /**
     * 患者ID（必须唯一）
     */
    private String sickId;
    /**
     * 午别(0全天、1上午、2下午)
     */
    private int timeBreak;
    /**
     * 最后操作时间
     */
    private String oprTime;
    /**
     * 患者与队列多对一
     */
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "queue_id", foreignKey = @ForeignKey(name = "FK_Reference_12"))
    private QueueManger queueManger;
    /**
     * 患者与医生多对一
     */
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", foreignKey = @ForeignKey(name = "FK_Reference_13"))
    private DoctorManger doctorManger;
    /**
     * 患者与叫号器多对一
     */
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", foreignKey = @ForeignKey(name = "FK_Reference_14"))
    private QueuingMachine queuingMachine;

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

    public int getIsReport() {
        return isReport;
    }

    public void setIsReport(int isReport) {
        this.isReport = isReport;
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

    public int getCallCount() {
        return callCount;
    }

    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    public String getMachineIp() {
        return machineIp;
    }

    public void setMachineIp(String machineIp) {
        this.machineIp = machineIp;
    }

    public int getSmallScreenState() {
        return smallScreenState;
    }

    public void setSmallScreenState(int smallScreenState) {
        this.smallScreenState = smallScreenState;
    }

    public int getBigScreenState() {
        return bigScreenState;
    }

    public void setBigScreenState(int bigScreenState) {
        this.bigScreenState = bigScreenState;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getRowState() {
        return rowState;
    }

    public void setRowState(int rowState) {
        this.rowState = rowState;
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

    public String getSickId() {
        return sickId;
    }

    public void setSickId(String sickId) {
        this.sickId = sickId;
    }

    public int getTimeBreak() {
        return timeBreak;
    }

    public void setTimeBreak(int timeBreak) {
        this.timeBreak = timeBreak;
    }

    public String getOprTime() {
        return oprTime;
    }

    public void setOprTime(String oprTime) {
        this.oprTime = oprTime;
    }

    public QueueManger getQueueManger() {
        return queueManger;
    }

    public void setQueueManger(QueueManger queueManger) {
        this.queueManger = queueManger;
    }

    public DoctorManger getDoctorManger() {
        return doctorManger;
    }

    public void setDoctorManger(DoctorManger doctorManger) {
        this.doctorManger = doctorManger;
    }

    public QueuingMachine getQueuingMachine() {
        return queuingMachine;
    }

    public void setQueuingMachine(QueuingMachine queuingMachine) {
        this.queuingMachine = queuingMachine;
    }
}
