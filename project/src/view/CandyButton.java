package view;

import model.*;

import javax.swing.*;

public class CandyButton extends JButton {
    private final CandyCell cell;

    public CandyButton(CandyCell cell) {
        this.cell = cell;
        setBorderPainted(true);
        setContentAreaFilled(false);
        setBorder(BorderFactory.createEmptyBorder());
        updateIcon(); // Show icons on GUI startup
    }

    public CandyCell getCell() {
        return cell;
    }

    public void updateIcon() {
        CandyType type = cell.getCandyType();
        setIcon(type != null ? type.getIcon() : null);
    }
}