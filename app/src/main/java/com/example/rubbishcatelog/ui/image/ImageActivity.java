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
    private ImageView currentImageView; //???????????????????????????
    private String currentFilename;   //???????????????
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
        upload.setClickable(false);//???????????????????????????
        upload.setOnClickListener(this);
        frontImage = findViewById(R.id.ivFront);
        permissionUtil.setActivity(this);//????????????
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.frontButton:
                currentImageView = frontImage;
                currentFilename = "li.jpg";
                takephoto();
                upload.setClickable(true);//??????????????????
                break;//????????????
            case R.id.licenseChooseButton:
                openAlbum();
                upload.setClickable(true);//??????????????????
                break;
            case R.id.upload:
                if(!fullFilename.isEmpty())
                {
                   pic();
                }//??????

        }

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //????????????????????????????????????
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
    private void takephoto() {

        permissionUtil.requestPermission(Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA);
        //????????????
        File cacheDir = getExternalFilesDir("image");
        Log.i("just", cacheDir == null ? "null" : cacheDir.toString());
        File outputImage = new File(cacheDir, currentFilename );
        imageFileUri = Uri.fromFile(outputImage);
        //???????????????
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
        //??????????????????
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
                break;//????????????????????????????????????????????????
            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    fullFilename = imageFileUri.getPath();
                    getimage(fullFilename);
                    displayImage(fullFilename);


                }//???????????????????????????????????????????????????
        }

    }
    //????????????????????????
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
        //??????uri????????????????????????????????????????????????
        final File cacheDir = getExternalFilesDir("image");
        currentFilename = "li"  + ".jpg";
        final String destFilename = new File(cacheDir, currentFilename).getAbsolutePath();
        fullFilename=imagePath;
        displayImage(imagePath);
        //????????????
    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            frontImage.setImageBitmap(bitmap);

        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }//???????????????????????????

    public ImageView getView() {
        ImageView imgView = new ImageView(this);
        imgView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        return imgView;
    }//??????imgView
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }//????????????
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
        //?????????????????????
    }

    private void pic()
    {



        //????????????
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
                //onResponse????????????????????????????????????UI???????????????????????????????????????????????????ui
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
                    //?????????json????????????????????????List
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
                    initPopWindow(list);//??????????????????


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
        //????????????????????????
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        PopupWindow popupWindow = new PopupWindow(findViewById(R.id.mainLayout), width, 1000,true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_corners_pop));
        popupWindow.setContentView(contentView);
        contentView.setBackgroundColor(Color.WHITE);
        TextView textView = (TextView) contentView.findViewById(R.id.text);
        textView.setText("????????????");
        VoiceItemAdapter arrayAdapter = new VoiceItemAdapter(ImageActivity.this, R.layout.result_voiceitem, arrayList);
        ListView listView = (ListView) contentView.findViewById(R.id.list);
        listView.setAdapter(arrayAdapter);
        //?????????????????????listview???
        //??????????????????
        popupWindow.setAnimationStyle(R.anim.popwindow_enter);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(VehicleView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * ?????????????????????????????????
     *
     * @param srcPath ???????????????????????????????????????
     * @return
     */
    public  File getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // ??????????????????????????????options.inJustDecodeBounds ??????true????????????????????????????????????????????????
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// ????????????bm??????
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // ??????????????????????????????1920*1080??????????????????????????????????????????
        float hh = 1920f;// ?????????????????????1920f
        float ww = 1080f;// ?????????????????????1080f
        // ????????????????????????????????????????????????????????????????????????????????????????????????
        int be = 1;// be=1???????????????
        if (w > h && w > ww) {// ???????????????????????????????????????????????????
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// ???????????????????????????????????????????????????
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// ??????????????????
        // ??????????????????????????????????????????options.inJustDecodeBounds ??????false???
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //?????????????????????
        Bitmap bitmap1=changeImageLocate(srcPath,bitmap);
        return compressImage(bitmap1);// ?????????????????????????????????????????????
    }

    //?????????????????????
    public  Bitmap changeImageLocate(String filepath, Bitmap bitmap) {
        //???????????????filepath???????????????ExifInterface?????????
        int degree = 0;
        try {
            ExifInterface exif = new ExifInterface(filepath);
            if (exif != null) {
                // ?????????????????????????????????
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                Log.e("degree========ori====", ori + "");

                // ??????????????????
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
                    // ????????????
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

    //????????????
    public  File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ???????????????????????????100????????????????????????????????????????????????baos???
        int options = 90;

        while (baos.toByteArray().length / 1024 > 300) { // ?????????????????????????????????????????????100kb,??????????????????
            baos.reset(); // ??????baos?????????baos
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// ????????????options%?????????????????????????????????baos???
            options -= 10;// ???????????????10
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

    //??????Bitmap
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
