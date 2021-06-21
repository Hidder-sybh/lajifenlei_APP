package com.example.rubbishcatelog.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.rubbishcatelog.MainActivity;
import com.example.rubbishcatelog.R;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class CansearchFragment extends Fragment implements View.OnClickListener {

    private CansearchViewModel cansearchViewModel;
    private TextView info;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private EditText editText;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        cansearchViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(CansearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_cansearch, container, false);
        final TextView textView = root.findViewById(R.id.text_mine);
        cansearchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        btn1 = (Button) root.findViewById(R.id.btn1);
        btn2 = (Button) root.findViewById(R.id.btn2);
        btn3 = (Button) root.findViewById(R.id.btn3);
        info = (TextView) root.findViewById(R.id.info);
        editText = (EditText) root.findViewById(R.id.editText1);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        editText.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        String user_input;
        switch (v.getId()) {
            case R.id.btn1:
                new Thread() {
                    public void run() {
                        try {
                            URL url = new URL("http", "101.200.143.70", 11235, "/total_records/");
                            //URL url = new URL("http://www.baidu.com");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(3000);
                            conn.setRequestMethod("GET");
                            int responsecode = conn.getResponseCode();
                            if (responsecode == conn.HTTP_OK) {
                                System.out.println("成功访问该网址");
                                InputStream is = conn.getInputStream();                     // 从服务器获得的is流
                                int len = -1;
                                byte buf[] = new byte[512];                                // 开辟空间
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();   // 捕获内存
                                while ((len = is.read(buf)) != -1) {
                                    baos.write(buf, 0, len);
                                }
                                String temp = new String(baos.toByteArray());
                                System.out.println(temp);
                                info.setText(temp);
                                conn.disconnect();                                        // 关闭输入流
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case R.id.btn2:
                user_input = editText.getText().toString();
                System.out.println(user_input);
                if(user_input.equals("请输入垃圾桶编号")){
                    info.setText("请先输入垃圾桶编号!");
                    break;
                }
                new Thread() {
                    public void run() {
                        OutputStream out = null;
                        StringBuffer buffer = new StringBuffer();
                        try {
                            Map<String,String> param = new HashMap<String,String>();
                            param.put("Can_ID",user_input);
                            buffer.append("Can_ID").append("=").
                                    append(URLEncoder.encode(user_input,"UTF-8"));
                            byte[] mydata = buffer.toString().getBytes();

                            URL url = new URL("http", "101.200.143.70", 11235, "/ID_research/");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(3000);
                            conn.setRequestMethod("POST");
                            conn.setUseCaches(false);
                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            conn.setRequestProperty("Content-Length", String.valueOf(mydata.length));
                            OutputStream outputStream = conn.getOutputStream();
                            outputStream.write(mydata,0,mydata.length);

                            int responsecode = conn.getResponseCode();
                            System.out.println(responsecode);
                            String res = "";
                            if (responsecode == conn.HTTP_OK) {
                                res = changeInputeStream(conn.getInputStream(),"UTF-8");
                                System.out.println(res);
                                info.setText(res);
                            }else{
                                info.setText("连接失败");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case R.id.btn3:
                user_input = editText.getText().toString();
                if(user_input.equals("请输入垃圾桶编号")){
                    info.setText("请先输入垃圾桶编号!");
                    break;
                }
                new Thread() {
                    public void run() {
                        OutputStream out = null;
                        StringBuffer buffer = new StringBuffer();
                        try {
                            Map<String,String> param = new HashMap<String,String>();
                            param.put("Can_ID",user_input);
                            buffer.append("Can_ID").append("=").
                                    append(URLEncoder.encode(user_input,"UTF-8"));
                            byte[] mydata = buffer.toString().getBytes();

                            URL url = new URL("http", "101.200.143.70", 11235, "/Can_check/");
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(3000);
                            conn.setRequestMethod("POST");
                            conn.setUseCaches(false);
                            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                            conn.setRequestProperty("Content-Length", String.valueOf(mydata.length));
                            OutputStream outputStream = conn.getOutputStream();
                            outputStream.write(mydata,0,mydata.length);

                            int responsecode = conn.getResponseCode();
                            System.out.println(responsecode);
                            String res = "";
                            if (responsecode == conn.HTTP_OK) {
                                res = changeInputeStream(conn.getInputStream(),"UTF-8");
                                System.out.println(res);
                                info.setText(res);
                            }else{
                                info.setText("连接失败");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                break;
            case R.id.editText1:
                editText.setText("");
                break;
        }
    }



    /**
     * 将一个输入流转换成字符串
     * @param inputStream
     * @param encode
     * @return
     */
    private static String changeInputeStream(InputStream inputStream,String encode) {
        //通常叫做内存流，写在内存中的
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if(inputStream != null){
            try {
                while((len = inputStream.read(data))!=-1){
                    data.toString();

                    outputStream.write(data, 0, len);
                }
                //result是在服务器端设置的doPost函数中的
                result = new String(outputStream.toByteArray(),encode);
                outputStream.flush();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }
}