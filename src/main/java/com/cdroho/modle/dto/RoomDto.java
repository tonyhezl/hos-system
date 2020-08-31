package com.cdroho.modle.dto;

/**
 * 诊室模板类
 * @author HZL
 * @date 2020 3-27
 */
public class RoomDto {
    private long id;
    private String roomName;
    private String roomProfile;
    private String roomIp;
    private String nurseName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomProfile() {
        return roomProfile;
    }

    public void setRoomProfile(String roomProfile) {
        this.roomProfile = roomProfile;
    }

    public String getRoomIp() {
        return roomIp;
    }

    public void setRoomIp(String roomIp) {
        this.roomIp = roomIp;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }
}
