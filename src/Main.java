import view.GameMenuGUI;

import javax.swing.SwingUtilities;

/**
 * Entry point for the Candy Crush game.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMenuGUI::new);
    }
}