package com.code.common.utils;

import java.util.Random;
import java.util.UUID;

public class RandomUtils {
    public static Integer nextInt(Integer bound) {
        Random random = new Random();
        return random.nextInt(bound);
    }

    public static <T> T nextObj(T[] objs) {
        if (objs == null) {
            return null;
        }
        return objs[nextInt(objs.length)];
    }

    public static String nextString() {
        String sign = UUID.randomUUID().toString();
        if (sign.contains("-")) {
            sign = sign.replace("-", "");
        }
        return sign;
    }
}
