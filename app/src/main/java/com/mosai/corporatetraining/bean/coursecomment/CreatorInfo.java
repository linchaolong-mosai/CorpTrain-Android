package com.mosai.corporatetraining.bean.coursecomment;

import java.io.Serializable;

public class CreatorInfo implements Serializable{
    private String creator;

    private String creatorEmail;

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public String getCreatorEmail() {
        return this.creatorEmail;
    }

}