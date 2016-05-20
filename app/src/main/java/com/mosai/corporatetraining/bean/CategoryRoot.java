package com.mosai.corporatetraining.bean;

import com.mosai.corporatetraining.entity.HttpResponse;

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