package com.example.analysisxmldemo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 小飞 on 2018/1/27.
 */

public class DanMuListViewAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<DanMuBean> mDanMuDataList;
    public DanMuListViewAdapter(Context context) {
        mContext = context;
    }
    public DanMuListViewAdapter(Context context, ArrayList<DanMuBean> list) {
        mContext = context;
        mDanMuDataList = list;
    }

    @Override

    public int getCount() {
        return mDanMuDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDanMuDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        if (convertView == null) {
            holder = new viewHolder();
            convertView = View.inflate(mContext, R.layout.item_layout, null);
            holder.videoTime = (TextView) convertView.findViewById(R.id.video_time_txt);
            holder.content = (TextView) convertView.findViewById(R.id.content_txt);
            holder.sendDate = (TextView) convertView.findViewById(R.id.send_date_txt);
            convertView.setTag(holder);
        } else {
            holder = (viewHolder) convertView.getTag();
        }
        DanMuBean bean = mDanMuDataList.get(position);
        holder.videoTime.setText(bean.videoTime);
        holder.content.setText(bean.content);
        holder.content.setSelected(true);
        holder.sendDate.setText(bean.sendDate);
        return convertView;
    }

    class viewHolder {
        TextView videoTime;
        TextView content;
        TextView sendDate;
    }

}
