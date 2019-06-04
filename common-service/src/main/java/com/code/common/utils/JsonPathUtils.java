package com.code.common.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.internal.JsonContext;
import net.minidev.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonPathUtils {
    public static String getValue(String content, String jsonpath) {
        try {
            DocumentContext context = (JsonContext) JsonPath.parse(content);

            String val = context.read(jsonpath);
            return val;
        } catch (Exception e) {
            return null;
        }
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

    public static List<Map<String, Object>> getMapList(String content, String jsonpath) {
        DocumentContext context = (JsonContext) JsonPath.parse(content);

        List<Map<String, Object>> maps = context.read(jsonpath);
        return maps;
    }


    public static String jsonArray(String content, String jsonpath) {
        DocumentContext context = (JsonContext) JsonPath.parse(content);

        try {
            JSONArray jsonArray = context.read(jsonpath);
            if (jsonArray == null) {
                return null;
            }
            return jsonArray.toJSONString();
        } catch (PathNotFoundException e) {
            return null;
        }
    }

}
