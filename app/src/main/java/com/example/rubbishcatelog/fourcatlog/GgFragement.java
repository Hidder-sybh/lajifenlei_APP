package com.example.rubbishcatelog.fourcatlog;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rubbishcatelog.R;

import java.util.ArrayList;
import java.util.List;

import Util.ImageAndText;
import Util.ImageViewAdapter;
//暂时弃用
public class GgFragement extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragement_gg, container, false);
        TextView tv1=root.findViewById(R.id.news_item_content_text_view);
        String source=getResources().getString(R.string.gg);
        tv1.setText((Html.fromHtml(source)));


        return root;
    }


}
