package com.itutorgroup.liveh2h.train.bean.usercourse;

import java.io.Serializable;

public class AttendeeInfo implements Serializable{
private String ctUserId;

private boolean favorite;

private int completePercent;

private int rating;

public void setCtUserId(String ctUserId){
this.ctUserId = ctUserId;
}
public String getCtUserId(){
return this.ctUserId;
}
public void setFavorite(boolean favorite){
this.favorite = favorite;
}
public boolean getFavorite(){
return this.favorite;
}
public void setCompletePercent(int completePercent){
this.completePercent = completePercent;
}
public int getCompletePercent(){
return this.completePercent;
}
public void setRating(int rating){
this.rating = rating;
}
public int getRating(){
return this.rating;
}

}