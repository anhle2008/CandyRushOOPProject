package model.board;

import model.type.CandyType;

public class CandyCell {
    private final int row;
    private final int column;
    private CandyType candyType;

    public CandyCell(int row, int column, CandyType candyType) {
        this.row = row;
        this.column = column;
        this.candyType = candyType;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public CandyType getCandyType() {
        return candyType;
    }

    public void setCandyType(CandyType candyType) {
        this.candyType = candyType;
    }

    public boolean isEmpty() {
        return candyType == null;
    }

    public void clearCandy() {
        this.candyType = null;
    }
}