package com.cdroho.modle;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name = "doctor")
public class DoctorManger {

    @Id
    @GeneratedValue
    private long id;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 医生工号
     */
    private String doctorCode;

    /**
     * 所属科室
     */
    private String department;

    /**
     * 医生职称
     */
    private String title;

    /**
     * 医生简介
     */
    private String profile;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorCode() {
        return doctorCode;
    }

    public void setDoctorCode(String doctorCode) {
        this.doctorCode = doctorCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
