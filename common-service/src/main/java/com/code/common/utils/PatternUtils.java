package com.code.common.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtils {
    private final static String REPLACE_REGEX = "${value}";

    public static boolean match(String original, String reg) {
        Pattern pattern = Pattern.compile(".*" + reg + ".*");
        Matcher matcher = pattern.matcher(original);
        return matcher.matches();
    }

    public static String groupOne(String original, String reg, Integer index) {
        Pattern pattern = Pattern.compile(".*" + reg + ".*");
        Matcher matcher = pattern.matcher(original);

        if (matcher.matches()) {
            return matcher.group(index);
        }
        return null;
    }

    public static Map<Integer, List<String>> groupAll(String original, String reg) {
        Pattern pattern = Pattern.compile(".*" + reg + ".*");
        Matcher matcher = pattern.matcher(original);

        Map<Integer, List<String>> results = null;
        if (matcher.matches()) {
            matcher = Pattern.compile(reg).matcher(original);
            results = new HashMap<>();
            int count = matcher.groupCount();
            while (matcher.find()) {
                for (int i = 1; i <= count; i++) {
                    if (results.get(i) == null) {
                        List<String> result = new LinkedList<>();
                        results.put(i, result);
                    }
                    results.get(i).add(matcher.group(i));
                }
            }
            return results;
        }
        return null;
    }

    public static List<Object[]> groupList(String original, String reg) {
        Pattern pattern = Pattern.compile(".*" + reg + ".*");
        Matcher matcher = pattern.matcher(original);

        List<Object[]> results = null;
        if (matcher.matches()) {
            matcher = Pattern.compile(reg).matcher(original);
            results = new LinkedList<>();
            int count = matcher.groupCount();
            while (matcher.find()) {
                Object[] result = new Object[count];
                for (int i = 0; i < count; i++) {
                    result[i] = matcher.group(i + 1);
                }
                results.add(result);
            }
            return results;
        }
        return null;
    }

    public static boolean isReplace(String original) {
        return match(original, REPLACE_REGEX);
    }
}
