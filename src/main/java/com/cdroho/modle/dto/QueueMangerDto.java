package com.cdroho.modle.dto;


/**
 * 医生队列模版类
 * @author HZL
 * @date 2020-3-30
 */
public class QueueMangerDto {
    private long id;
    private String queueName;
    private String queueProfile;
    private String floorProfile;
    private String nurseName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueProfile() {
        return queueProfile;
    }

    public void setQueueProfile(String queueProfile) {
        this.queueProfile = queueProfile;
    }

    public String getFloorProfile() {
        return floorProfile;
    }

    public void setFloorProfile(String floorProfile) {
        this.floorProfile = floorProfile;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }
}
