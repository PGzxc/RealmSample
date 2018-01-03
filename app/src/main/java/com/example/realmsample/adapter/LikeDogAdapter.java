package com.example.realmsample.adapter;

import android.content.Context;
import android.view.View;

import com.example.realmsample.R;
import com.example.realmsample.bean.Dog;

import java.util.List;

/**
 */

public class LikeDogAdapter extends BaseAdapter<Dog> {

    public LikeDogAdapter(Context mContext, List<Dog> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, Dog dog) {
        holder.setText(R.id.tv_name, dog.getName())
                .setText(R.id.tv_id,dog.getId())
                .setVisible(R.id.iv_like, View.INVISIBLE);

    }
}
