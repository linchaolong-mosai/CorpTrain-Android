package com.mosai.corporatetraining.bean;

import java.io.Serializable;

public class Categories implements Serializable{
    private String parentCategoryId;

    private String parentCategoryName;

    private String categoryId;

    private String ctCompanyId;

    private String name;

    private String updateTime;

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getParentCategoryId() {
        return this.parentCategoryId;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

    public String getParentCategoryName() {
        return this.parentCategoryName;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCtCompanyId(String ctCompanyId) {
        this.ctCompanyId = ctCompanyId;
    }

    public String getCtCompanyId() {
        return this.ctCompanyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

}




