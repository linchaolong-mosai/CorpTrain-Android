package com.itutorgroup.liveh2h.train.bean.classesforcourse;

import com.itutorgroup.liveh2h.train.entity.HttpResponse;

import java.util.List;

public class ClassesRoot extends HttpResponse {

private List<Classes> classes ;

public void setClasses(List<Classes> classes){
this.classes = classes;
}
public List<Classes> getClasses(){
return this.classes;
}

}