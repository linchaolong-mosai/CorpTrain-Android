package com.mosai.corporatetraining.bean.resourseforclass;

import java.io.Serializable;

public class Resources implements Serializable {
//    public boolean exist;
    public int percent;
    public boolean exist;
    public long totalcount;
    public long currentcount;
    public boolean showProgress;
    private String classId;

    private String resourceId;

    private int resourceType;

    private int sequence;

    private String name;

    private int total;

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return this.classId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return this.resourceId;
    }

    public void setResourceType(int resourceType) {
        this.resourceType = resourceType;
    }

    public int getResourceType() {
        return this.resourceType;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resources resources = (Resources) o;

        if (!classId.equals(resources.classId)) return false;
        return resourceId.equals(resources.resourceId);

    }

    @Override
    public int hashCode() {
        int result = classId.hashCode();
        result = 31 * result + resourceId.hashCode();
        return result;
    }
}