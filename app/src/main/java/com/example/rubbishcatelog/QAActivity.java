package com.example.rubbishcatelog;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.Wrongtask;

import java.util.ArrayList;
import java.util.List;

import Util.ImageAndText;
import Util.ImageViewAdapter;
import Util.QAAdapter;

public class QAActivity extends AppCompatActivity {
    private List<Wrongtask> onePieceList = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        //错题初始化
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa);
        onePieceList=CommonData.wrongList;
        QAAdapter imageViewAdapter=new QAAdapter(this,R.layout.qa, onePieceList);
        ListView listView = (ListView)findViewById(R.id.qsa); //将适配器导入Listview
        listView.setAdapter(imageViewAdapter);
    }
}
