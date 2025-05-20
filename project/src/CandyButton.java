import javax.swing.JButton;
import java.awt.*;

public class CandyButton extends JButton {
    private int row, col;

    public CandyButton(int row, int col, char candy, Color color) {
        super(String.valueOf(candy));
        this.row = row;
        this.col = col;
        setFont(new Font("Arial", Font.BOLD, 20));
        setBackground(color);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}