package com.example.rubbishcatelog.ui.voice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rubbishcatelog.MainActivity;
import com.example.rubbishcatelog.R;
import com.example.rubbishcatelog.VoiceSelectActivity;
import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.FuzzyArrayAdapter;
import com.example.rubbishcatelog.model.rubbish;
import com.example.rubbishcatelog.network.DriverService;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Util.JsonParser;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import top.androidman.SuperButton;

public class VoiceActivity extends Activity implements View.OnClickListener {
    AutoCompleteTextView tv;
    SuperButton voices,searcches;
    private ListView trush;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_voice);
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5e55bb66");//初始化语音识别模块
        initView();
        final List<Object> listdata = new ArrayList<Object>();
        final List<Object> code = new ArrayList<Object>();
        for (int i = 0; i < CommonData.rubbish.size(); i++) {
            rubbish rub = (rubbish) CommonData.rubbish.get(i);
            listdata.add(rub.getName());
            code.add(rub.getClassID());
        }//获取垃圾信息
        FuzzyArrayAdapter<Object> arrayAdapter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            arrayAdapter = new FuzzyArrayAdapter<Object>(VoiceActivity.this, R.layout.list_item, listdata);
        }//判断系统版本，实现自动填充文本框
        trush.setAdapter(arrayAdapter);
        //设置自动填充文本框
        tv.setAdapter(arrayAdapter);
        tv.setThreshold(1);
        tv.bringToFront();
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                trush.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!tv.getText().toString().isEmpty()) {

                }
            }
        });
       trush.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               tv.setText(listdata.get(position).toString());
           }
       });
       if(getIntent().getStringExtra("selectname")!=null)
       {
          searchx(getIntent().getStringExtra("selectname"));

       }
    }
    private void showTip(String data) {
        Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
    }
    //显示提示
    //初始化监听器
    class MyInitListener implements InitListener {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败 ");
            }

        }
    }
    //初始化页面
    private void initView()
    {
        tv=findViewById(R.id.midSearch);
        trush=findViewById(R.id.trush);
        voices=findViewById(R.id.voiceStart);
        searcches=findViewById(R.id.search_bar);
        voices.setOnClickListener(this);
        searcches.setOnClickListener(this);

    }
    private void startSpeechDialog() {
        //1. 创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2. 设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");// 设置中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后 onResult回调返回将是语义理解
        // 结果

        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4. 显示dialog，接收语音输入
        mDialog.show();
    }
    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param results
         * @param isLast  是否说完了
         */
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String result = results.getResultString(); //为解析的
            System.out.println(" 没有解析的 :" + result);

            String text = JsonParser.parseIatResult(result);//解析过后的
            System.out.println(" 解析后的 :" + text);

            String sn = null;
            // 读取json结果中的 sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);//没有得到一句，添加到

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }

            tv.setText(format(resultBuffer.toString()));// 设置输入框的文本
            tv.setSelection(tv.length());//把光标定位末尾
            Intent intent=new Intent(VoiceActivity.this, VoiceSelectActivity.class);
            intent.putExtra("text",format(resultBuffer.toString()));
            startActivity(intent);
        }
        @Override
        public void onError(SpeechError speechError) {

        }

    }
    public static String format(String s){
        //去掉标点符号
        String str=s.replaceAll("[`qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        return str;
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //点击完返回键，执行的动作
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
    public void searchx(String str) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DriverService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DriverService driverService = retrofit.create(DriverService.class);

        Call<ResponseBody> call = driverService.search(str);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //onResponse方法是运行在主线程也就是UI线程的，所以我们可以在这里直接更新ui
                if (response.isSuccessful()) {
                    try {

                        String string = response.body().string();
                        Log.e("just", "onResponse: " + string);
                        Gson gson  = new Gson();
                        Map map = gson.fromJson(string, Map.class);
                        System.out.println("");
                        Double ClassId=Double.valueOf(map.get("ClassID").toString());
                        String Name=map.get("Name").toString();
                        //返回搜索结果，形成要说的话
                        if(ClassId==1.0)
                        {

                            String str=Name+"是可回收垃圾";
                            speekText(str);//调用语音输出
                            Toast.makeText(VoiceActivity.this,str,Toast.LENGTH_SHORT).show();
                        }
                        else if(ClassId==2.0)
                        {

                            String str=Name+"是干垃圾";
                            speekText(str);
                            Toast.makeText(VoiceActivity.this,str,Toast.LENGTH_SHORT).show();
                        }
                        else if(ClassId==3.0)
                        {

                            String str=Name+"是有害垃圾";
                            speekText(str);
                            Toast.makeText(VoiceActivity.this,str,Toast.LENGTH_SHORT).show();
                        }
                        else if(ClassId==4.0)
                        {

                            String str=Name+"是湿垃圾";
                            speekText(str);
                            Toast.makeText(VoiceActivity.this,str,Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            speekText("对不起，我不知道欸");
                            Toast.makeText(VoiceActivity.this,"对不起，您输入的数据数据库中没有找到",Toast.LENGTH_SHORT).show();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
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
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.voiceStart:
                startSpeechDialog();
                break;
            case R.id.search_bar:
                searchx("xxx");
                break;
        }
    }
    private void speekText( String str) {
        //1. 创建 SpeechSynthesizer 对象 , 第二个参数： 本地合成时传 InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer( this, null);
//2.合成参数设置，详见《 MSC Reference Manual》 SpeechSynthesizer 类
//设置发音人（更多在线发音人，用户可参见 附录 13.2
        mTts.setParameter(SpeechConstant. VOICE_NAME, "vixyun" ); // 设置发音人
        mTts.setParameter(SpeechConstant. SPEED, "50" );// 设置语速
        mTts.setParameter(SpeechConstant. VOLUME, "80" );// 设置音量，范围 0~100
        mTts.setParameter(SpeechConstant. ENGINE_TYPE, SpeechConstant. TYPE_CLOUD); //设置云端
//设置合成音频保存位置（可自定义保存位置），保存在 “./sdcard/iflytek.pcm”
//保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
//仅支持保存为 pcm 和 wav 格式， 如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant. TTS_AUDIO_PATH, "./sdcard/iflytek.pcm" );
//3.开始合成
        mTts.startSpeaking( str, new MySynthesizerListener()) ;

    }

    class MySynthesizerListener implements SynthesizerListener {

        @Override
        public void onSpeakBegin() {
            //showTip(" 开始播放 ");
        }

        @Override
        public void onSpeakPaused() {
          //  showTip(" 暂停播放 ");
        }

        @Override
        public void onSpeakResumed() {
           // showTip(" 继续播放 ");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos ,
                                     String info) {
            // 合成进度
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成 ");
            } else if (error != null ) {
                showTip(error.getPlainDescription( true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1 , int arg2, Bundle obj) {

        }
    }

}
