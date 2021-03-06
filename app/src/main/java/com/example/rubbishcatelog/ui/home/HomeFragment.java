package com.example.rubbishcatelog.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageView;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.rubbishcatelog.MainActivity;
import com.example.rubbishcatelog.R;
import com.example.rubbishcatelog.fourcatlog.GcActivity;
import com.example.rubbishcatelog.fourcatlog.GcFragement;
import com.example.rubbishcatelog.fourcatlog.GeActivity;
import com.example.rubbishcatelog.fourcatlog.GeFragement;
import com.example.rubbishcatelog.fourcatlog.GgActivity;
import com.example.rubbishcatelog.fourcatlog.GgFragement;
import com.example.rubbishcatelog.fourcatlog.GsActivity;
import com.example.rubbishcatelog.fourcatlog.GsFragement;
import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.FuzzyArrayAdapter;
import com.example.rubbishcatelog.model.article;
import com.example.rubbishcatelog.model.rubbish;
import com.example.rubbishcatelog.network.DriverService;
import com.example.rubbishcatelog.scActivity;
import com.example.rubbishcatelog.ui.image.ImageActivity;
import com.example.rubbishcatelog.ui.voice.VoiceActivity;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import Util.GlideImageLoader;
import Util.SaveImageUtils;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener {

    String codex;
    private HomeViewModel homeViewModel;
    ImageView imgS,voiS,sc;
    private com.youth.banner.Banner banner;
    private ArrayList<String> list_path =new ArrayList<>();
    private GlideImageLoader glideImageLoader;//??????Gif??????
    ImageView Gs,Gg,Ge,Gc;

    TextView searchView;
    LinearLayout lv;
    private ListView trush;
    final List<Object> listdata = new ArrayList<Object>();
    View root;
    private List<Integer> images = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        if(CommonData.work==1)
        {
            refreshList();
            CommonData.work=0;
        }//????????????
        images.add(R.drawable.s1);
        images.add(R.drawable.s2);
        GifImageView gifImageView = root.findViewById(R.id.gif_iamge_view);
        GifDrawable gifDrawable = (GifDrawable) gifImageView.getDrawable();
        gifDrawable.setLoopCount(0);
        //????????????
        lv=root.findViewById(R.id.t1);
        lv.setOnClickListener(this);
        imgS=root.findViewById(R.id.imgStaet);
        voiS=root.findViewById(R.id.voiStart);
        imgS.setOnClickListener(this);
        voiS.setOnClickListener(this);
        trush=root.findViewById(R.id.trush);
        sc=root.findViewById(R.id.sc);
        sc.setOnClickListener(this);
        Gs=root.findViewById(R.id.gs);
        Gg=root.findViewById(R.id.gg);
        Ge=root.findViewById(R.id.ge);
        Gc=root.findViewById(R.id.gc);
        Gs.setOnClickListener(this);
        Gg.setOnClickListener(this);
        Gc.setOnClickListener(this);
        Ge.setOnClickListener(this);
        //???????????????
        ImageButton opendraw=root.findViewById(R.id.openssl);
        DrawerLayout drawerLayout=getActivity().findViewById(R.id.container);
        opendraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });//????????????????????????
        initView(root);
         searchView=root.findViewById(R.id.search_view);
        searchView.setOnClickListener(this);

        return root;
    }

    /**
     * ????????????
     */
    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }
    public void onResume() {

        super.onResume();

    }


    /**
     * ????????????
     */
    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }//?????????????????????
    public void refreshList() {
        CommonData.rubbish.clear();
        //???????????????
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DriverService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DriverService driverService = retrofit.create(DriverService.class);

        Call<ResponseBody> call = driverService.getall(0);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //onResponse????????????????????????????????????UI???????????????????????????????????????????????????ui
                if (response.isSuccessful()) {
                    try {

                        String string = response.body().string();
                        Gson gson  = new Gson();
                        Map map = gson.fromJson(string, Map.class);

                        for(Object key:map.keySet())
                        {
                            try {
                                LinkedTreeMap<String,String> linkedTreeMap=(LinkedTreeMap<String, String>)map.get(key);
                                String s = String.valueOf( linkedTreeMap.get("CLassID"));
                                String ss=s.substring(0,1);
                                System.out.println();
                                if(linkedTreeMap.get("Name").isEmpty())
                                continue;
                                rubbish rub=new rubbish(Integer.parseInt(ss),linkedTreeMap.get("Name"));
                                System.out.println("");
                                CommonData.rubbish.add(rub);
                                //??????retrofit??????????????????
                            }catch (Exception e)
                            {
                                System.out.println(e.getMessage());
                            }

                        }
                        List<Map<String,Object>> tempList=( List<Map<String,Object>>)map.get("rows");


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {

                    }
                }
                else {
                    Log.e("just", "onFailure: " );

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("just", "onFailure: " + t.getMessage());
            }

        });
    }

    private void initView(View view) {
        glideImageLoader = new GlideImageLoader();
        banner = (Banner) view.findViewById(R.id.banner);
        banner = view.findViewById(R.id.banner);

        //??????banner??????
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //?????????????????????
        banner.setImageLoader(glideImageLoader);
        //??????????????????
        banner.setImages(images);
        //??????????????????
        banner.setDelayTime(5000);
        //banner?????????????????????????????????????????????
        banner.start();
        //????????????????????????
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgStaet:
                Intent intent=new Intent(getActivity(), ImageActivity.class);getActivity(). finish();

                startActivity(intent);
                break;

            case R.id.voiStart:
                Intent intent2=new Intent(getActivity(), VoiceActivity.class);getActivity(). finish();
                startActivity(intent2);
                break;
            case R.id.sc:

            case R.id.gc:
                Intent intent3=new Intent(getActivity(), GcActivity.class);getActivity(). finish();
                startActivity(intent3);
                break;
            case R.id.gs:
                Intent intent4=new Intent(getActivity(), GsActivity.class);getActivity(). finish();
                startActivity(intent4);
                break;
            case R.id.ge:
                Intent intent5=new Intent(getActivity(), GeActivity.class);getActivity(). finish();
                startActivity(intent5);
                break;
            case R.id.gg:
                Intent intent6=new Intent(getActivity(), GgActivity.class);
                startActivity(intent6);
                break;
                //????????????
            case R.id.search_view:
                alpha();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //??????3s???????????????????????????????????????
                Intent intent7=new Intent(getActivity(), scActivity.class);getActivity(). finish();
                startActivity(intent7);
                break;
        }


    }
    public void alpha() {
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        AlphaAnimation alphaAni = new AlphaAnimation(1, 0.2f);
        TranslateAnimation translateAni = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT,
                0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, -1f);

        //?????????????????????????????????????????????
        alphaAni.setDuration(1800);
        translateAni.setDuration(1800);
        // ?????????????????????????????????????????????true?????????????????????????????????

        // true?????????????????????????????????

        // ?????????????????????Animation.REVERSE??????????????????????????????,Animation.RESTART????????????????????????
        alphaAni.setRepeatMode(Animation.REVERSE);
        AnimationSet as = new AnimationSet(false);
        as.addAnimation(alphaAni);
        as.addAnimation(translateAni);
        // ????????????
        lv.startAnimation(as);
    }
}