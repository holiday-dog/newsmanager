package com.code.common.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;

import java.util.List;

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
}
