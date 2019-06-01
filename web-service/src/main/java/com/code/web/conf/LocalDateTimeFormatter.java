package com.code.web.conf;

import com.code.common.utils.DateUtils;
import org.springframework.format.Formatter;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Locale;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime>, Serializable {
    @Override
    public LocalDateTime parse(String s, Locale locale) throws ParseException {
        LocalDateTime dateTime = DateUtils.parseDateTime(s);
        return dateTime;
    }

    @Override
    public String print(LocalDateTime dateTime, Locale locale) {
        return DateUtils.formatDateTime(dateTime);
    }
}
