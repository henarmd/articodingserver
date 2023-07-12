package com.articoding.model.articodingLevel;

import java.util.List;

public class ACActiveBlock {

    private String specialBlock;
    private List<ACCategoryData> categories;

    public ACActiveBlock() {
    }

    public String getSpecialBlock() {
        return specialBlock;
    }

    public void setSpecialBlock(String specialBlock) {
        this.specialBlock = specialBlock;
    }

    public List<ACCategoryData> getCategories() {
        return categories;
    }

    public void setCategories(List<ACCategoryData> categories) {
        this.categories = categories;
    }
}
