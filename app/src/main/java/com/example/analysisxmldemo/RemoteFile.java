package com.example.analysisxmldemo;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class RemoteFile {
    static ArrayList<DanMuBean> mDanMuBeanList = null;
    static OkHttpClient mOkHttpClient = new OkHttpClient();
    private static onDataChangelListener mDataChangelListener;

    public static ArrayList<DanMuBean> getData(String url) {
        mDanMuBeanList = new ArrayList<DanMuBean>();

        Request.Builder requestBuilder = new Request.Builder().url(url);
        //可以省略，默认是GET请求
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        Call mcall = mOkHttpClient.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {

                String bodyContent = null;
                Document doc = null;
                String videoTime = null;
                String sendDate = null;
                InputStream inputStream = null;
                if (response.isSuccessful()) {
                    if (null != response.cacheResponse()) {
                        String str = response.cacheResponse().toString();
                        Log.i("cjf", "cache---" + str);
                    } else {
                        try {
                            byte[] bytes = response.body().bytes();
                            bodyContent = new String(bytes, "GB2312");
                            inputStream = new ByteArrayInputStream(bodyContent.getBytes());
//                        mDanMuBeanList = LocalFile.getData(null, content);

                            String netUrlStr = response.networkResponse().toString();
//                    doc = Jsoup.parse(bodyContent);
                            doc = Jsoup.parse(inputStream, "UTF-8", "https://comment.bilibili.com");
                            Log.i("cjf", "network---" + netUrlStr + ", doc:" + doc);
                            //每条弹幕的内容都处于<d></d>标签中，于是根据该标签找到所有弹幕
                            Elements contents = doc.getElementsByTag("d");
                            Log.d("cjf", "contents.size():" + String.valueOf(contents.size()));
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
                                mDanMuBeanList.add(bean);
                                Log.d("cjf", "videoTime" + videoTime + ", context:"
                                        + content.text() + ", sendDate:" + sendDate);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                mDataChangelListener.onDataChanged("", bodyContent, "");
            }
        });

        return mDanMuBeanList;
    }

    static void downAsynFile(final String fileName, String url) {
        mOkHttpClient = new OkHttpClient();
//        String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        Request request = new Request.Builder().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), fileName + ".xml"));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    Log.i("cjf", "IOException");
                    e.printStackTrace();
                }
                Log.d("cjf", "文件下载成功");
                mDataChangelListener.onDataChanged(fileName);
            }
        });
    }

    public static void setOnDataChangelListener(onDataChangelListener listener) {
        mDataChangelListener = listener;
    }

    public interface onDataChangelListener {
        void onDataChanged(String videoTime, String text, String sendTime);

        void onDataChanged(String fileName);
        void onDataChanged(ArrayList<DanMuBean> list);
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
