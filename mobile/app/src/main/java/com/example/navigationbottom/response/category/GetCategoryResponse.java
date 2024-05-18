package com.example.navigationbottom.response.category;

import com.example.navigationbottom.model.Category;

import java.util.List;

public class GetCategoryResponse {
    private List<Category> listCategory;

    public List<Category> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<Category> listCategory) {
        this.listCategory = listCategory;
    }
}
