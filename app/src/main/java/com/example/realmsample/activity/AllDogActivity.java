package com.example.realmsample.activity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import com.example.realmsample.R;
import com.example.realmsample.adapter.BaseAdapter;
import com.example.realmsample.adapter.LikeDogAdapter;
import com.example.realmsample.bean.Dog;
import com.example.realmsample.utils.DefaultItemTouchHelpCallback;
import com.example.realmsample.utils.RealmHelper;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

/**
 *  查询所有
 */
public class AllDogActivity extends BaseActivity {
    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<Dog> mDogs = new ArrayList<>();
    private LikeDogAdapter mAdapter;
    private RealmHelper mRealmHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar(mToolbar, "查询所有");
        initData();
        addListener();
    }

    private void initData() {
        mRealmHelper = new RealmHelper(this);
        mDogs = mRealmHelper.queryAllDog();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new LikeDogAdapter(this, mDogs, R.layout.item_dog);
        mRecyclerView.setAdapter(mAdapter);
        setSwipeDelete();
        Snackbar.make(mRecyclerView,"滑动删除item、点击Item进入修改界面",Snackbar.LENGTH_LONG).show();

    }

    private void setSwipeDelete() {
        DefaultItemTouchHelpCallback mCallback = new DefaultItemTouchHelpCallback(new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                //删除数据库数据
                mRealmHelper.deleteDog(mDogs.get(adapterPosition).getId());
                //滑动删除
                mDogs.remove(adapterPosition);
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

    private void addListener() {
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(AllDogActivity.this,UpdateActivity.class);
                intent.putExtra("id",mDogs.get(position).getId());
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_all_dog;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==100){
            mDogs.clear();
            List<Dog> dogs=mRealmHelper.queryAllDog();
            mDogs.addAll(dogs);
            mAdapter.notifyDataSetChanged();
        }
    }
}
