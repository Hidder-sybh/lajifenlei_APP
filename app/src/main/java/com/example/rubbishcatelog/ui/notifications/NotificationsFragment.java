package com.example.rubbishcatelog.ui.notifications;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.example.rubbishcatelog.ContentActivity;
import com.example.rubbishcatelog.MainActivity;
import com.example.rubbishcatelog.R;
import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.article;
import com.example.rubbishcatelog.network.DriverService;
import com.example.rubbishcatelog.network.dailynote;
import com.example.rubbishcatelog.scActivity;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import Util.ContentAdapter;
import Util.HttpUtil;
import Util.SaveImageUtils;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment {
//通知页面
    private NotificationsViewModel notificationsViewModel;
    ImageView ivOneImg;
    TextView tvOneWord,tvOneWordFrom,tvImgAuthor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        if (CommonData.sentense!=null&&!CommonData.sentense.isEmpty())//判断每日一句是否为空
        textView.setText(CommonData.sentense);//加载每日一句
        ListView listView=root.findViewById(R.id.contentListview);
        ContentAdapter adapter=new ContentAdapter(getContext(),R.layout.contentitem, CommonData.articles);
        listView.setAdapter(adapter);//初始化文章
        TextView day=root.findViewById(R.id.days);
        TextView monthandyear=root.findViewById(R.id.monthandyear);
        Calendar calendar = Calendar.getInstance();
//获取系统的日期
//年
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        int dayx = calendar.get(Calendar.DAY_OF_MONTH);
//月
        int month = calendar.get(Calendar.MONTH)+1;
        day.setText(dayx+"");
        //day.setText(dayx);
        monthandyear.setText(nowMonth(month)+".2020");
        ivOneImg=root.findViewById(R.id.iv_one_img);
        tvOneWord=root.findViewById(R.id.tv_one_word);
        tvOneWordFrom=root.findViewById(R.id.tv_one_word_from);
        tvImgAuthor=root.findViewById(R.id.tv_img_author);
        //initOneContent();
        initContent();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), ContentActivity.class);
                intent.putExtra("index",position);
                startActivity(intent);
                //跳转到文章页面


            }
        });
        return root;
    }
    //数字月份转英文显示
    private String nowMonth(int month)
    {
        switch (month)
        {
            case 1:return "Jan";
            case 2:return "Feb";
            case 3:return "Mar";
            case 4:return "Apr";
            case 5:return "May";
            case 6:return "Jun";
            case 7:return "Jul";
            case 8:return "Oct";
            case 9:return "Sep";
            case 10:return "Oct";
            case 11:return "Nov";
            case 12:return "Dec";
            default:return null;

        }
    }


    //加载金山词霸每日一句的内容

    public void initContent() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(dailynote.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        dailynote driverService = retrofit.create(dailynote.class);
//        Map<String,String> map=new HashMap<>();
//        map.put("class_ID","1");
        retrofit2.Call<ResponseBody> call = driverService.getall("61e55813e49c0495c8386626bd85875e");
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                //onResponse方法是运行在主线程也就是UI线程的，所以我们可以在这里直接更新ui
                if (response.isSuccessful()) {
                    try {
                        String string = response.body().string();
                        Gson gson  = new Gson();
                        Map map = gson.fromJson(string, Map.class);
                        ArrayList newslist=new ArrayList();
                        newslist= (ArrayList) map.get("newslist");
                        LinkedTreeMap linkedTreeMap= (LinkedTreeMap) newslist.get(0);
                        Glide.with(getActivity()).load(linkedTreeMap.get("imgurl")).into(ivOneImg);//下载图像
                        tvOneWord.setText(""+linkedTreeMap.get("word"));
                        tvOneWordFrom.setText(""+linkedTreeMap.get("wordfrom"));
                        tvImgAuthor.setText(""+linkedTreeMap.get("imgauthor"));
                        //获取到json格式的数据，并加以显示
                        Log.e("sss","sdsa");



                    } catch (Exception e ) {
                        e.printStackTrace();
                    }


                }
                else {
                    Log.e("just", "onFailure: " );

                }
            }
            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.e("just", "onFailure: " + t.getMessage());
            }

        });
    }


}