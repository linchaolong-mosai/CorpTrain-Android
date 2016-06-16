package com.itutorgroup.liveh2h.train.bean.quiz;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable{
private String questionId;

private String quizId;

private String text;

private int correctAnswer;

private String explanation;

private List<Answer> answers ;

public void setQuestionId(String questionId){
this.questionId = questionId;
}
public String getQuestionId(){
return this.questionId;
}
public void setQuizId(String quizId){
this.quizId = quizId;
}
public String getQuizId(){
return this.quizId;
}
public void setText(String text){
this.text = text;
}
public String getText(){
return this.text;
}
public void setCorrectAnswer(int correctAnswer){
this.correctAnswer = correctAnswer;
}
public int getCorrectAnswer(){
return this.correctAnswer;
}
public void setExplanation(String explanation){
this.explanation = explanation;
}
public String getExplanation(){
return this.explanation;
}
public void setAnswers(List<Answer> answers){
this.answers = answers;
}
public List<Answer> getAnswers(){
return this.answers;
}

}