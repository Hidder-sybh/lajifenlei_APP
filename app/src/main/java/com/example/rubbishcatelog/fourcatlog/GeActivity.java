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

public class GeActivity extends BaseActivity implements View.OnClickListener {
    public static boolean work=true;
    Button Gs2,Gg2,Ge2,Gc2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_ge);
        TextView tv1=findViewById(R.id.news_item_content_text_view);
        String source=getResources().getString(R.string.ge);
        tv1.setText((Html.fromHtml(source)));
        iniButton();
        Ge2.setTextColor(getResources().getColor(R.color.white));
        Ge2.setBackgroundColor(getResources().getColor(R.color.red));

    }//页面初始化

    public void nextPage() {
        Intent intent = new Intent(this, GcActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
    }
//下一页
    @Override
    public void prePage() {
        Intent intent = new Intent(this, GgActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
//上一页

    private void iniButton()
    {
        Gs2=findViewById(R.id.Gs2);
        Gs2.setOnClickListener(this);
        Gg2=findViewById(R.id.Gg2);
        Gg2.setOnClickListener(this);
        Ge2=findViewById(R.id.Ge2);
        Ge2.setOnClickListener(this);
        Gc2=findViewById(R.id.Gc2);
        Gc2.setOnClickListener(this);
    }//按钮初始化
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.Gs2:
                Intent intent4=new Intent(this, GsActivity.class);
                startActivity(intent4);
                break;
            case R.id.Gc2:
                Intent intent5=new Intent(this, GcActivity.class);
                startActivity(intent5);
                break;
            case R.id.Gg2:
                Intent intent6=new Intent(this, GgActivity.class);
                startActivity(intent6);
                break;
        }
    }
//页面跳转
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent5=new Intent(this, MainActivity.class);
            startActivity(intent5);
            //点击完返回键，执行的动作

        }
        return true;
    }

}