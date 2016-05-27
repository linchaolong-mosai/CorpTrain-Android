package com.mosai.corporatetraining.bean.coursecomment;

import java.io.Serializable;

public class CourseCommentInfo implements Serializable{
    private String comment;

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return this.comment;
    }

}