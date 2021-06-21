package com.example.rubbishcatelog.ui.image;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.rubbishcatelog.MainActivity;
import com.example.rubbishcatelog.R;
import com.example.rubbishcatelog.VoiceSelectActivity;
import com.example.rubbishcatelog.model.CommonData;
import com.example.rubbishcatelog.model.rubbish;
import com.example.rubbishcatelog.network.DriverService;
import com.example.rubbishcatelog.scActivity;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import Util.PermissionUtil;
import Util.VoiceItem;
import Util.VoiceItemAdapter;
import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import top.androidman.SuperButton;

public class ImageActivity extends Activity implements View.OnClickListener {
    PermissionUtil permissionUtil = new PermissionUtil();
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 6;
    private ImageView frontImage;
    private View mProgressView;
    private View VehicleView;
    private ImageView currentImageView; //当前该设置那张图片
    private String currentFilename;   //当前图片名
    private Uri imageUri;
    private Uri imageFileUri;
    private Button frontButton;
    private Button licenseChooseButton;
    private String fullFilename;
    private SuperButton upload;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragement_image);
        mProgressView=findViewById(R.id.login_progress);
        VehicleView=findViewById(R.id.vehicle);
        frontButton = findViewById(R.id.frontButton);
        frontButton.setOnClickListener(this);
        licenseChooseButton = findViewById(R.id.licenseChooseButton);
        licenseChooseButton.setOnClickListener(this);
        upload=findViewById(R.id.upload);
        upload.setClickable(false);//未拍照不可点击上传
        upload.setOnClickListener(this);
        frontImage = findViewById(R.id.ivFront);
        permissionUtil.setActivity(this);//权限管理
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.frontButton:
                currentImageView = frontImage;
                currentFilename = "li.jpg";
                takephoto();
                upload.setClickable(true);//解禁上传按钮
                break;//进行拍照
            case R.id.licenseChooseButton:
                openAlbum();
                upload.setClickable(true);//解禁上传按钮
                break;
            case R.id.upload:
                if(!fullFilename.isEmpty())
                {
                   pic();
                }//上传

        }

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //点击完返回键，执行的动作
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
    private void takephoto() {

        permissionUtil.requestPermission(Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA);
        //权限赋予
        File cacheDir = getExternalFilesDir("image");
        Log.i("just", cacheDir == null ? "null" : cacheDir.toString());
        File outputImage = new File(cacheDir, currentFilename );
        imageFileUri = Uri.fromFile(outputImage);
        //初始化文件
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT > 24) {
            imageUri = FileProvider.getUriForFile(this,
                    "com.zjdt.driver.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
        //调用拍照活动
    }
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CHOOSE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    handleImageOnKitKat(data);
                }
                break;//接受到选择图像完成，调用图像处理
            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    fullFilename = imageFileUri.getPath();
                    getimage(fullFilename);
                    displayImage(fullFilename);


                }//接收到拍照完成，在图像框中显示图像
        }

    }
    //获取到图像的路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                }
                cursor.close();
            }
        }
        return path;
    }

    private void handleImageOnKitKat(Intent data)
    {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        //根据uri的权限及模式获取图像路径，并改名
        final File cacheDir = getExternalFilesDir("image");
        currentFilename = "li"  + ".jpg";
        final String destFilename = new File(cacheDir, currentFilename).getAbsolutePath();
        fullFilename=imagePath;
        displayImage(imagePath);
        //显示图像
    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            frontImage.setImageBitmap(bitmap);

        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }//在图像框中显示图像

    public ImageView getView() {
        ImageView imgView = new ImageView(this);
        imgView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        return imgView;
    }//获取imgView
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }//打开相册
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            VehicleView.setVisibility(show ? View.GONE : View.VISIBLE);
            VehicleView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    VehicleView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            VehicleView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
        //显示上传进度条
    }

    private void pic()
    {



        //上传图像
        //compress(256);
         OkHttpClient client = new OkHttpClient.Builder().
                connectTimeout(60, TimeUnit.SECONDS).
                readTimeout(60, TimeUnit.SECONDS).
                writeTimeout(60, TimeUnit.SECONDS).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DriverService.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        DriverService driverService = retrofit.create(DriverService.class);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), new File(fullFilename));
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("item_picture", new File(fullFilename).getName(), requestFile);

        Call<ResponseBody> call = driverService.pic(body);
        showProgress(true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //onResponse方法是运行在主线程也就是UI线程的，所以我们可以在这里直接更新ui
                if (response.isSuccessful()) {
                    showProgress(false);
                    String string = null;
                    try {
                        string = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e("just", "onResponse: " + string);
                    Gson gson  = new Gson();
                    Map map = gson.fromJson(string, Map.class);
                    System.out.println("");
                    List list=new ArrayList();
                    //获取到json文件，将其转化为List
                    for(Object key:map.keySet())
                    {
                        try {
                            LinkedTreeMap<String,String> linkedTreeMap=(LinkedTreeMap<String, String>)map.get(key);
                            VoiceItem item=new VoiceItem();
                            item.setItem(linkedTreeMap.get("Name"));
                            item.setCatelog(String.valueOf(linkedTreeMap.get("ClassID")).substring(0,1));

                            list.add(item);
                            System.out.println("");

                        }catch (Exception e)
                        {

                        }

                    }
                    initPopWindow(list);//弹出弹出窗口


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
    private void initPopWindow(List arrayList){

        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popwindow, null);
        upload=contentView.findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentImageView = frontImage;
                currentFilename = "li.jpg";
                takephoto();
                upload.setClickable(true);
            }
        });
        //重新上传按钮功能
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        PopupWindow popupWindow = new PopupWindow(findViewById(R.id.mainLayout), width, 1000,true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corners_pop));
        popupWindow.setContentView(contentView);
        contentView.setBackgroundColor(Color.WHITE);
        TextView textView = (TextView) contentView.findViewById(R.id.text);
        textView.setText("识别结果");
        VoiceItemAdapter arrayAdapter = new VoiceItemAdapter(ImageActivity.this, R.layout.result_voiceitem, arrayList);
        ListView listView = (ListView) contentView.findViewById(R.id.list);
        listView.setAdapter(arrayAdapter);
        //将识别结果放在listview中
        //初始化弹出框
        popupWindow.setAnimationStyle(R.anim.popwindow_enter);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(VehicleView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param srcPath （根据路径获取图片并压缩）
     * @return
     */
    public  File getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了，只读取图片的大小，不分配内存
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是1920*1080分辨率，所以高和宽我们设置为
        float hh = 1920f;// 这里设置高度为1920f
        float ww = 1080f;// 这里设置宽度为1080f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //检测并旋转图片
        Bitmap bitmap1=changeImageLocate(srcPath,bitmap);
        return compressImage(bitmap1);// 压缩好比例大小后再进行质量压缩
    }

    //检测并旋转图片
    public  Bitmap changeImageLocate(String filepath, Bitmap bitmap) {
        //根据图片的filepath获取到一个ExifInterface的对象
        int degree = 0;
        try {
            ExifInterface exif = new ExifInterface(filepath);
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                Log.e("degree========ori====", ori + "");

                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        degree = 0;
                        break;
                }
                Log.e("degree============", degree + "");
                if (degree != 0) {
                    Log.e("degree============", "degree != 0");
                    // 旋转图片
                    Matrix m = new Matrix();
//                    m.setScale(0.5f,0.5f);
                    m.postRotate(degree);

                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

                    return bitmap;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //质量压缩
    public  File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 300) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }


        File file = new File(fullFilename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        recycleBitmap(bitmap);
        return file;
    }

    //回收Bitmap
    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }



}
