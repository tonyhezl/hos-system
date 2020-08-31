package com.cdroho.modle;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * 医生队列管理
 * @author HZL
 * @date 2020-3-30
 */

@Entity
@Table(name = "queuelist")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer"})
public class QueueManger {

    @Id
    @GeneratedValue
    private long id;

    private String queueName;

    private String queueProfile;

    private String floorProfile;
    /**
     * 队列与分诊台 多对一
     */
    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id", foreignKey = @ForeignKey(name = "FK_Reference_21"))
    private NurseTriage nurseTriage;

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

    public NurseTriage getNurseTriage() {
        return nurseTriage;
    }

    public void setNurseTriage(NurseTriage nurseTriage) {
        this.nurseTriage = nurseTriage;
    }
}
