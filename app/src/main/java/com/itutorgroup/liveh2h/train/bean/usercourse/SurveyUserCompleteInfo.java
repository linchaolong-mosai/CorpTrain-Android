package com.itutorgroup.liveh2h.train.bean.usercourse;

import java.io.Serializable;

public class SurveyUserCompleteInfo implements Serializable{
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