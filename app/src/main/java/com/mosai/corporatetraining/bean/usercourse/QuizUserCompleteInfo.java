package com.mosai.corporatetraining.bean.usercourse;

import java.io.Serializable;

public class QuizUserCompleteInfo implements Serializable{
private int completed;

private int total;

public void setCompleted(int completed){
this.completed = completed;
}
public int getCompleted(){
return this.completed;
}
public void setTotal(int total){
this.total = total;
}
public int getTotal(){
return this.total;
}

}