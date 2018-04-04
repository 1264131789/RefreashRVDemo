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
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TYPE_REFRESH:
                    mRv.refreshEnd();
                    mCustomView6Adapter1.setDatas(mStrings);
                    mRv.getAdapter().notifyDataSetChanged();
                    TextView tvHeader=mHeaderView.findViewById(R.id.tv_refresh);
                    LinearLayout llRefreshing = mHeaderView.findViewById(R.id.ll_refreshing);
                    tvHeader.setVisibility(View.VISIBLE);
                    llRefreshing.setVisibility(View.INVISIBLE);
                    break;
                case TYPE_LOAD:
                    mCustomView6Adapter1.setDatas(mStrings);
                    mRv.getAdapter().notifyDataSetChanged();
                    TextView tvLoad = mFooterView.findViewById(R.id.tv_load);
                    LinearLayout llLoading = mFooterView.findViewById(R.id.ll_loading);
                    tvLoad.setVisibility(View.VISIBLE);
                    llLoading.setVisibility(View.INVISIBLE);
                    break;
            }

        }
    };
    private RefreashRecyclerView mRv;
    private Demo1Adapter mCustomView6Adapter1;
    private View mHeaderView;
    private View mFooterView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_demo1, null);
        mStrings = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mStrings.add("条目 " + i);
        }
        mRv = view.findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(getContext()));
        mRv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mHeaderView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_header, mRv, false);
        mRv.setHeaderView(mHeaderView);
        mFooterView = LayoutInflater.from(this.getContext()).inflate(R.layout.item_footer, mRv, false);
        mRv.setFooterView(mFooterView);
        mCustomView6Adapter1 = new Demo1Adapter(getContext(), mStrings);
        mRv.setAdapter(mCustomView6Adapter1);
        mRv.setOnRefreshListener(new RefreashRecyclerView.OnRefreshListener() {
            @Override
            public void onStartRefresh() {
                TextView tvHeader=mHeaderView.findViewById(R.id.tv_refresh);
                LinearLayout llRefreshing = mHeaderView.findViewById(R.id.ll_refreshing);
                tvHeader.setVisibility(View.INVISIBLE);
                llRefreshing.setVisibility(View.VISIBLE);
                refresh();
            }
        });
        mRv.setOnPullDownListener(new RefreashRecyclerView.OnPullDownListener() {
            @Override
            public void onPullDownProgress(float progress) {
                if (progress>1){
                    progress=1;
                }
                TextView tvHeader=mHeaderView.findViewById(R.id.tv_refresh);
                tvHeader.setTextSize(15+20*progress);
            }
        });
        mRv.setOnLoadMoreListener(new RefreashRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMoreStart() {
                TextView tvLoad = mFooterView.findViewById(R.id.tv_load);
                LinearLayout llLoading = mFooterView.findViewById(R.id.ll_loading);
                tvLoad.setVisibility(View.INVISIBLE);
                llLoading.setVisibility(View.VISIBLE);
                loadMore();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(3000);
                for (int i = 0; i < 20; i++) {
                    mStrings.add("新栏目 " + i);
                }
                Log.d(TAG, "run: loadMore");
                mHandler.sendEmptyMessage(TYPE_LOAD);
            }
        }).start();
    }
}
