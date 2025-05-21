package model;

import java.awt.Color;

/**
 * Enum representing different candy types and their colors.
 */
public enum CandyType {
    A('A', Color.RED),
    B('B', Color.GREEN),
    C('C', Color.BLUE),
    D('D', Color.MAGENTA);

    private final char symbol;
    private final Color color;

    CandyType(char symbol, Color color) {
        this.symbol = symbol;
        this.color = color;
    }

    public char getSymbol() {
        return symbol;
    }

    public Color getColor() {
        return color;
    }

    public static CandyType fromChar(char c) {
        for (CandyType type : values()) {
            if (type.symbol == c) return type;
        }
        throw new IllegalArgumentException("Invalid candy: " + c);
    }
}
