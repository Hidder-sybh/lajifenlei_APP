package com.example.rubbishcatelog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.FuzzyArrayAdapter;
import com.example.rubbishcatelog.model.rubbish;
import com.example.rubbishcatelog.ui.voice.VoiceActivity;

import java.util.ArrayList;
import java.util.List;

import Util.VoiceItem;
import Util.VoiceItemAdapter;

public class VoiceSelectActivity extends AppCompatActivity {
   EditText tv;
    private ListView trush;
    Button back;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);
        initView();//视图初始化
        Intent intent=getIntent();
        String str=intent.getStringExtra("text");//获取传递过来的Intent的附加信息
        tv.setText(str);
        final List<VoiceItem> listdata = new ArrayList<>();
        for (int i = 0; i < CommonData.rubbish.size(); i++) {
            rubbish rub = (rubbish) CommonData.rubbish.get(i);
            if(rub.getName().contains(str))
            {
                VoiceItem item=new VoiceItem();
                item.setItem(rub.getName());
                item.setCatelog(rub.getClassID()+"");
                listdata.add(item);
            }

        }//将之前获取到的垃圾信息放入listdata中
       VoiceItemAdapter arrayAdapter = new VoiceItemAdapter(VoiceSelectActivity.this, R.layout.result_voiceitem, listdata);
        trush.setAdapter(arrayAdapter);
        trush.setClickable(true);
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!tv.getText().toString().isEmpty()) {
                    listdata.clear();
                    //文字改变时，更新listdata
                    for (int i = 0; i < CommonData.rubbish.size(); i++) {
                        rubbish rub = (rubbish) CommonData.rubbish.get(i);
                        if(rub.getName().contains(tv.getText().toString()))
                        {
                            VoiceItem item=new VoiceItem();
                            item.setItem(rub.getName());
                            item.setCatelog(rub.getClassID()+"");
                            listdata.add(item);
                        }
                        VoiceItemAdapter arrayAdapter = new VoiceItemAdapter(VoiceSelectActivity.this, R.layout.result_voiceitem, listdata);
                        trush.setAdapter(arrayAdapter);

                    }
                }
                else
                {
                    for (int i = 0; i < CommonData.rubbish.size(); i++) {
                        rubbish rub = (rubbish) CommonData.rubbish.get(i);
                            //获取其全部数据
                            VoiceItem item=new VoiceItem();
                            item.setItem(rub.getName());
                            item.setCatelog(rub.getClassID()+"");
                            listdata.add(item);


                    }

                    VoiceItemAdapter arrayAdapter = new VoiceItemAdapter(VoiceSelectActivity.this, R.layout.result_voiceitem, listdata);
                    trush.setAdapter(arrayAdapter);
                }
            }
        });
        trush.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VoiceItem item=listdata.get(position);
                Intent intent1=new Intent(VoiceSelectActivity.this,VoiceActivity.class);
                intent1.putExtra("selectname",item.getItem());
                intent1.putExtra("selectCatelog",item.getCatelog());
                startActivity(intent1);//传递选择的数据到上一页面中

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });//返回上一页面
    }
    private void initView()
    {
        tv=findViewById(R.id.midSearch);
        trush=findViewById(R.id.trush);
        back=findViewById(R.id.back);


    }
}
