package view;

import javax.swing.*;

import model.core.GameMode;

import java.awt.*;

public class GameMenuGUI extends JFrame {
    public GameMenuGUI() {
        setTitle("Select Game Mode");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JButton endlessBtn = new JButton("Endless");
        JButton timeBtn = new JButton("Beat the clock");
        JButton moveBtn = new JButton("Limited move");

        endlessBtn.addActionListener(e -> launchGame(GameMode.ENDLESS));
        timeBtn.addActionListener(e -> launchGame(GameMode.TIME_LIMITED));
        moveBtn.addActionListener(e -> launchGame(GameMode.MOVE_LIMITED));

        add(endlessBtn);
        add(timeBtn);
        add(moveBtn);

        setVisible(true);
    }

    private void launchGame(GameMode mode) {
        dispose(); // Close menu
        new CandyCrushGUI(mode); // Pass mode to game
    }
}