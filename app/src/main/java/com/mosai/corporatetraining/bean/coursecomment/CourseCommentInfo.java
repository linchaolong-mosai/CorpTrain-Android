package com.mosai.corporatetraining.bean.coursecomment;

import java.io.Serializable;

public class CourseCommentInfo implements Serializable{
    private String comment;

    private String linkedCommentId;

    public void setComment(String comment){
        this.comment = comment;
    }
    public String getComment(){
        return this.comment;
    }
    public void setLinkedCommentId(String linkedCommentId){
        this.linkedCommentId = linkedCommentId;
    }
    public String getLinkedCommentId(){
        return this.linkedCommentId;
    }

}