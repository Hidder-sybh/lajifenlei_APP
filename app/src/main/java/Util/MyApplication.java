package Util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.Wrongtask;
import com.example.rubbishcatelog.model.article;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends Application{
    public static MyApplication app;
    private  Context mContext;
    public static List rubbish=new ArrayList();
    private final LinkedList<Activity> mActivitys = new LinkedList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        app = this;
        Fresco.initialize(this);//初始化极光推送
        JPushInterface.setDebugMode(true);//打印log
        JPushInterface.init(this);
        //获取持久化的文章
        if(getSharedPreferences("articles", MODE_PRIVATE)!=null)
        {
            SharedPreferences preferences = getSharedPreferences("articles", MODE_PRIVATE);
            String json = preferences.getString("listStr", null);
            if (json != null)
            {
                Gson gson = new Gson();
                Type type = new TypeToken<List<article>>(){}.getType();
                CommonData.articles = gson.fromJson(json, type);

            }
        }
        //获取持久化的错题
        if(getSharedPreferences("wrongpoints", MODE_PRIVATE)!=null)
        {
            SharedPreferences preferences = getSharedPreferences("wrongpoints", MODE_PRIVATE);
            String json = preferences.getString("listStr", null);
            if (json != null)
            {
                Gson gson = new Gson();
                Type type = new TypeToken<List<Wrongtask>>(){}.getType();
                CommonData.wrongList = gson.fromJson(json, type);

            }
        }
        //获取持久化的积分
        if(getSharedPreferences("points", MODE_PRIVATE)!=null)
        {
            SharedPreferences preferences = getSharedPreferences("points", MODE_PRIVATE);
            int points = preferences.getInt("points",0);//第二个参数为默认值
            CommonData.points=points;
        }


    }


    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        SharedPreferences sp=getSharedPreferences("articles",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//将文章持久化
        Gson gson = new Gson();
        String data = gson.toJson(CommonData.articles);
        editor.putString("listStr", data);
        editor.commit();
    }

}
