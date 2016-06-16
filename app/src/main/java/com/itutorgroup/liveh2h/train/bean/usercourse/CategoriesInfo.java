package com.itutorgroup.liveh2h.train.bean.usercourse;

import java.io.Serializable;
import java.util.List;

public class CategoriesInfo implements Serializable{
private List<Categories> categories ;

public void setCategories(List<Categories> categories){
this.categories = categories;
}
public List<Categories> getCategories(){
return this.categories;
}

}