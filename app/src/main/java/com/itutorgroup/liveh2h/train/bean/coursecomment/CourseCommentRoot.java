package com.itutorgroup.liveh2h.train.bean.coursecomment;

import com.itutorgroup.liveh2h.train.entity.HttpResponse;

import java.util.List;

public class CourseCommentRoot extends HttpResponse {

    private List<Comments> comments;

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }

    public List<Comments> getComments() {
        return this.comments;
    }

}