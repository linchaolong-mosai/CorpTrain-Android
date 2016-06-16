package com.itutorgroup.liveh2h.train.bean;

import com.itutorgroup.liveh2h.train.entity.HttpResponse;

public class CourseRoot extends HttpResponse {

private Course course;

public void setCourse(Course course){
this.course = course;
}
public Course getCourse(){
return this.course;
}

}