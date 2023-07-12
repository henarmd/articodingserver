package com.articoding.model.articodingLevel;

import java.util.List;

public class ACBoardState {


    private int rows;
    private int columns;
    private List<ACBoardCellState> cells;

    private List<ACBoardObjectState> boardElements;

    private List<ACBoardHintState> boardHints;

    public ACBoardState() {
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<ACBoardCellState> getCells() {
        return cells;
    }

    public void setCells(List<ACBoardCellState> cells) {
        this.cells = cells;
    }

    public List<ACBoardObjectState> getBoardElements() {
        return boardElements;
    }

    public void setBoardElements(List<ACBoardObjectState> boardElements) {
        this.boardElements = boardElements;
    }

    public List<ACBoardHintState> getBoardHints() {
        return boardHints;
    }

    public void setBoardHints(List<ACBoardHintState> boardHints) {
        this.boardHints = boardHints;
    }
}
