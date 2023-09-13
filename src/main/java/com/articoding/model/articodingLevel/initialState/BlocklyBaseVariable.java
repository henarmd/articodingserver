package com.articoding.model.articodingLevel.initialState;

import java.util.List;

public class BlocklyBaseVariable {

    private List<ACBlocklyVariable> variable;

    public BlocklyBaseVariable() {
    }

    public List<ACBlocklyVariable> getVariable() {
        return variable;
    }

    public void setVariable(List<ACBlocklyVariable> variable) {
        this.variable = variable;
    }
}
