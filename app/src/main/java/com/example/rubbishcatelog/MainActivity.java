package com.example.rubbishcatelog;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rubbishcatelog.fourcatlog.GgActivity;
import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.article;
import com.example.rubbishcatelog.model.rubbish;
import com.example.rubbishcatelog.network.DriverService;
import com.example.rubbishcatelog.ui.image.ImageActivity;
import com.example.rubbishcatelog.ui.voice.VoiceActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import Util.ImageAndText;
import Util.ImageViewAdapter;
import Util.MyApplication;
import Util.QAAdapter;
import Util.SaveImageUtils;
import Util.SuperActivity;
import cn.refactor.lib.colordialog.PromptDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends SuperActivity implements View.OnClickListener {

    CommonData commonData;
    private List<ImageAndText> onePieceList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(commonData.articles.size()!=0)
        {
            article articles=commonData.articles.get(0);//???????????????????????????????????????????????????????????????????????????????????????????????????
            if(!articles.isWork())
            {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy???MM???dd??? HH:mm:ss");// HH:mm:ss
//??????????????????
                Date date = new Date(System.currentTimeMillis());
                SaveImageUtils.saveImage(articles.getUrl(),simpleDateFormat.format(date), MainActivity.this);
                Log.e("err1",articles.getUrl());
                articles.setImgname(simpleDateFormat.format(date));
                Log.e("err",articles.getImgname());
                Toast.makeText(MainActivity.this,"?????????",Toast.LENGTH_SHORT).show();
                articles.setWork(true);
                SharedPreferences sp=getSharedPreferences("articles",MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                //????????????????????????
                Gson gson = new Gson();
                String data = gson.toJson(CommonData.articles);
                editor.putString("listStr", data);
                editor.commit();
            }
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//???????????????
        BottomNavigationView navView = findViewById(R.id.nav_view);//????????????????????????
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_CanStatus, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
            addingData();
        ImageViewAdapter imageViewAdapter=new ImageViewAdapter(this,R.layout.imageview_list,onePieceList);
        ListView listView = (ListView)findViewById(R.id.selectlist); //??????????????????Listview
        listView.setAdapter(imageViewAdapter);
        TextView tv=findViewById(R.id.point);
        tv.setText(CommonData.points+"");
        initPermission();//??????????????????
       // CommonData.rubbish.clear();
        handler.postDelayed(runnable, 5000);//5????????????????????????????????????
        LinearLayout qa=findViewById(R.id.qaaz);
        qa.setOnClickListener(this);
        LinearLayout share=findViewById(R.id.share);
        share.setOnClickListener(this);
        LinearLayout call=findViewById(R.id.nav_call);
        call.setOnClickListener(this);
        LinearLayout renew=findViewById(R.id.nav_renew);
        renew.setOnClickListener(this);
    }
    private long mExitTime;

public void addingData()//????????????????????????????????????
{

    ImageAndText b=new ImageAndText("??????",R.drawable.nav_task);
    onePieceList.add(b);



}
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // ???Flag??????????????????????????????????????????????????????????????????????????????????????????Q???????????????????????????????????????????????????????????????    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // ????????????Q???????????????????????????
            return false;
        }
    }//?????????QQ????????????
    @Override
protected void onDestroy() {
    super.onDestroy();
}
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "??????????????????RBlong", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {

                Intent intent = new Intent();
                intent.setAction(SuperActivity.SYSTEM_EXIT);
                sendBroadcast(intent);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finishAffinity();
                System.exit(0);

            }

        }
    return false;
    }//????????????
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if(commonData.articles.size()!=0)
            {
                article articles=commonData.articles.get(0);
                if(!articles.isWork())
                {
                    //????????????????????????????????????????????????????????????????????????????????????
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy???MM???dd??? HH:mm:ss");// HH:mm:ss
//??????????????????
                    Date date = new Date(System.currentTimeMillis());
                    SaveImageUtils.saveImage(articles.getUrl(),simpleDateFormat.format(date), MainActivity.this);
                    articles.setImgname(simpleDateFormat.format(date));
                    Toast.makeText(MainActivity.this,"?????????",Toast.LENGTH_SHORT).show();
                    articles.setWork(true);
                    SharedPreferences sp=getSharedPreferences("articles",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sp.edit();
                    Gson gson = new Gson();
                    String data = gson.toJson(CommonData.articles);
                    editor.putString("listStr", data);
                    editor.commit();
                    //
                }
            }
            //???????????????
            handler.postDelayed(this, 5000);
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qaaz:
                Intent intent = new Intent(this, QAActivity.class);
                startActivity(intent);//?????????????????????
                break;
            case R.id.share://??????????????????
                Intent intentx = new Intent(Intent.ACTION_SEND);
                intentx.setType("image/*");
                intentx.putExtra(Intent.EXTRA_SUBJECT, "Share");
                intentx.putExtra(Intent.EXTRA_TEXT, "I have successfully share my message through my app");
                intentx.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intentx, getTitle()));
                break;
                //?????????????????????
            case R.id.nav_call:
                if (isQQClientAvailable(this)) {
                    joinQQGroup("cKp0yMaHlggUeFsHoncGAOK7xhEkdwn-");
                    }



                break;
            case R.id.nav_renew://??????????????????
                new PromptDialog(this)
                        .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                        .setAnimationEnable(true)
                        .setTitleText("??????")
                        .setContentText("???????????????????????????")
                        .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();

                break;

        }


    }
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }//????????????????????????
    String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECORD_AUDIO

            //,Manifest.permission.REQUEST_INSTALL_PACKAGES    //??????????????????????????????????????????
    };
    //2???????????????mPermissionList??????????????????????????????????????????????????????????????????mPerrrmissionList???
    List<String> mPermissionList = new ArrayList<>();
    private final int mRequestCode = 100;//???????????????
    private void initPermission() {

        mPermissionList.clear();//???????????????????????????

        //?????????????????????????????????????????????
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//???????????????????????????
            }
        }
        //????????????
        if (mPermissionList.size() > 0) {//????????????????????????????????????
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        }else{
            //????????????????????????????????????????????????????????????
        }
    }

    public void onStart() {

        super.onStart();
        MyReceiver myReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,filter);
    }//????????????????????????

}
