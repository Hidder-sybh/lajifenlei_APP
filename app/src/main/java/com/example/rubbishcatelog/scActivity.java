package com.example.rubbishcatelog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.FuzzyArrayAdapter;
import com.example.rubbishcatelog.model.rubbish;
import com.example.rubbishcatelog.network.DriverService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Util.MyApplication;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class scActivity extends AppCompatActivity implements View.OnClickListener {
    AutoCompleteTextView searchView;
    private ListView trush;
    final List<Object> listdata = new ArrayList<Object>();
    Button del,search;
    private List<Integer> images = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sc);
        searchView=findViewById(R.id.search_view);
        searchView.setOnClickListener(this);
        trush=findViewById(R.id.trush);
        del=findViewById(R.id.del);
        del.setOnClickListener(this);
        search=findViewById(R.id.sc);
        search.setOnClickListener(this);
        final List<Object> listdata = new ArrayList<Object>();
        final List<Object> code = new ArrayList<Object>();
        for (int i = 0; i < CommonData.rubbish.size(); i++) {
            rubbish rub = (rubbish) CommonData.rubbish.get(i);
            listdata.add(rub.getName());
            code.add(rub.getClassID());
        }
        FuzzyArrayAdapter<Object> arrayAdapter = new FuzzyArrayAdapter<Object>(scActivity.this, R.layout.list_item, listdata);
        trush.setAdapter(arrayAdapter);
        searchView.setAdapter(arrayAdapter);
        searchView.setThreshold(1);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(searchView.getText().toString().isEmpty())
                {
                    trush.setVisibility(View.VISIBLE);
                }
            }
        });
        trush.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                View s=getViewByPosition(position,trush);
                TextView tx=s.findViewById(R.id.it);
                searchView.setText((tx.getText()));
            }
        });
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //点击完返回键，执行的动作
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
    public void searchx() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DriverService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        DriverService driverService = retrofit.create(DriverService.class);
//        Map<String,String> map=new HashMap<>();
//        map.put("class_ID","1");
        Call<ResponseBody> call = driverService.search(searchView.getText().toString());
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
                        if(ClassId==1.0)
                        {
                            ColorDialog dialog = new ColorDialog(scActivity.this);
                            dialog.setTitle(searchView.getText().toString());//标题
                            dialog.setColor("#b0e0e6");
                            dialog.setContentImage(getResources().getDrawable(R.drawable.gc1));//图片（可以没有）
                            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {//设置确定监听
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    Toast.makeText(scActivity.this, dialog.getPositiveText().toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).show();//展示
                        }
                        else if(ClassId==2.0)
                        {
                            ColorDialog dialog = new ColorDialog(scActivity.this);
                            dialog.setTitle(searchView.getText().toString());//标题
                            dialog.setContentImage(getResources().getDrawable(R.drawable.gg1));//图片（可以没有）
                            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {//设置确定监听
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    Toast.makeText(scActivity.this, dialog.getPositiveText().toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).show();//展示
                        }
                        else if(ClassId==3.0)
                        {
                            ColorDialog dialog = new ColorDialog(scActivity.this);
                            dialog.setTitle(searchView.getText().toString());//标题
                            dialog.setContentImage(getResources().getDrawable(R.drawable.ge1));//图片（可以没有）
                            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {//设置确定监听
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    Toast.makeText(scActivity.this, dialog.getPositiveText().toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).show();//展示
                        }
                        else if(ClassId==4.0)
                        {
                            ColorDialog dialog = new ColorDialog(scActivity.this);
                            dialog.setTitle(searchView.getText().toString());//标题
                            dialog.setColor("#808000");//颜色（可以没有）
                            dialog.setContentImage(getResources().getDrawable(R.drawable.gs1));//图片（可以没有）
                            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {//设置确定监听
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    Toast.makeText(scActivity.this, dialog.getPositiveText().toString(), Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            }).show();//展示

                        }
                        else
                        {
                            new PromptDialog(scActivity.this)
                                    .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)//设置样式
                                    .setAnimationEnable(true)//是否有动画
                                    .setTitleText("提示")//标题
                                    .setContentText("对不起，您输入的数据数据库中没有找到")//内容
                                    .setPositiveListener("OK", new PromptDialog.OnPositiveListener() {//设置监听
                                        @Override
                                        public void onClick(PromptDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();//展示

                            Toast.makeText(scActivity.this,"对不起，您输入的数据数据库中没有找到",Toast.LENGTH_SHORT).show();
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
            case R.id.sc:
                searchx();
            case R.id.del:
                searchView.setText("");
        }
    }
    private View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

}
