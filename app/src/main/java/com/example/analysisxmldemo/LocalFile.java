package com.example.analysisxmldemo;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by 小飞 on 2018/1/27.
 */

public class LocalFile {
    private static RemoteFile.onDataChangelListener mDataChangelListener;

    public static ArrayList<DanMuBean> getData(InputStream input, String str){
        Log.d("cjf", "LocalFile getData, input:" + input + ", str:" + input);
        ArrayList<DanMuBean> list = new ArrayList<DanMuBean>();
        String videoTime = null;
        String sendDate = null;
        try{
            Document doc = null;
            if (input == null) {
                doc = Jsoup.parse(str);
            } else {
                doc = Jsoup.parse(input, "UTF-8", "https://comment.bilibili.com");
            }
            //每条弹幕的内容都处于<d></d>标签中，于是根据该标签找到所有弹幕
            Elements contents = doc.getElementsByTag("d");

            for (Element content : contents) {
                DanMuBean bean = new DanMuBean();
//                list.add(content.text()); //将<d></d>标签中的文本内容，也就是弹幕内容，添加到list中
//                Log.d("cjf", content.text());
                String[] temp = content.attr("p").split(",");
                //第一个弹幕出现时间
                //第五个弹幕时间基准时间为 1970-1-1 08:00:00
                videoTime = util.SecondFormat(temp[0]);
                sendDate = util.DateFormat(temp[4]);
                bean.content = content.text();
                bean.videoTime = videoTime;
                bean.sendDate = sendDate;
                list.add(bean);
                Log.d("cjf", "videoTime" + videoTime + ", context:"
                        + content.text() + ", sendDate:" + sendDate);
            }
            mDataChangelListener.onDataChanged(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void setOnDataChangelListener(RemoteFile.onDataChangelListener listener) {
        mDataChangelListener = listener;
    }



/*    <d p="0,1,25,16777215,1312863760,0,eff85771,42759017">前排占位置</d>
    这行内容的意义呢
    先说内容“前排站位置”就不解释了
    p这个字段里面的内容：
            0,1,25,16777215,1312863760,0,eff85771,42759017
    中几个逗号分割的数据
    第一个参数是弹幕出现的时间 以秒数为单位。
    第二个参数是弹幕的模式1..3 滚动弹幕 4底端弹幕 5顶端弹幕 6.逆向弹幕 7精准定位 8高级弹幕
    第三个参数是字号， 12非常小,16特小,18小,25中,36大,45很大,64特别大
    第四个参数是字体的颜色 以HTML颜色的十位数为准
    第五个参数是Unix格式的时间戳。基准时间为 1970-1-1 08:00:00
    第六个参数是弹幕池 0普通池 1字幕池 2特殊池 【目前特殊池为高级弹幕专用】
    第七个参数是发送者的ID，用于“屏蔽此弹幕的发送者”功能
    第八个参数是弹幕在弹幕数据库中rowID 用于“历史弹幕”功能。*/
}
