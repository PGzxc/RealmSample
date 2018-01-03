package com.example.realmsample.async;

import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.example.realmsample.R;
import com.example.realmsample.activity.BaseActivity;
import com.example.realmsample.bean.Cat;
import com.example.realmsample.utils.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmAsyncTask;

public class UpdateCatActivity extends BaseActivity {
    @BindView(R.id.toolBar)
    Toolbar mToolbar;
    @BindView(R.id.et_name)
    EditText etName;

    private Realm mRealm;
    private String mId;
    private RealmAsyncTask updateTask;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_cat;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

    }

    private void initData() {
        setToolbar(mToolbar,"异步更新");
        mId=getIntent().getStringExtra("id");
        mRealm=Realm.getDefaultInstance();
    }

    @OnClick(R.id.btn_update)
    void onClick(View view){
        final String name=etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)){
            ToastUtil.showToast(UpdateCatActivity.this,"请输入新的Name");
            return;
        }

     updateTask=   mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Cat cat=realm.where(Cat.class).equalTo("id",mId).findFirst();
                cat.setName(name);
            }
        }, new Realm.Transaction.Callback(){
         @Override
         public void onSuccess() {
             super.onSuccess();
             ToastUtil.showToast(UpdateCatActivity.this,"更新成功");
             setResult(RESULT_OK);
             finish();
         }

         @Override
         public void onError(Exception e) {
             super.onError(e);
             ToastUtil.showToast(UpdateCatActivity.this,"失败成功");
         }
     });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateTask!=null&&!updateTask.isCancelled()){
            updateTask.cancel();
        }
    }
}
