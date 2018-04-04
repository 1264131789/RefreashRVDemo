package com.shbj.refreashrvdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shbj.refreashrvdemo.ui.RefreashRecyclerView;

import java.util.ArrayList;

public class Demo1Fragment extends Fragment {

    private final String TAG = "Demo1Fragment";
    private ArrayList<String> mStrings;
    private final int TYPE_REFRESH = 1;
    private final int TYPE_LOAD = 2;
    private final int TYPE_NO_LOAD_MORE = 3;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TYPE_REFRESH:
                    mRv.refreshEnd();//刷新完成后需调用refreshEnd，更新HeaderView显示状态
                    mDemo1Adapter.setDatas(mStrings);
                    //由于自定义RecycleView中使用了自定义的adapter所以数据改变时需调用自定义adapter的notifyDataSetChanged
                    mRv.getAdapter().notifyDataSetChanged();
                    TextView tvHeader = mHeaderView.findViewById(R.id.tv_refresh);
                    LinearLayout llRefreshing = mHeaderView.findViewById(R.id.ll_refreshing);
                    tvHeader.setVisibility(View.VISIBLE);
                    llRefreshing.setVisibility(View.INVISIBLE);
                    break;
                case TYPE_LOAD:
                    mDemo1Adapter.setDatas(mStrings);
                    mRv.getAdapter().notifyDataSetChanged();
                    TextView tvLoad = mFooterView.findViewById(R.id.tv_load);
                    LinearLayout llLoading = mFooterView.findViewById(R.id.ll_loading);
                    tvLoad.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.INVISIBLE);
                    break;
                case TYPE_NO_LOAD_MORE:
                    mRv.removeOnLoadMoreListener();//当没有更多数据时可移除加载更多监听
                    tvLoad = mFooterView.findViewById(R.id.tv_load);
                    llLoading = mFooterView.findViewById(R.id.ll_loading);
                    tvLoad.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.INVISIBLE);
                    tvLoad.setText("没有更多数据了");
                    break;
            }
        }
    };
    private RefreashRecyclerView mRv;
    private Demo1Adapter mDemo1Adapter;
    private View mHeaderView;
    private View mFooterView;
    private int mLoadCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_demo1, null);
        mStrings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {//初始数据
            mStrings.add("条目 " + i);
        }
        mRv = view.findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mHeaderView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_header, mRv, false);
        mRv.setHeaderView(mHeaderView);//设置HeaderView，需在setLayoutManager后设置否则会报错
        mFooterView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_footer, mRv, false);
        mRv.setFooterView(mFooterView);//设置FooterView
        mDemo1Adapter = new Demo1Adapter(getContext(), mStrings);
        mRv.setAdapter(mDemo1Adapter);//设置adapter，照样继承的是RecycleView中自带的adapter
        mRv.setOnRefreshListener(new RefreashRecyclerView.OnRefreshListener() {//设置下拉刷新监听
            @Override
            public void onStartRefresh() {
                TextView tvHeader = mHeaderView.findViewById(R.id.tv_refresh);
                LinearLayout llRefreshing = mHeaderView.findViewById(R.id.ll_refreshing);
                tvHeader.setVisibility(View.INVISIBLE);
                llRefreshing.setVisibility(View.VISIBLE);
                refresh();//监听到开始刷新时调用刷新函数
            }
        });
        mRv.setOnPullDownListener(new RefreashRecyclerView.OnPullDownListener() {//设置下拉过程的监听
            @Override
            public void onPullDownProgress(float progress) {//获取下拉的占比，一些特殊要求可能会用到，比如下面实现了下拉过程中字体逐渐变大
                if (progress > 1) {
                    progress = 1;
                }
                TextView tvHeader = mHeaderView.findViewById(R.id.tv_refresh);
                tvHeader.setTextSize(15 + 20 * progress);
            }
        });
        mRv.setOnLoadMoreListener(new RefreashRecyclerView.OnLoadMoreListener() {//设置上拉加载更多的监听
            @Override
            public void onLoadMoreStart() {
                TextView tvLoad = mFooterView.findViewById(R.id.tv_load);
                LinearLayout llLoading = mFooterView.findViewById(R.id.ll_loading);
                tvLoad.setVisibility(View.INVISIBLE);
                llLoading.setVisibility(View.VISIBLE);
                loadMore();//监听到开始加载时调用加载更多函数
            }
        });
        return view;
    }

    private void refresh() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                mStrings.clear();
                for (int i = 0; i < 50; i++) {
                    mStrings.add("栏目 " + i);
                }
                mHandler.sendEmptyMessage(TYPE_REFRESH);
            }
        }).start();
    }

    private void loadMore() {
        mLoadCount++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                if (mLoadCount == 3) {//模拟没有更多数据
                    mHandler.sendEmptyMessage(TYPE_NO_LOAD_MORE);
                } else {
                    for (int i = 0; i < 20; i++) {
                        mStrings.add("新栏目 " + i);
                    }
                    Log.d(TAG, "run: loadMore");
                    mHandler.sendEmptyMessage(TYPE_LOAD);
                }
            }
        }).start();
    }
}
