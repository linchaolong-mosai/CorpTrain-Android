package com.itutorgroup.liveh2h.train.bean.coursecomment;

import java.io.Serializable;

public class CreatorInfo implements Serializable{
    private String creator;

    private String creatorFirstName;

    private String creatorLastName;

    private String creatorEmail;

    private int userSn;

    private String avatar;

    public void setCreator(String creator){
        this.creator = creator;
    }
    public String getCreator(){
        return this.creator;
    }
    public void setCreatorFirstName(String creatorFirstName){
        this.creatorFirstName = creatorFirstName;
    }
    public String getCreatorFirstName(){
        return this.creatorFirstName;
    }
    public void setCreatorLastName(String creatorLastName){
        this.creatorLastName = creatorLastName;
    }
    public String getCreatorLastName(){
        return this.creatorLastName;
    }
    public void setCreatorEmail(String creatorEmail){
        this.creatorEmail = creatorEmail;
    }
    public String getCreatorEmail(){
        return this.creatorEmail;
    }
    public void setUserSn(int userSn){
        this.userSn = userSn;
    }
    public int getUserSn(){
        return this.userSn;
    }
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
    public String getAvatar(){
        return this.avatar;
    }

}