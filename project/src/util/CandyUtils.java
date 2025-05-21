package util;

import model.*;

import java.util.Random;

/**
 * Utility class for generating random candies and managing aniation delay.
 */
public class CandyUtils {
    private static final Random random = new Random();

    public static CandyType getRandomCandy() {
        CandyType[] types = CandyType.values();
        return types[random.nextInt(types.length)];
    }

    public static int getAnimationDelay() {
        return GameConfig.ANIMATION_DELAY_MS;
    }
}