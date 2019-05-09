package com.code.common.utils;

import java.util.Random;

public class RandomUtils {
    public static Integer nextInt(Integer bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }
}
