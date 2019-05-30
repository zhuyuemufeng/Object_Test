package com.itheima.web.coverters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomCoverter implements Converter<String,Date> {

    private String timeFomate;

    public String getTimeFomate() {
        return timeFomate;
    }

    public void setTimeFomate(String timeFomate) {
        this.timeFomate = timeFomate;
    }

    @Override
    public Date convert(String s) {
        if (StringUtils.isEmpty(timeFomate)){
            this.timeFomate = "yyyy-MM-dd";
        }
        DateFormat format = new SimpleDateFormat(timeFomate);
        try {
            return format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("日期解析错误");
        }
    }
}
