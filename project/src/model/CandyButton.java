package model;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a single candy button on the board.
 */
public class CandyButton extends JButton {
    private final int row;
    private final int column;

    public CandyButton(int row, int column, CandyType candyType) {
        super(String.valueOf(candyType.getSymbol()));
        this.row = row;
        this.column = column;
        setFont(new Font("Arial", Font.BOLD, 20));
        setBackground(candyType.getColor());
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setCandy(CandyType type) {
        setText(String.valueOf(type.getSymbol()));
        setBackground(type.getColor());
    }

    public CandyType getCandyType() {
        if (isEmpty()) return null;
        return CandyType.fromChar(getText().charAt(0));
    }

    public boolean isEmpty() {
        return getText().equals(" ");
    }

    public void clearCandy() {
        setText(" ");
        setBackground(Color.WHITE);
    }
}