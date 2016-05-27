package com.mosai.corporatetraining.bean.resourseforclass;

import com.mosai.corporatetraining.entity.HttpResponse;

import java.util.List;

public class ResourcesRoot extends HttpResponse{

private int total;

private List<Resources> resources ;

public void setTotal(int total){
this.total = total;
}
public int getTotal(){
return this.total;
}
public void setResources(List<Resources> resources){
this.resources = resources;
}
public List<Resources> getResources(){
return this.resources;
}

}