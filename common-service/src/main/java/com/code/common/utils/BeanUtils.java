package com.code.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public class BeanUtils {
    private static Logger logger = LoggerFactory.getLogger(BeanUtils.class);

    public static <T> T mapToBean(Map map, Class<T> cls) {
        if (map == null || cls == null) {
            return null;
        }
        T obj = null;
        try {
            obj = cls.newInstance();
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                Field field = cls.getDeclaredField(key.toString());
                field.setAccessible(true);
                if (field.getType() == LocalDateTime.class || field.getType() == Date.class) {
                    field.set(obj, DateUtils.parseDateTimeByJson(value.toString()));
                } else {
                    field.set(obj, value);
                }
            }
        } catch (Exception e) {
            logger.error("beanUtils error:msg:{}", e);
        }
        return obj;
    }
}
