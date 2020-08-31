package com.cdroho.modle.dto;

/**
 * 屏幕模版类
 * @author HZL
 * @date 2020-5-6
 */
public class ScreenDto {
    private long id;
    private String nurseName;
    private String screenName;
    private String screenIp;
    private String screenProfile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNurseName() {
        return nurseName;
    }

    public void setNurseName(String nurseName) {
        this.nurseName = nurseName;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getScreenIp() {
        return screenIp;
    }

    public void setScreenIp(String screenIp) {
        this.screenIp = screenIp;
    }

    public String getScreenProfile() {
        return screenProfile;
    }

    public void setScreenProfile(String screenProfile) {
        this.screenProfile = screenProfile;
    }
}
