package com.example.realmsample.async;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import com.example.realmsample.R;
import com.example.realmsample.activity.BaseActivity;
import com.example.realmsample.adapter.BaseAdapter;
import com.example.realmsample.adapter.LikeCatAdapter;
import com.example.realmsample.bean.Cat;
import com.example.realmsample.utils.DefaultItemTouchHelpCallback;
import com.example.realmsample.utils.ToastUtil;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class AsyncQueryActivity extends BaseActivity {
    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private Realm mRealm;
    private List<Cat> mCats = new ArrayList<>();
    private LikeCatAdapter mAdapter;
    private RealmResults<Cat> cats;
    private RealmAsyncTask deleteTask;

    @Override
    public int getLayoutId() {
        return R.layout.activity_async_query;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(mToolbar, "异步查、改");
        mRealm = Realm.getDefaultInstance();
        initRecyclerView();
        getData();
        addListener();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new LikeCatAdapter(this, mCats, R.layout.item_dog);
        mRecyclerView.setAdapter(mAdapter);
        setSwipeDelete();
    }


    private void getData() {
        cats = mRealm.where(Cat.class).findAllAsync();
//        cats.addChangeListener(new RealmChangeListener<RealmResults<Cat>>() {
//            @Override
//            public void onChange(RealmResults<Cat> element) {
//                Log.i("TAG", "111111111");
//                element.sort("id");
//                List<Cat> datas = mRealm.copyFromRealm(element);
//                mCats.clear();
//                mCats.addAll(datas);
//                mAdapter.notifyDataSetChanged();
//            }
//        });
       cats.addChangeListener(new RealmChangeListener() {
           @Override
           public void onChange() {
               cats.sort("id");
               List<Cat> cats = mRealm.copyFromRealm(AsyncQueryActivity.this.cats);
               mCats.clear();
               mCats.addAll(cats);
               mAdapter.notifyDataSetChanged();
           }
       });
    }

    private void addListener() {
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(AsyncQueryActivity.this, UpdateCatActivity.class);
                intent.putExtra("id", mCats.get(position).getId());
                startActivityForResult(intent, 100);
            }
        });
    }


    private void setSwipeDelete() {
        DefaultItemTouchHelpCallback mCallback = new DefaultItemTouchHelpCallback(new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                deleteCat(mCats.get(adapterPosition).getId());
                mCats.remove(adapterPosition);
                mAdapter.notifyItemRemoved(adapterPosition);
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {

                return false;
            }
        });
        mCallback.setDragEnable(false);
        mCallback.setSwipeEnable(true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void deleteCat(final String id) {

        deleteTask = mRealm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                Cat cat = realm.where(Cat.class).equalTo("id", id).findFirst();

                if (cat != null) {
                    cat.removeFromRealm();
                }

            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                ToastUtil.showToast(AsyncQueryActivity.this, "删除成功");
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                ToastUtil.showToast(AsyncQueryActivity.this, "删除失败");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            mCats.clear();
            getData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cats.removeChangeListeners();
        if (deleteTask != null && !deleteTask.isCancelled()) {
            deleteTask.cancel();
        }
    }
}
