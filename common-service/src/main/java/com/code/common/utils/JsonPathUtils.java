package com.code.common.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonPathUtils {
    public static Object getValue(String content, String jsonpath) {
        DocumentContext context = (JsonContext) JsonPath.parse(content);

        Object val = context.read(jsonpath);
        return val;
    }

    public static <T> T getObj(String content, String jsonpath, Class<T> cls) {
        DocumentContext context = (JsonContext) JsonPath.parse(content);

        T obj = context.read(jsonpath, cls);
        return obj;
    }

    public static List<String> getValueList(String content, String jsonpath) {
        DocumentContext context = (JsonContext) JsonPath.parse(content);

        List<String> vals = context.read(jsonpath);
        return vals;
    }

    public static <T> List<T> getObjList(String content, String jsonpath, Class<T> cls) {
        DocumentContext context = (JsonContext) JsonPath.parse(content);

        List<Map> maps = context.read(jsonpath);
        List<T> objs = new ArrayList<>();
        try {
            for (Map map : maps) {
                objs.add(BeanUtils.mapToBean(map, cls));
            }
        } catch (Exception e) {
        }
        return objs;
    }

}
