package com.code.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class IOUtils {
    private static Logger logger = LoggerFactory.getLogger(IOUtils.class);

    public static byte[] getBytesByInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        int n;
        byte[] bus = new byte[2048];

        while ((n = inputStream.read(bus)) != -1) {
            boas.write(bus, 0, n);
        }
        return boas.toByteArray();
    }

    public static String getStringByInputStream(InputStream inputStream, Charset charset) throws IOException {
        try {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int n;
            byte[] bus = new byte[2048];
            StringBuffer stringBuffer = new StringBuffer();

            while ((n = bis.read(bus)) != -1) {
                stringBuffer.append(new String(bus, 0, n, charset));
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            logger.error("file not exist");
            return null;
        }
    }

    public static String getStringByInputStream(InputStream inputStream) throws IOException {
        return getStringByInputStream(inputStream, Charset.defaultCharset());
    }

    public static String stringByResource(String path, Charset charset) throws IOException {
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        InputStream inputStream = IOUtils.class.getClassLoader().getResourceAsStream(path);
        return getStringByInputStream(inputStream, charset);
    }
}
