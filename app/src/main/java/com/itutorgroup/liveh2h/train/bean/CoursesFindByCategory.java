package com.itutorgroup.liveh2h.train.bean;

import com.itutorgroup.liveh2h.train.bean.usercourse.Courses;
import com.itutorgroup.liveh2h.train.entity.HttpResponse;

import java.util.List;

public class CoursesFindByCategory extends HttpResponse {

    private List<Courses> courses;

    public void setCourses(List<Courses> courses) {
        this.courses = courses;
    }

    public List<Courses> getCourses() {
        return this.courses;

    }
}