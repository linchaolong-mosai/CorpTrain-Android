package com.mosai.corporatetraining.bean;

import com.mosai.corporatetraining.entity.HttpResponse;

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