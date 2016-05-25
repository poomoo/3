/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.commlib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/22 11:09.
 */
public class MyDateFormat {
    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String NOW = "刚刚";
    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "昨天";
    private static final String TWO_DAY_AGO = "前天";
    private static SimpleDateFormat format;


    public static String format(String date) {
        if (date.length() > 10)
            format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
        else
            format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        String yearMonthDay;
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        yearMonthDay = format2.format(date1);
        long delta = new Date().getTime() - date1.getTime();
        if (delta < 1L * ONE_MINUTE) {
            return NOW;
        } else if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        } else if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        } else if (delta < 48L * ONE_HOUR || delta == 48L * ONE_HOUR) {
            return ONE_DAY_AGO;
        } else if (delta < 72L * ONE_HOUR || delta == 72L * ONE_HOUR) {
            return TWO_DAY_AGO;
        } else {
            return yearMonthDay;
        }
//        if (delta < 12L * 4L * ONE_WEEK) {
//            long months = toMonths(delta);
//            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
//        } else {
//            long years = toYears(delta);
//            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
//        }
    }

    public static String formatToMessage(String date) {
        if (date.length() > 10)
            format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        else
            format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        String yearMonthDay;
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        yearMonthDay = format2.format(date1);
        long delta = new Date().getTime() - date1.getTime();
        if (delta > 1L * ONE_DAY) {
            return date;
        } else {
            return yearMonthDay;
        }
    }

    /**
     * @param date1
     * @param date2
     * @return true-大于5分钟 false
     */
    public static boolean minus(String date1, String date2) {
        if (date1.length() == 0)
            return true;
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long delta = d1.getTime() - d2.getTime();
        LogUtils.d("MyDateFormat", "delta-->" + delta);
        if (delta > 5L * ONE_MINUTE) return true;//五分钟内不需要显示
        return false;
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
}
