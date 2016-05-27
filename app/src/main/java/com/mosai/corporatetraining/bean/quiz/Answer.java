package com.mosai.corporatetraining.bean.quiz;

import java.io.Serializable;

public class Answer implements Serializable{
private String answer;

public void setAnswer(String answer){
this.answer = answer;
}
public String getAnswer(){
return this.answer;
}

}