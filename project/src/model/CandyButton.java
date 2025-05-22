package model;

import javax.swing.*;

/**
 * Represents a single candy button on the board.
 */
public class CandyButton extends JButton {
    private final int row;
    private final int column;
    private CandyType candyType;

    public CandyButton(int row, int column, CandyType candyType) {
        this.row = row;
        this.column = column;
        setCandy(candyType);
        setBorderPainted(true);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder());
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setCandy(CandyType type) {
        this.candyType = type;

        if (type != null) {
            setIcon(type.getIcon());
        } else {
            setIcon(null);
        }
    }

    public CandyType getCandyType() {
        return candyType;
    }

    public boolean isEmpty() {
        return candyType == null;
    }

    public void clearCandy() {
        this.candyType = null;
        setIcon(null);
    }
}