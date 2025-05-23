package model.logic;

import util.CandyUtils;
import view.CandyButton;
import model.board.CandyCell;

import javax.swing.*;

/**
 * Handles animation, including matching, dropping, swapping, etc.
 */
public class AnimationManager {
    private final CandyCell[][] board;
    private final CandyButton[][] buttons;

    public AnimationManager(CandyCell[][] board, CandyButton[][] buttons) {
        this.board = board;
        this.buttons = buttons;
    }

    /**
     * Animation for swapping candies.
     */
    public void animateSwap(CandyButton a, CandyButton b, Runnable onComplete) {
        Icon iconA = a.getIcon();
        Icon iconB = b.getIcon();

        Timer timer = new Timer(CandyUtils.getSwapDelay(), null);
        timer.addActionListener(e -> {
            a.setIcon(iconB);
            b.setIcon(iconA);
            onComplete.run();
            timer.stop();
        });

        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Animation for matching/crushing candies by blinking.
     */
    public void animateCrush(boolean[][] toCrush, Runnable onComplete) {
        final int size = board.length;
        final int[] blinkStep = {0};
        final int maxSteps = 4;

        // Store original icons for blinking toggle
        Icon[][] originalIcons = new Icon[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (toCrush[i][j]) {
                    originalIcons[i][j] = buttons[i][j].getIcon();
                }
            }
        }

        Timer timer = new Timer(CandyUtils.getCrushBlinkDelay(), null);
        timer.addActionListener(e -> {
            if (blinkStep[0] < maxSteps) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (toCrush[i][j]) {
                            Icon current = buttons[i][j].getIcon();
                            buttons[i][j].setIcon(current == null ? originalIcons[i][j] : null);
                        }
                    }
                }
                blinkStep[0]++;
            } else {
                timer.stop();
                onComplete.run();
            }
        });

        timer.start();
    }
}