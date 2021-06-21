package com.example.rubbishcatelog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubbishcatelog.model.CommonData;
import com.google.gson.Gson;

import top.androidman.SuperButton;

public class RecordActivity extends AppCompatActivity {
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,totalCount;
    SuperButton back;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //初始化界面
        tv1=findViewById(R.id.tv1);
        tv2=findViewById(R.id.tv2);
        tv3=findViewById(R.id.tv3);
        tv4=findViewById(R.id.tv4);
        tv5=findViewById(R.id.tv5);
        tv6=findViewById(R.id.tv6);
        tv7=findViewById(R.id.tv7);
        tv8=findViewById(R.id.tv8);
        tv9=findViewById(R.id.tv9);
        tv10=findViewById(R.id.tv10);
        changbg(tv1,(int)CommonData.arrayList.get(0));
        changbg(tv2,(int)CommonData.arrayList.get(1));
        changbg(tv3,(int)CommonData.arrayList.get(2));
        changbg(tv4,(int)CommonData.arrayList.get(3));
        changbg(tv5,(int)CommonData.arrayList.get(4));
        changbg(tv6,(int)CommonData.arrayList.get(5));
        changbg(tv7,(int)CommonData.arrayList.get(6));
        changbg(tv8,(int)CommonData.arrayList.get(7));
        changbg(tv9,(int)CommonData.arrayList.get(8));
        changbg(tv10,(int)CommonData.arrayList.get(9));
        totalCount=findViewById(R.id.totalcount);
        int sum=0;
        for(Object i:CommonData.arrayList)
        {
            sum+= Integer.parseInt(i.toString());//获取总分
        }
        totalCount.setText("总分为："+sum);
        CommonData.points+=sum;
        SharedPreferences sp=getSharedPreferences("wrongpoints",MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();//分数持久化
        Gson gson = new Gson();
        String data = gson.toJson(CommonData.wrongList);
        editor.putString("listStr", data);
        editor.commit();
        SharedPreferences spx=getSharedPreferences("points",MODE_PRIVATE);
        SharedPreferences.Editor editorx=spx.edit();
        editorx.putInt("points",CommonData.points);
        editorx.commit();//分数持久化
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecordActivity.this, MainActivity.class);
                startActivity(intent);//跳转到主页面
            }
        });

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //点击完返回键，执行的动作
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
    private  void changbg( TextView tv,int i)
    {
        if(i==0)
            tv.setBackgroundColor(this.getResources().getColor(R.color.lightpink));
        else
            tv.setBackgroundColor(this.getResources().getColor(R.color.aquamarine));
    }//根据题目对错换背景

}
