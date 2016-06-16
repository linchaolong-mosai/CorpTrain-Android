package com.itutorgroup.liveh2h.train.bean;

import com.itutorgroup.liveh2h.train.entity.HttpResponse;

import java.util.List;

public class CategoryRoot extends HttpResponse {

    private List<Categories> categories;

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public List<Categories> getCategories() {
        return this.categories;
    }

}