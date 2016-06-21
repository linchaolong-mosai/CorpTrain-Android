package com.itutorgroup.liveh2h.train.bean;

import java.io.Serializable;

public class CourseFindByCategory implements Serializable {
    private String courseId;
    private String creatorId;


    private String ctCompanyId;

    private String subject;

    private String company;

    private boolean published;

    private boolean archived;

    private int shareType;

    private int viewCount;

    private String description;

    private int imageVersion;

    private String imageName;

    private float rating;

    private long createTime;

    private long updateTime;

    private long publishTime;

    private int duration;

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public void setCtCompanyId(String ctCompanyId) {
        this.ctCompanyId = ctCompanyId;
    }

    public String getCtCompanyId() {
        return this.ctCompanyId;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return this.company;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean getPublished() {
        return this.published;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean getArchived() {
        return this.archived;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public int getShareType() {
        return this.shareType;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getViewCount() {
        return this.viewCount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setImageVersion(int imageVersion) {
        this.imageVersion = imageVersion;
    }

    public int getImageVersion() {
        return this.imageVersion;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public float getRating() {
        return this.rating;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public long getUpdateTime() {
        return this.updateTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public long getPublishTime() {
        return this.publishTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

}