import view.CandyCrushGUI;

import javax.swing.*;

/**
 * Entry point for the Candy Crush game.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(CandyCrushGUI::new);
    }
}