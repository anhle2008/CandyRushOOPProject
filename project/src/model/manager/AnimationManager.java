package model.manager;

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
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (toCrush[row][col]) {
                    originalIcons[row][col] = buttons[row][col].getIcon();
                }
            }
        }

        Timer timer = new Timer(CandyUtils.getCrushBlinkDelay(), null);
        timer.addActionListener(e -> {
            if (blinkStep[0] < maxSteps) {
                for (int row = 0; row < size; row++) {
                    for (int col = 0; col < size; col++) {
                        if (toCrush[row][col]) {
                            Icon current = buttons[row][col].getIcon();
                            buttons[row][col].setIcon(current == null ? originalIcons[row][col] : null);
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