package com.alihaine.bulchunkbuster.utils;

import java.util.Random;

public class MathUtils {
    private static final Random random = new Random();

    public static double getRandomDouble() {
        return random.nextDouble() * 100;
    }
}
