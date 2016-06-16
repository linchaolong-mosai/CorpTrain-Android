package com.itutorgroup.liveh2h.train.bean;

import com.itutorgroup.liveh2h.train.entity.HttpResponse;

import java.util.List;

public class CoursesFindByCategory extends HttpResponse {

    private List<CourseFindByCategory> courses;


    public void setCourses(List<CourseFindByCategory> courses) {
        this.courses = courses;
    }

    public List<CourseFindByCategory> getCourses() {
        return this.courses;

    }
}