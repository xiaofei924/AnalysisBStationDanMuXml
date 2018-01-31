package com.example.analysisxmldemo;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity {
    private ListView mListView;
    private DanMuListViewAdapter mDanMuAdapter;
    private TextView mTestTxt;
    private ArrayList<DanMuBean> mList;
    private int[] rawId = {R.raw.a3910708, R.raw.a4767147, R.raw.a8553542};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initData();
        initUI();
    }

    private void initData() {
        Intent intent = getIntent();
        mList = new ArrayList<DanMuBean>();
        String fileName = intent.getStringExtra(util.FILE_NAME);
        Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
//        fileName = "30540135";

        //1，通过解析本地文件的方式得到所有弹幕
        mList = LocalFile.getData(getResources().openRawResource(rawId[2]),null);

        //2，通过解析远程服务器文件的方式得到所有弹幕
        String url = "https://chat.bilibili.com/" + fileName + ".xml";
//        list = RemoteFile.getData(url);
//        RemoteFile.downAsynFile(fileName, url);
        mDanMuAdapter = new DanMuListViewAdapter(this, mList);
        LocalFile.setOnDataChangelListener(new RemoteFile.onDataChangelListener() {
            @Override
            public void onDataChanged(String videoTime, String text, String sendTime) {

            }

            @Override
            public void onDataChanged(String fileName) {

            }

            @Override
            public void onDataChanged(ArrayList<DanMuBean> list) {
                mList = list;
                Log.d("cjf", "mList:" + mList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDanMuAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        RemoteFile.setOnDataChangelListener(new RemoteFile.onDataChangelListener() {
            @Override
            public void onDataChanged(String videoTime, final String text, String sendTime) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDanMuAdapter.notifyDataSetChanged();
                        mTestTxt.setText(text);
                    }
                });
            }

            @Override
            public void onDataChanged(String fileName) {
                try {
                    Log.d("cjf", "fileName:" + fileName);
                    File file = new File(Environment.getExternalStorageDirectory(), fileName + ".xml");
                    InputStream inputStream = new FileInputStream(file);
                    mList = LocalFile.getData(inputStream, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDanMuAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onDataChanged(ArrayList<DanMuBean> list) {

            }
        });
    }

    private void initUI() {
        mListView = findViewById(R.id.dan_mu_data_lv);
        mListView.setAdapter(mDanMuAdapter);
        mTestTxt = findViewById(R.id.test_txt);
    }

}
