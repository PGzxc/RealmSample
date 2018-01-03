package com.example.realmsample.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.example.realmsample.R;
import com.example.realmsample.bean.Cat;
import com.example.realmsample.utils.ToastUtil;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

/**
 */

public class AsyncCatAdapter extends BaseAdapter<Cat> {
    private Realm mRealm;
    private Context mContext;
    private RealmAsyncTask addTask;
    private RealmAsyncTask deleteTask;

    public AsyncCatAdapter(Context context, List<Cat> mDatas, int mLayoutId) {
        super(context, mDatas, mLayoutId);
        mContext = context;
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    protected void convert(Context mContext, BaseViewHolder holder, final Cat cat) {
        holder.setText(R.id.tv_id, cat.getId())
                .setText(R.id.tv_name, cat.getName());
        final ImageView imageView = holder.getView(R.id.iv_like);
        if (isLiked(cat.getId())) {
            imageView.setSelected(true);
        } else {
            imageView.setSelected(false);
        }

        holder.setOnClickListener(R.id.iv_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageView.isSelected()) {
                    deleteCat(cat.getId(), imageView);
                } else {
                    addCat(cat, imageView);
                }
            }
        });

    }
    private boolean isLiked(String id) {
        Cat cat = mRealm.where(Cat.class).equalTo("id", id).findFirst();
        if (cat == null) {
            return false;
        } else {
            return true;
        }
    }

    private void addCat(final Cat cat, final ImageView imageView) {
        addTask = mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(cat);
            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                ToastUtil.showToast(mContext, "收藏成功");
            }
            @Override
            public void onError(Exception e) {
                super.onError(e);
                ToastUtil.showToast(mContext, "收藏失败");
            }
        });

    }

    private void deleteCat(final String id, final ImageView imageView) {
        deleteTask = mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Cat cat = realm.where(Cat.class).equalTo("id", id).findFirst();
                cat.removeFromRealm();

            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                ToastUtil.showToast(mContext, "取消收藏成功");
                imageView.setSelected(false);
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                ToastUtil.showToast(mContext, "取消收藏失败");
            }
        });
    }

    public void CancelTask() {
        if (addTask != null && !addTask.isCancelled()) {
            addTask.cancel();
        }
        if (deleteTask != null && !deleteTask.isCancelled()) {
            deleteTask.cancel();
        }
    }
}
