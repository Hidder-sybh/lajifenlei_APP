package com.example.rubbishcatelog;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.Wrongtask;
import com.example.rubbishcatelog.ui.image.ImageActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import Util.MyApplication;
import Util.SuperActivity;
import top.androidman.SuperButton;

@SuppressLint("Registered")
public class ExamActivity extends SuperActivity implements View.OnClickListener {
    Button A,B,C,D;
    SuperButton submit;
    boolean work=true;
    String result;
    String answer="";
    String answer2;
    TextView tc;
    TextView qs;
    List<String> list;
    List<Map<String, String>> mlist;
    int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        list = new ArrayList<String>();
        list.add("干垃圾");
        list.add("湿垃圾");
        list.add("有害垃圾");
        list.add("可回收物");
        //答案集合
        Intent preCal=getIntent();
        num=preCal.getIntExtra("num",0);
        //获取题目信息
        TextView numx=findViewById(R.id.numx);
        qs=findViewById(R.id.qs);
        CommonData commonData=new CommonData();
         mlist=commonData.getMapList();
        qs.setText("\n\u3000\u3000"+mlist.get(num).get("问题"));
        result=mlist.get(num).get("答案");
        //题目初始化
        Toolbar toolbarx = (Toolbar) findViewById(R.id.toolbar);
        toolbarx.setTitle("");
        setSupportActionBar(toolbarx);
        numx.setText(num+"");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarx.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ExamActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        //设置返回键跳转到主页
        submit=findViewById(R.id.submit);
        submit.setOnClickListener(this);
        A=findViewById(R.id.A);
        A.setOnClickListener(this);
        B=findViewById(R.id.B);
        B.setOnClickListener(this);
        C=findViewById(R.id.C);
        C.setOnClickListener(this);
        D=findViewById(R.id.D);
        D.setOnClickListener(this);
        tc=findViewById(R.id.feedback);
        RandomButton();
        //将四个按钮随机放置
    }
    @Override
    public void onClick(View v) {
    switch (v.getId())
    {
        case R.id.A:setButtonStyle1(A,list.get(0));answer=list.get(0);answer2="A";break;//选择A选项
        case R.id.B:setButtonStyle1(B,list.get(1));answer=list.get(1);answer2="B";;break;//选择B选项
        case R.id.C:setButtonStyle1(C,list.get(2));answer=list.get(2);answer2="C";break;//选择C选项
        case R.id.D:setButtonStyle1(D,list.get(3));answer=list.get(3);answer2="D";;break;//选择D选项
        case R.id.submit:
            if(work)
            {
                try {
                    setButtonStyle2();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }//设置点击后按钮样式
            else
            {
                Intent intent;
                if(num<=9) {


                    intent= new Intent(ExamActivity.this, ExamActivity.class);
                    intent.putExtra("num", 1 + num);

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }//为下一题做准备
                }
                else {
                    intent= new Intent(ExamActivity.this, RecordActivity.class);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }//跳转到结算页面
                startActivity(intent);
            }

    }
    }
    public void setButtonStyle1(Button button,String answer)
    {
        A.setEnabled(false);B.setEnabled(false);C.setEnabled(false);D.setEnabled(false);
        if(answer.equals("干垃圾"))
            button.setBackground(getDrawable(R.drawable.tubiao3));
        if(answer.equals("湿垃圾"))
            button.setBackground(getDrawable(R.drawable.tubiao1));
        if(answer.equals("有害垃圾"))
            button.setBackground(getDrawable(R.drawable.tubiao4));
        if(answer.equals("可回收物"))
            button.setBackground(getDrawable(R.drawable.tubiao2));

        submit.setVisibility(View.VISIBLE);
        submit.setText("提交");
    }//设置按钮样式函数
    public void setButtonStyle2() throws NoSuchFieldException, IllegalAccessException {
        if(result.equals(answer))
        {
            String select=answer2;
            int view_id = R.id.class.getField(select).getInt(null);
            Button bth=findViewById(view_id);
            if(answer.equals("干垃圾"))
            bth.setBackground(getDrawable(R.drawable.ggy));
            if(answer.equals("湿垃圾"))
                bth.setBackground(getDrawable(R.drawable.gsy));
            if(answer.equals("有害垃圾"))
                bth.setBackground(getDrawable(R.drawable.gey));
            if(answer.equals("可回收物"))
                bth.setBackground(getDrawable(R.drawable.gcy));
            bth.setTextColor(getResources().getColor(R.color.green));
            CommonData.arrayList.add(1);

        }//设置答对按钮背景
        else
        {
            String select=answer2;
            int view_id = R.id.class.getField(select).getInt(null);
            Button bth=findViewById(view_id);
            bth.setTextColor(getResources().getColor(R.color.red));
            if(answer.equals("干垃圾"))
                bth.setBackground(getDrawable(R.drawable.ggn));
            if(answer.equals("湿垃圾"))
                bth.setBackground(getDrawable(R.drawable.gsn));
            if(answer.equals("有害垃圾"))
                bth.setBackground(getDrawable(R.drawable.gen));
            if(answer.equals("可回收物"))
                bth.setBackground(getDrawable(R.drawable.gcn));            tc.setText("正确答案："+result);
                tc.setBackgroundColor(getResources().getColor(R.color.lightpink));

            Wrongtask wrongtask=new Wrongtask();
            wrongtask.setQus(mlist.get(num).get("问题"));
            wrongtask.setAns(result);
            CommonData.wrongList.add(wrongtask);

            CommonData.arrayList.add(0);
        }//设置答错按钮背景
        submit.setText("下一题");
        work=false;

    }
    private void RandomButton()
    {

        Collections.shuffle(list);
        A.setText("");
        B.setText("");
        C.setText("");
        D.setText("");
        setButtonBg(A,list.get(0));
        setButtonBg(B,list.get(1));
        setButtonBg(C,list.get(2));
        setButtonBg(D,list.get(3));

    }//随机按钮函数
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent=new Intent(ExamActivity.this, MainActivity.class);
            startActivity(intent);
            //点击完返回键，执行的动作

        }
        return true;
    }//设置返回键返回主页
    public void setButtonBg(Button b,String s)
    {
        if(s.equals("干垃圾"))
            b.setBackground(getDrawable(R.drawable.gg1));
        if(s.equals("湿垃圾"))
            b.setBackground(getDrawable(R.drawable.gs1));
        if(s.equals("有害垃圾"))
            b.setBackground(getDrawable(R.drawable.ge1));
        if(s.equals("可回收物"))
            b.setBackground(getDrawable(R.drawable.gc1));
    }//设置按钮背景
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
