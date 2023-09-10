package com.articoding.model.articodingLevel.initialState;

public class ACBlocklyValue {
    private String name;
    private ACBlocklyBlock block;

    public ACBlocklyValue() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ACBlocklyBlock getBlock() {
        return block;
    }

    public void setBlock(ACBlocklyBlock block) {
        this.block = block;
    }
}
