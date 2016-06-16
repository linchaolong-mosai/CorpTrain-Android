package com.itutorgroup.liveh2h.train.bean.quiz;

import com.itutorgroup.liveh2h.train.entity.HttpResponse;

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