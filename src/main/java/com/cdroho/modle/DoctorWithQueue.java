package com.cdroho.modle;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 医生与队列
 * @author HZL
 * @date 2020-4-3
 */
@Entity
@Table(name = "doctorwithqueue")
public class DoctorWithQueue {
    @Id
    @GeneratedValue
    private long id;
    private long doctorId;
    private long queueId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }

    public long getQueueId() {
        return queueId;
    }

    public void setQueueId(long queueId) {
        this.queueId = queueId;
    }
}
