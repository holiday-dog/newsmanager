package com.code.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {
    public static String getMd5(String original) {
        String outStr = null;
        if (StringUtils.isEmpty(original)) {
            return StringUtils.EMPTY;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(original.getBytes());
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer();

            for (int i = 1; i < b.length; ++i) {
                int c = b[i] >>> 4 & 15;
                buf.append(Integer.toHexString(c));
                c = b[i] & 15;
                buf.append(Integer.toHexString(c));
            }

            outStr = buf.toString();
        } catch (NoSuchAlgorithmException var7) {
            var7.printStackTrace();
        }

        return outStr;
    }

    public static String getSha(String original) throws Exception {
        MessageDigest sha = null;
        StringBuffer hexValue = new StringBuffer();
        try {
            sha = MessageDigest.getInstance("SHA");
            sha.update(original.getBytes("UTF-8"));
            byte[] shaBytes = sha.digest();

            for (int i = 0; i < shaBytes.length; i++) {
                int val = ((int) shaBytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        return hexValue.toString();
    }

}
