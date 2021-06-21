package com.example.rubbishcatelog.ui.zn;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rubbishcatelog.R;
import com.example.rubbishcatelog.fourcatlog.GcFragement;
import com.example.rubbishcatelog.fourcatlog.GeFragement;
import com.example.rubbishcatelog.fourcatlog.GgFragement;
import com.example.rubbishcatelog.fourcatlog.GsFragement;

public class ZnActivity extends AppCompatActivity implements View.OnClickListener {
    //指南页显示和跳转
    Button Gs,Gg,Ge,Gc;
    GsFragement gsFragement=new GsFragement();
    GgFragement ggFragement=new GgFragement();
    GcFragement gcFragement=new GcFragement();
    GeFragement geFragement=new GeFragement();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_zn);
        Gs=findViewById(R.id.Gs);
        Gs.setOnClickListener(this);
        Gg=findViewById(R.id.Gg);
        Gg.setOnClickListener(this);
        Ge=findViewById(R.id.Ge);
        Ge.setOnClickListener(this);
        Gc=findViewById(R.id.Gc);
        Gc.setOnClickListener(this);
        Gs.callOnClick();
    }
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.Gs:
                replaceFragement(gsFragement);
                Gs.setTextColor(getResources().getColor(R.color.white));
                Gs.setBackgroundColor(getResources().getColor(R.color.olive));
                Ge.setTextColor(getResources().getColor(R.color.black));
                Ge.setBackgroundColor(getResources().getColor(R.color.trans));
                Gg.setTextColor(getResources().getColor(R.color.black));
                Gg.setBackgroundColor(getResources().getColor(R.color.trans));
                Gc.setTextColor(getResources().getColor(R.color.black));
                Gc.setBackgroundColor(getResources().getColor(R.color.trans));

                break;
            case R.id.Ge:
                replaceFragement(geFragement);
                Ge.setTextColor(getResources().getColor(R.color.white));
                Ge.setBackgroundColor(getResources().getColor(R.color.red));
                Gs.setTextColor(getResources().getColor(R.color.black));
                Gs.setBackgroundColor(getResources().getColor(R.color.trans));
                Gg.setTextColor(getResources().getColor(R.color.black));
                Gg.setBackgroundColor(getResources().getColor(R.color.trans));
                Gc.setTextColor(getResources().getColor(R.color.black));
                Gc.setBackgroundColor(getResources().getColor(R.color.trans));
                break;
            case R.id.Gc:
                replaceFragement(gcFragement);
                Gc.setTextColor(getResources().getColor(R.color.white));
                Gc.setBackgroundColor(getResources().getColor(R.color.powderblue));
                Gs.setTextColor(getResources().getColor(R.color.black));
                Gs.setBackgroundColor(getResources().getColor(R.color.trans));
                Gg.setTextColor(getResources().getColor(R.color.black));
                Gg.setBackgroundColor(getResources().getColor(R.color.trans));
                Ge.setTextColor(getResources().getColor(R.color.black));
                Ge.setBackgroundColor(getResources().getColor(R.color.trans));
                break;
            case R.id.Gg:
                replaceFragement(ggFragement);
                Gg.setTextColor(getResources().getColor(R.color.white));
                Gg.setBackgroundColor(getResources().getColor(R.color.gray));
                Gs.setTextColor(getResources().getColor(R.color.black));
                Gs.setBackgroundColor(getResources().getColor(R.color.trans));
                Ge.setTextColor(getResources().getColor(R.color.black));
                Ge.setBackgroundColor(getResources().getColor(R.color.trans));
                Gc.setTextColor(getResources().getColor(R.color.black));
                Gc.setBackgroundColor(getResources().getColor(R.color.trans));
                break;
        }
    }
    //
    private void replaceFragement(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.f2_content,fragment);
        transaction.commit();
    }
}
