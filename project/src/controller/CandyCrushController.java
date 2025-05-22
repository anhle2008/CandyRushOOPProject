package controller;

import model.*;
import view.*;

public class CandyCrushController {
    private final CandyCrushGame game;
    private final CandyCrushGUI view;
    
    private CandyButton firstButton = null;
    
    public CandyCrushController(CandyCrushGame game, CandyCrushGUI view) {
        this.game = game;
        this.view = view;
    }
    
    public void handleCandyClick(CandyButton clickedButton) {
        if (firstButton == null) {
            // First selection
            firstButton = clickedButton;
            view.highlightButton(firstButton, true);
        } else {
            // Second selection
            view.highlightButton(firstButton, false);

            if (firstButton != clickedButton) {
                CandyCell firstCell = firstButton.getCell();
                CandyCell secondCell = clickedButton.getCell();

                if (game.areAdjacent(firstCell, secondCell)) {
                    CandyButton a = firstButton;
                    CandyButton b = clickedButton;

                    game.animateSwap(a, b, () -> {
                        game.swap(firstCell, secondCell);

                        if (game.hasMatch()) {
                            game.processMatches(true);
                        } else {
                            game.animateSwap(a, b, () -> game.swap(firstCell, secondCell)); // revert
                        }
                    });
                }

            }

            firstButton = null;
        }
    }
}