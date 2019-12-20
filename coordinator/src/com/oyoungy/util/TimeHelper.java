package com.oyoungy.util;

import java.util.TimeZone;

public class TimeHelper {
    public static final long MONTH_TIME = 2592000000L;  //1000 * 60 * 60 * 24 * 30
    public static final long WEEK_TIME = 1000 * 60 * 60 * 24 * 7;
    public static final long DAY_TIME = 1000 * 60 * 60 * 24;
    public static final long HOUR_TIME = 1000 * 60 * 60;
    public static final long MINUTE_TIME = 1000 * 60;
    public static final long SECOND_TIME = 1000;

    public static long getZeroStampOfCurrentDay(){
        long now = System.currentTimeMillis();
        return now - (now+ TimeZone.getDefault().getRawOffset())%DAY_TIME; //需要转换时区
    }

    public static void main(String[] args){
        System.out.println(System.currentTimeMillis());
        System.out.println(getZeroStampOfCurrentDay());
    }
}
