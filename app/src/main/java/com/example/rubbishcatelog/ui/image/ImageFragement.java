package com.example.rubbishcatelog.ui.image;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.rubbishcatelog.R;

import java.io.File;
import java.io.IOException;

import Util.PermissionUtil;
//暂时弃用
public class ImageFragement extends Fragment implements View.OnClickListener {

    PermissionUtil permissionUtil = new PermissionUtil();
    private static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 6;
    private ImageView frontImage;
    private ImageView currentImageView; //当前该设置那张图片
    private String currentFilename;   //当前图片名
    private Uri imageUri;
    private Uri imageFileUri;
    private Button frontButton;
    private Button licenseChooseButton;
    private String fullFilename;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragement_image, container, false);
        frontButton = root.findViewById(R.id.frontButton);
        frontButton.setOnClickListener(this);
        licenseChooseButton = root.findViewById(R.id.licenseChooseButton);
        licenseChooseButton.setOnClickListener(this);
        frontImage = root.findViewById(R.id.ivFront);
        permissionUtil.setActivity(getActivity());

        return root;
    }

    @Override
    public void onClick(View v) {
    switch (v.getId())
    {
        case R.id.frontButton:
            currentImageView = frontImage;
            currentFilename = "li.jpg";
            takephoto();
            break;
        case R.id.licenseChooseButton:
            openAlbum();
            break;

    }

    }
    private void takephoto() {

       permissionUtil.requestPermission(Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA);
        File cacheDir = getActivity().getExternalFilesDir("image");
        //File cacheDir = getCacheDir();
        Log.i("just", cacheDir == null ? "null" : cacheDir.toString());
        File outputImage = new File(cacheDir, currentFilename + ".jpg");
        imageFileUri = Uri.fromFile(outputImage);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT > 24) {
            imageUri = FileProvider.getUriForFile(getActivity(),
                    "com.zjdt.driver.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
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
                break;
            case TAKE_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    fullFilename = imageFileUri.getPath();
                    displayImage(fullFilename);

                }
                break;
        }

    }
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
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
        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
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
        final File cacheDir = getActivity().getExternalFilesDir("image");
        currentFilename = "li"  + ".jpg";
        final String destFilename = new File(cacheDir, currentFilename).getAbsolutePath();
        displayImage(imagePath);

    }
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            frontImage.setImageBitmap(bitmap);

        } else {
            Toast.makeText(getContext(), "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    public ImageView getView() {
        ImageView imgView = new ImageView(getContext());
        imgView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
        return imgView;
    }
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }
}