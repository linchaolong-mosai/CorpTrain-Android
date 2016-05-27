package com.mosai.corporatetraining.bean.coursecomment;

import java.io.Serializable;

public class Comments implements Serializable{
private CourseCommentInfo courseCommentInfo;

private CreatorInfo creatorInfo;

public void setCourseCommentInfo(CourseCommentInfo courseCommentInfo){
this.courseCommentInfo = courseCommentInfo;
}
public CourseCommentInfo getCourseCommentInfo(){
return this.courseCommentInfo;
}
public void setCreatorInfo(CreatorInfo creatorInfo){
this.creatorInfo = creatorInfo;
}
public CreatorInfo getCreatorInfo(){
return this.creatorInfo;
}

}