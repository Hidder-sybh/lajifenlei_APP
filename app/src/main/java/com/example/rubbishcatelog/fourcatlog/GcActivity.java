package com.example.rubbishcatelog.fourcatlog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubbishcatelog.MainActivity;
import com.example.rubbishcatelog.R;

import java.util.ArrayList;
import java.util.List;

import Util.BaseActivity;
import Util.ImageAndText;
import Util.ImageViewAdapter;
import Util.MyApplication;

public class GcActivity extends BaseActivity implements View.OnClickListener {
    Button Gs,Gg,Ge,Gc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_gc);
        TextView tv1=findViewById(R.id.news_item_content_text_view);
        String source=getResources().getString(R.string.gc);
        tv1.setText((Html.fromHtml(source)));
        iniButton();
        Gc.setTextColor(getResources().getColor(R.color.white));
        Gc.setBackgroundColor(getResources().getColor(R.color.powderblue));
        //页面初始化
    }

    public void nextPage() {
        Intent intent = new Intent(this, GsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    //下一页动画
    }


    @Override
    public void prePage() {
        Intent intent = new Intent(this, GeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        //上一页动画
    }

    private void iniButton()
    {
        Gs=findViewById(R.id.Gs1);
        Gs.setOnClickListener(this);
        Gg=findViewById(R.id.Gg1);
        Gg.setOnClickListener(this);
        Ge=findViewById(R.id.Ge1);
        Ge.setOnClickListener(this);
        Gc=findViewById(R.id.Gc1);
        Gc.setOnClickListener(this);
    }//按钮初始化
    @Override
    public void onClick(View v) {
    switch (v.getId())
    {

        case R.id.Gs1:
            Intent intent4=new Intent(this, GsActivity.class);
            startActivity(intent4);
            break;
        case R.id.Ge1:
            Intent intent5=new Intent(this, GeActivity.class);
            startActivity(intent5);
            break;
        case R.id.Gg1:
            Intent intent6=new Intent(this, GgActivity.class);
            startActivity(intent6);
            break;
    }//页面跳转
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent5=new Intent(this, MainActivity.class);
            startActivity(intent5);
            //点击完返回键，执行的动作

        }
        return true;
    }
}
