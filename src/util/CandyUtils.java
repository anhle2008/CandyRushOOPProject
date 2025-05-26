package util;

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
}