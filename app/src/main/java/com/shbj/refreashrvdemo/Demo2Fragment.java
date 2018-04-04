package com.shbj.refreashrvdemo;

/*
 *  @项目名：  RefreashRVDemo 
 *  @包名：    com.shbj.refreashrvdemo
 *  @文件名:   Demo2Fragment
 *  @创建者:   shenbinjian
 *  @创建时间:  2018/4/4 21:32
 *  @描述：    TODO
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Demo2Fragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView=new TextView(getContext());
        textView.setText("待更新。。。");
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        return textView;
    }
}
