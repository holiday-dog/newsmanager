package com.code.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnicodeUtils {

    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("\\\\u(\\p{XDigit}+)");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String match = matcher.group(1);
            if (match.length() > 4) {
                String start = StringUtils.substring(match, 0, 4);
                start = (char) Integer.parseInt(start, 16) + "";
                sb.append(start);
                sb.append(StringUtils.substring(match, 4));
            } else {
                String start = (char) Integer.parseInt(match, 16) + "";
                sb.append(start);
            }
        }
        return sb.toString();
    }

    public static String stringToUnicode(String string) {
        if (string == null || "".equals(string)) {
            return null;
        }
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    public static String unicodeToCn(String str) {
        /** 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格 */
        String[] strs = str.split("\\\\u");
        StringBuffer sb = new StringBuffer();
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
            if (strs[i].length() > 4) {
                String start = StringUtils.substring(strs[i], 0, 4);
                start = (char) Integer.parseInt(start, 16) + "";
                sb.append(start);
                sb.append(StringUtils.substring(strs[i], 4));
            } else {
                String start = (char) Integer.parseInt(strs[i], 16) + "";
                sb.append(start);
            }
        }
        return sb.toString();
    }

}
