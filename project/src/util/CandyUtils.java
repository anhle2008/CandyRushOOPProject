package util;

import model.config.GameConfig;
import model.type.CandyType;

import java.util.Random;

/**
 * Utility class for generating random candies and managing animation delay.
 */
public class CandyUtils {
    private static final Random random = new Random();

    public static CandyType getRandomCandy() {
        CandyType[] types = CandyType.values();
        return types[random.nextInt(types.length)];
    }

    public static int getSwapDelay() {
        return GameConfig.SWAP_DELAY_MS;
    }

    public static int getCrushBlinkDelay() {
        return GameConfig.CRUSH_BLINK_DELAY_MS;
    }

    public static int getPostCrushDelay() {
        return GameConfig.POST_CRUSH_DELAY_MS;
    }
}