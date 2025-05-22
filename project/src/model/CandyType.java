package model;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Enum representing different candy types and their colors.
 */
public enum CandyType {
    A('A', "candy_a.png"),
    B('B', "candy_b.png"),
    C('C', "candy_c.png"),
    D('D', "candy_d.png");

    private final char symbol;
    private final ImageIcon icon;

    CandyType(char symbol, String imagePath) {
        // Rescale icon to fit button
        ImageIcon rawIcon = new ImageIcon(ClassLoader.getSystemResource("sprites/" + imagePath));
        Image scaledImage = rawIcon.getImage().getScaledInstance(GameConfig.ICON_SIZE, GameConfig.ICON_SIZE, Image.SCALE_SMOOTH);

        this.symbol = symbol;
        this.icon = new ImageIcon(scaledImage);
    }

    public char getSymbol() {
        return symbol;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public static CandyType fromChar(char c) {
        for (CandyType type : values()) {
            if (type.symbol == c) return type;
        }
        throw new IllegalArgumentException("Invalid candy: " + c);
    }
}
