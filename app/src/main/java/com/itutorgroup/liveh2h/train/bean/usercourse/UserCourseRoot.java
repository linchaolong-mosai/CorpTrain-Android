package com.itutorgroup.liveh2h.train.bean.usercourse;

import com.itutorgroup.liveh2h.train.entity.HttpResponse;

import java.util.List;

public class UserCourseRoot extends HttpResponse {

    private int total;

    private List<Courses> courses;



    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return this.total;
    }

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    public List<Courses> getCourses() {
        return this.courses;
    }

}