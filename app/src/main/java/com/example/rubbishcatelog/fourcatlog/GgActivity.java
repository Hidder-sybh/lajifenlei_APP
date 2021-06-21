package com.example.rubbishcatelog.fourcatlog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
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

public class GgActivity extends BaseActivity implements View.OnClickListener {
    Button Gs,Gg,Ge,Gc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_gg);
        TextView tv1=findViewById(R.id.news_item_content_text_view);
        String source=getResources().getString(R.string.gg);
        tv1.setText((Html.fromHtml(source)));
        iniButton();
        Gg.setTextColor(getResources().getColor(R.color.white));
        Gg.setBackgroundColor(getResources().getColor(R.color.gray));

    }//页面初始化

    @Override
    public void nextPage() {
        Intent intent = new Intent(this, GeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
//下一页
    @Override
    public void prePage() {
        Intent intent = new Intent(this, GsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
//上一页
    private void iniButton()
    {
        Gs=findViewById(R.id.Gs);
        Gs.setOnClickListener(this);
        Gg=findViewById(R.id.Gg);
        Gg.setOnClickListener(this);
        Ge=findViewById(R.id.Ge);
        Ge.setOnClickListener(this);
        Gc=findViewById(R.id.Gc);
        Gc.setOnClickListener(this);
    }//按钮初始化
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent5=new Intent(this, MainActivity.class);
            startActivity(intent5);
            //点击完返回键，执行的动作

        }
        return true;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.Gs:
                Intent intent4=new Intent(this, GsActivity.class);
                startActivity(intent4);
                break;
            case R.id.Ge:
                Intent intent5=new Intent(this, GeActivity.class);
                startActivity(intent5);
                break;
            case R.id.Gc:
                Intent intent6=new Intent(this, GcActivity.class);
                startActivity(intent6);
                break;
        }
    }
//页面跳转

}