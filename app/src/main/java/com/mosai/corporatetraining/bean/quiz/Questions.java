package com.mosai.corporatetraining.bean.quiz;

import com.mosai.corporatetraining.entity.HttpResponse;

import java.util.List;

public class Questions extends HttpResponse{

private List<Question> questions ;

public void setQuestions(List<Question> questions){
this.questions = questions;
}
public List<Question> getQuestions(){
return this.questions;
}

}