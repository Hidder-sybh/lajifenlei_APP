package com.example.rubbishcatelog.ui.zn;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.rubbishcatelog.R;
import com.example.rubbishcatelog.SearchActivity;
import com.example.rubbishcatelog.fourcatlog.GcFragement;
import com.example.rubbishcatelog.fourcatlog.GeFragement;
import com.example.rubbishcatelog.fourcatlog.GgFragement;
import com.example.rubbishcatelog.fourcatlog.GsFragement;
//暂时弃用
public class ZnFragement extends Fragment implements View.OnClickListener {

    Button Gs,Gg,Ge,Gc;
    GsFragement gsFragement=new GsFragement();
    GgFragement ggFragement=new GgFragement();
    GcFragement gcFragement=new GcFragement();
    GeFragement geFragement=new GeFragement();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragement_zn, container, false);

        Gs=root.findViewById(R.id.Gs);
        Gs.setOnClickListener(this);
        Gg=root.findViewById(R.id.Gg);
        Gg.setOnClickListener(this);
        Ge=root.findViewById(R.id.Ge);
        Ge.setOnClickListener(this);
        Gc=root.findViewById(R.id.Gc);
        Gc.setOnClickListener(this);
        Gs.callOnClick();
        return root;
    }

    @Override
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
        androidx.fragment.app.FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.replace(R.id.f2_content,fragment);
        transaction.commit();
    }
}