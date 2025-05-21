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
    
    public void handleCandyClick(CandyButton clicked) {
        if (firstButton == null) {
            firstButton = clicked;
            view.highlightButton(firstButton, true);
        } else {
            view.highlightButton(firstButton, false);

            if (firstButton != clicked && game.areAdjacent(firstButton, clicked)) {
                CandyButton a = firstButton;
                CandyButton b = clicked;
                game.animateSwap(a, b, () -> {
                    game.swap(a, b);
                    if (game.hasMatch()) {
                        game.processMatches(true);
                    } else {
                        game.animateSwap(a, b, () -> game.swap(a, b)); // revert
                    }
                });
            }

            firstButton = null;
        }
    }
}