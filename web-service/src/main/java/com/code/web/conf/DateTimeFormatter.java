package com.code.web.conf;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;


/**
 * Timestamp注解格式化工厂
 * <p>
 * TimestampFormatAnnotationFormatterFactory
 *
 * @author 潘广伟
 * @version 1.0.0
 * @Email p_3er@qq.com
 * @Date 2015-1-28 上午10:20:47
 */
public class DateTimeFormatter implements AnnotationFormatterFactory<LocalDateConverter> {
    private final Set<Class<?>> fieldTypes;
    private final LocalDateTimeFormatter formatter;

    public DateTimeFormatter() {
        Set<Class<?>> set = new HashSet<Class<?>>();
        set.add(Timestamp.class);
        this.fieldTypes = set;
        this.formatter = new LocalDateTimeFormatter();
    }


    public Set<Class<?>> getFieldTypes() {
        return fieldTypes;
    }

    @Override
    public Printer<?> getPrinter(LocalDateConverter localDateConverter, Class<?> aClass) {
        return formatter;
    }

    @Override
    public Parser<?> getParser(LocalDateConverter localDateConverter, Class<?> aClass) {
        return formatter;
    }

//    @Override
//    public Printer<?> getPrinter(LocalDateTimeFormatter localDateTimeFormatter, Class<?> aClass) {
//        return formatter;
//    }
//
//    @Override
//    public Parser<?> getParser(LocalDateTimeFormatter localDateTimeFormatter, Class<?> aClass) {
//        return formatter;
//    }


}
