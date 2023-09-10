package com.articoding.model.articodingLevel;


import com.articoding.model.articodingLevel.initialState.ACInitialState;

public class ACLevel {

    private ACActiveBlock activeblocks;
    private ACBoardState boardstate;

    private ACInitialState initialState;

    public ACLevel() {
    }

    public ACActiveBlock getActiveblocks() {
        return activeblocks;
    }

    public void setActiveblocks(ACActiveBlock activeblocks) {
        this.activeblocks = activeblocks;
    }

    public ACBoardState getBoardstate() {
        return boardstate;
    }

    public void setBoardstate(ACBoardState boardstate) {
        this.boardstate = boardstate;
    }
}
