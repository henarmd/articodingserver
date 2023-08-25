package com.articoding.model.articodingLevel;

public class ACCategoryData {

    private String categoryName;
    private ACCategoryBlockInfo blocksInfo;

    public ACCategoryData() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ACCategoryBlockInfo getBlocksInfo() {
        return blocksInfo;
    }

    public void setBlocksInfo(ACCategoryBlockInfo blocksInfo) {
        this.blocksInfo = blocksInfo;
    }
}
