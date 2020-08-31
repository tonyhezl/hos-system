package com.cdroho.modle.dto;

/**
 * 呼叫器模板类
 * @author HZL
 * @date 2020 3-27
 */
public class MachineDto {
    private Long id;
    private String machineName;
    private String roomName;
    private String machineIp;
    private String machineProfile;
    private String nurseName;
    private int againBeCompared;
    private int passBeCompared;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
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
