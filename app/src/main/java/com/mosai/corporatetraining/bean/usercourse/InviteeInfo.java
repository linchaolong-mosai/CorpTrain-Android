package com.mosai.corporatetraining.bean.usercourse;

import java.io.Serializable;

public class InviteeInfo implements Serializable {
    private boolean mandatory;

    private boolean subscribed;

    private String email;

    private long startTime;

    private long endTime;

    private long updateTime;

    private String timeZone;

    private boolean inGroup;

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean getMandatory() {
        return this.mandatory;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean getSubscribed() {
        return this.subscribed;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public void setInGroup(boolean inGroup) {
        this.inGroup = inGroup;
    }

    public boolean getInGroup() {
        return this.inGroup;
    }

}