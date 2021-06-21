package com.example.rubbishcatelog.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.rubbishcatelog.ExamActivity;
import com.example.rubbishcatelog.R;
import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.article;
import com.example.rubbishcatelog.ui.image.ImageActivity;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import Util.SaveImageUtils;
import top.androidman.SuperButton;

import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment {
//答题主页
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        SuperButton stExam=root.findViewById(R.id.examStart);
//页面初始化
        stExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(CommonData.rubbish.size()>1) {

                    Toast.makeText(getActivity(),"准备考试",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), ExamActivity.class);
                    intent.putExtra("num", 1);
                    getActivity().finish();
                    startActivity(intent);
                    //跳转页面
                }
                else
                {
                    Toast.makeText(getActivity(),"请检查网络",Toast.LENGTH_SHORT).show();

                }
            }
        });
        return root;
    }



}