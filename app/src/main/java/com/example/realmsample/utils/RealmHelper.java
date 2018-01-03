package com.example.realmsample.utils;

import android.content.Context;
import com.example.realmsample.bean.Dog;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by admin on 2018/1/3.
 */

public class RealmHelper {
    public static  final String DB_NAME="myRealm.realm";
    private Realm mRealm;

    public RealmHelper(Context context){
        mRealm=Realm.getDefaultInstance();
    }
    /**
     * add(增)
     */
    public void addDog(final Dog dog){
        mRealm.beginTransaction();
        mRealm.copyToRealm(dog);
        mRealm.commitTransaction();
    }
    /**
     * delete(删除)
     */
    public void deleteDog(String id){
        Dog dog = mRealm.where(Dog.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        dog.removeFromRealm();
        mRealm.commitTransaction();
    }
    /**
     * update(该)
     */
    public  void updateDog(String id,String newName){
        Dog dog = mRealm.where(Dog.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        dog.setName(newName);
        mRealm.commitTransaction();
    }
    /**
     * qurey(查询所有)
     */
     public List<Dog> queryAllDog(){
         RealmResults<Dog> dogs = mRealm.where(Dog.class).findAll();
         //对查询结果，按照id进行排序，只能对查询结果进行排序
         //增序排列
          dogs.sort("id");
          //降序排列
         //dogs.sort("id", Sort.DESCENDING)
         return mRealm.copyFromRealm(dogs);
     }
    /**
     * query(根据id(主键)查)
     */
    public Dog queryDogById(String id){
        Dog dog = mRealm.where(Dog.class).equalTo("id", id).findFirst();
        return dog;
    }
    /**
     * query(根据age查)
     */
    public List<Dog> queryDogByAge(int age){
        RealmResults<Dog> dogs = mRealm.where(Dog.class).equalTo("age", age).findAll();
        return mRealm.copyFromRealm(dogs);
    }
    /**
     * 根据id查是否存在
     */
    public  boolean isDogExist(String id){
        Dog dog = mRealm.where(Dog.class).equalTo("id", id).findFirst();
        if(dog==null){
            return false;
        }else{
            return true;
        }
    }
    /**
     * 获取realm
     */
    public Realm getmRealm(){
        return mRealm;
    }
     /**
     * 关闭realm
     */
    public void close(){
        if(mRealm!=null){
            mRealm.close();
        }
    }
}
