package com.example.rubbishcatelog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.rubbishcatelog.model.CommonData;

import java.io.File;

import Util.SuperActivity;

public class ContentActivity extends SuperActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.itemcontent);
        Toolbar toolbarx = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarx);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarx.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置标题栏
        Intent intent=getIntent();
        int index=intent.getIntExtra("index",0);
        //获取传递过来的文章信息
        ImageView imageView=findViewById(R.id.articleImg);
        TextView title=findViewById(R.id.articleTitle);
        TextView content=findViewById(R.id.articleContent);
        String url= CommonData.articles.get(index).getImgname();
        File appDir = new File(Environment.getExternalStorageDirectory(), "baiUtil");
        String fileName = url + ".jpg";
        File file = new File(appDir,fileName);
        if(file.exists()){

            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());

            imageView.setImageBitmap(bm);

        }
        title.setText("\u3000"+CommonData.articles.get(index).getTitle());
        content.setText(CommonData.articles.get(index).getContent());
        //页面初始化

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           finish();
            //点击完返回键，执行的动作

        }
        return true;
    }
    //返回键返回上级页面
}
