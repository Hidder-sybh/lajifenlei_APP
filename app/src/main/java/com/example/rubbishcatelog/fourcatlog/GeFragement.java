package com.example.rubbishcatelog.fourcatlog;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.rubbishcatelog.R;
//暂时弃用
public class GeFragement extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragement_ge, container, false);
        TextView tv1=root.findViewById(R.id.news_item_content_text_view);
        String source=getResources().getString(R.string.ge);
        tv1.setText((Html.fromHtml(source)));
        return root;
    }
}
