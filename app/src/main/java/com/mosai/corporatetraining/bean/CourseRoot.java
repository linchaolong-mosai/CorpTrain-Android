package com.mosai.corporatetraining.bean;

import com.mosai.corporatetraining.entity.HttpResponse;

public class CourseRoot extends HttpResponse{

private Course course;

public void setCourse(Course course){
this.course = course;
}
public Course getCourse(){
return this.course;
}

}