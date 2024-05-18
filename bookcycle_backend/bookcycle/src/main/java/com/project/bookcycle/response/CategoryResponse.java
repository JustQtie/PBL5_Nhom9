package com.project.bookcycle.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.bookcycle.model.Category;

import java.util.List;

public class CategoryResponse {
    @JsonProperty("listCategory")
    private List<Category> listCategory;

    public CategoryResponse(List<Category> listCategory) {
        this.listCategory = listCategory;
    }

    public List<Category> getListCategory() {
        return listCategory;
    }

    public void setListCategory(List<Category> listCategory) {
        this.listCategory = listCategory;
    }
}
