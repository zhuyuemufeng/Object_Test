package com.itheima.web.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLEncodeUtils {
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try
        {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }

        return "";
    }

    public static String toURLDecoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try
        {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLDecoder.decode(str, "UTF-8");
            return str;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }

        return "";
    }
}
