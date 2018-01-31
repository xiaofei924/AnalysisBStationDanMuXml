package com.example.analysisxmldemo;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

/**
 * Created by 小飞 on 2018/1/27.
 */

public class DanMuBean {
    String videoTime;
    String content;
    String sendDate;

    public String toString() {
        try {
            return new String((videoTime + " | " + sendDate + " | " + content).getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
