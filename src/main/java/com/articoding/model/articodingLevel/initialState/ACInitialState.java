package com.articoding.model.articodingLevel.initialState;

import java.util.List;

public class ACInitialState {
    private List<ACBlocklyVariable> variables;
    private ACBlocklyBlock block;

    public ACInitialState() {
    }

    public List<ACBlocklyVariable> getVariables() {
        return variables;
    }

    public void setVariables(List<ACBlocklyVariable> variables) {
        this.variables = variables;
    }

    public ACBlocklyBlock getBlock() {
        return block;
    }

    public void setBlock(ACBlocklyBlock block) {
        this.block = block;
    }
}
