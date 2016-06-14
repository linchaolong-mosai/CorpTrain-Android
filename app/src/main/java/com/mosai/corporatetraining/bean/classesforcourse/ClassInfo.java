package com.mosai.corporatetraining.bean.classesforcourse;

import java.io.Serializable;

public class ClassInfo implements Serializable {
    public int percent;
    private String classId;

    private String ctCompanyId;

    private String subject;

    private String creatorId;

    private long createTime;

    private long updateTime;

    private String description;

    private String imageName;

    private int imageVersion;

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return this.classId;
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

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorId() {
        return this.creatorId;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageName() {
        return this.imageName;
    }

    public void setImageVersion(int imageVersion) {
        this.imageVersion = imageVersion;
    }

    public int getImageVersion() {
        return this.imageVersion;
    }

}