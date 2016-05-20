package com.mosai.corporatetraining.bean.usercourse;

import java.io.Serializable;

public class Courses implements Serializable{
private CourseInfo courseInfo;

private CreatorInfo creatorInfo;

private String tagInfo;

private CategoriesInfo categoriesInfo;

private int classCount;

private QuizUserCompleteInfo quizUserCompleteInfo;

private SurveyUserCompleteInfo surveyUserCompleteInfo;

private AttendeeInfo attendeeInfo;

private InviteeInfo inviteeInfo;

public void setCourseInfo(CourseInfo courseInfo){
this.courseInfo = courseInfo;
}
public CourseInfo getCourseInfo(){
return this.courseInfo;
}
public void setCreatorInfo(CreatorInfo creatorInfo){
this.creatorInfo = creatorInfo;
}
public CreatorInfo getCreatorInfo(){
return this.creatorInfo;
}
public void setTagInfo(String tagInfo){
this.tagInfo = tagInfo;
}
public String getTagInfo(){
return this.tagInfo;
}
public void setCategoriesInfo(CategoriesInfo categoriesInfo){
this.categoriesInfo = categoriesInfo;
}
public CategoriesInfo getCategoriesInfo(){
return this.categoriesInfo;
}
public void setClassCount(int classCount){
this.classCount = classCount;
}
public int getClassCount(){
return this.classCount;
}
public void setQuizUserCompleteInfo(QuizUserCompleteInfo quizUserCompleteInfo){
this.quizUserCompleteInfo = quizUserCompleteInfo;
}
public QuizUserCompleteInfo getQuizUserCompleteInfo(){
return this.quizUserCompleteInfo;
}
public void setSurveyUserCompleteInfo(SurveyUserCompleteInfo surveyUserCompleteInfo){
this.surveyUserCompleteInfo = surveyUserCompleteInfo;
}
public SurveyUserCompleteInfo getSurveyUserCompleteInfo(){
return this.surveyUserCompleteInfo;
}
public void setAttendeeInfo(AttendeeInfo attendeeInfo){
this.attendeeInfo = attendeeInfo;
}
public AttendeeInfo getAttendeeInfo(){
return this.attendeeInfo;
}
public void setInviteeInfo(InviteeInfo inviteeInfo){
this.inviteeInfo = inviteeInfo;
}
public InviteeInfo getInviteeInfo(){
return this.inviteeInfo;
}

}