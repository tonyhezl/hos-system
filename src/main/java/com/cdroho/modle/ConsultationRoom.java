package com.cdroho.modle;


import javax.persistence.*;

/**
 * 诊室
 * @author HZL
 * @date 2020-3-31
 */
@Entity
@Table(name = "room")
public class ConsultationRoom {
    @Id
    @GeneratedValue
    private long id;

    private String roomName;

    private String roomProfile;

    private String roomIp;

    /**
     * 诊室与分诊台 多对一
     */
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", foreignKey = @ForeignKey(name = "FK_Reference_22"))
    private NurseTriage nurseTriage;

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

    public NurseTriage getNurseTriage() {
        return nurseTriage;
    }

    public void setNurseTriage(NurseTriage nurseTriage) {
        this.nurseTriage = nurseTriage;
    }
}
