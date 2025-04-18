import java.awt.Color;
import java.util.Random;

public class CandyUtils {
    private static final char[] candies = {'A', 'B', 'C', 'D'};
    private static final Random random = new Random();

    public static char randomCandy() {
        return candies[random.nextInt(candies.length)];
    }

    public static Color getColorForCandy(char candy) {
        return switch (candy) {
            case 'A' -> Color.RED;
            case 'B' -> Color.GREEN;
            case 'C' -> Color.BLUE;
            case 'D' -> Color.MAGENTA;
            default -> Color.LIGHT_GRAY;
        };
    }
}