package com.code.common.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;

import java.util.List;
import java.util.Map;

public class JsonPathUtils {
    public static Object getValue(String content, String jsonpath) {
        DocumentContext context = (JsonContext) JsonPath.parse(content);

        Object val = context.read(jsonpath);
        return val;
    }

    public static List<String> getValueList(String content, String jsonpath) {
        DocumentContext context = (JsonContext) JsonPath.parse(content);

        List<String> vals = context.read(jsonpath);
        return vals;
    }

    public static List<Map<String, String>> getObjList(String content, String jsonpath) {
        DocumentContext context = (JsonContext) JsonPath.parse(content);

        List<Map<String, String>> maps = context.read(jsonpath);
        return maps;
    }
}
