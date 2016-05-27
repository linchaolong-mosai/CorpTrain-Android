package com.mosai.corporatetraining.bean.classesforcourse;

import com.mosai.corporatetraining.entity.HttpResponse;

import java.util.List;

public class ClassesRoot extends HttpResponse{

private List<Classes> classes ;

public void setClasses(List<Classes> classes){
this.classes = classes;
}
public List<Classes> getClasses(){
return this.classes;
}

}