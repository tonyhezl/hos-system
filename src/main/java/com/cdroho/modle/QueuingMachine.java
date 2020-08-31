package com.cdroho.modle;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

/**
 * 叫号器
 * @author HZL
 * @date 2020-3-26
 */
@Entity
@Table(name = "machine")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class QueuingMachine {

    @Id
    @GeneratedValue
    private Long id;

    private String machineName;

    private String roomName;

    private Date loginTime;

    private String machineIp;

    private String machineProfile;

    private long againBeCompared;

    private long passBeCompared;

    /**
     * 叫号器与医生一对一关系
     */
    @OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", foreignKey = @ForeignKey(name = "FK_Reference_47"))
    private DoctorManger doctorManger;

    /**
     * 叫号器与分诊台 多对一
     */
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", foreignKey = @ForeignKey(name = "FK_Reference_68"))
    private NurseTriage nurseTriage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getMachineIp() {
        return machineIp;
    }

    public void setMachineIp(String machineIp) {
        this.machineIp = machineIp;
    }

    public String getMachineProfile() {
        return machineProfile;
    }

    public void setMachineProfile(String machineProfile) {
        this.machineProfile = machineProfile;
    }

    public DoctorManger getDoctorManger() {
        return doctorManger;
    }

    public void setDoctorManger(DoctorManger doctorManger) {
        this.doctorManger = doctorManger;
    }

    public NurseTriage getNurseTriage() {
        return nurseTriage;
    }

    public void setNurseTriage(NurseTriage nurseTriage) {
        this.nurseTriage = nurseTriage;
    }

    public long getAgainBeCompared() {
        return againBeCompared;
    }

    public void setAgainBeCompared(long againBeCompared) {
        this.againBeCompared = againBeCompared;
    }

    public long getPassBeCompared() {
        return passBeCompared;
    }

    public void setPassBeCompared(long passBeCompared) {
        this.passBeCompared = passBeCompared;
    }
}
