package com.example.realmsample.adapter;

import android.content.Context;
import android.view.View;
import com.example.realmsample.R;
import com.example.realmsample.bean.Cat;
import java.util.List;


public class LikeCatAdapter extends BaseAdapter<Cat> {

    public LikeCatAdapter(Context mContext, List<Cat> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
    }
    @Override
    protected void convert(Context mContext, BaseViewHolder holder, Cat cat) {
        holder.setText(R.id.tv_name, cat.getName())
                .setText(R.id.tv_id,cat.getId())
                .setVisible(R.id.iv_like, View.INVISIBLE);
    }
}
