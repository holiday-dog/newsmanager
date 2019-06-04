package com.code.analyse.utils;

import java.util.List;
import java.util.Map;

public class ConvertUtils {
    public static String convertMapList(List entryList, String speator) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Object str : entryList) {
            Map.Entry entry = (Map.Entry) str;
            stringBuffer.append(entry.getKey().toString());
            stringBuffer.append(speator);
        }

        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }
    //.split("[,|\\s]")
}
