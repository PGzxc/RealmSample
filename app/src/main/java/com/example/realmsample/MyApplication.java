package com.example.realmsample;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;


/**
 * Created by admin on 2018/1/3.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }
}
