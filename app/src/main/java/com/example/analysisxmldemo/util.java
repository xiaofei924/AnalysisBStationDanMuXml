package com.example.analysisxmldemo;

import android.support.annotation.IntegerRes;

import java.nio.FloatBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.DataFormatException;

/**
 * Created by 小飞 on 2018/1/27.
 */

public class util {
    public static final String FILE_NAME = "fileName";

    /**
     *
     * @param str 传入秒
     * @return返回00：00的分：秒格式的时间字符串
     */
    public static String SecondFormat(String str) {
        float totalSecond;
        totalSecond = Float.valueOf(str);
        int min = (int) (totalSecond / 60);
        int second = (int) (totalSecond - min * 60);
        return (min < 10 ? ("0" + min) : min) + ":" + (second < 10 ? ("0" + second) : second);
    }

    /**
     *
     * @param str 传入时间字符串
     * @return 返回日期字符串(1970-1-1 08:00:00)
     */
    public static String DateFormat(String str) {
        long time = Long.valueOf(str);
//        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format=new SimpleDateFormat("MM-dd HH:mm");
        String date = format.format(new Date(time));
        return date;
    }
}
