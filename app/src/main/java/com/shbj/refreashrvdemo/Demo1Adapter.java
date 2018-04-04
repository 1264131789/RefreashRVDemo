package com.shbj.refreashrvdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Demo1Adapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<String> mDatas;
    public Demo1Adapter(Context context, List<String> datas){
        mContext = context;
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_normal, parent, false);
        RecyclerView.ViewHolder holder = new NormalHolder(view);
        return holder;
    }

    public void setDatas(List<String> datas){
        mDatas=datas;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((NormalHolder)holder).setData(position);
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    class NormalHolder extends RecyclerView.ViewHolder {
        private View mItemView;

        public NormalHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
        }

        public void setData(int postion) {
            ((TextView) mItemView).setText(mDatas.get(postion));
        }
    }
}
