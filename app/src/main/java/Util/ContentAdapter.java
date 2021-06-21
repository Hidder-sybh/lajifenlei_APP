package Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.rubbishcatelog.R;
import com.example.rubbishcatelog.model.article;

import java.io.File;
import java.util.List;
//文章适配器，用以显示文章及其图像
public class ContentAdapter extends ArrayAdapter{
    private int resourceId;
    public ContentAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       article thisarticle =(article) getItem(position);//获取当前项的Fruit实例
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView currentImage = (ImageView) view.findViewById(R.id.articleImg);
        TextView currentTitle = (TextView) view.findViewById(R.id.articleTitle);
        TextView currentContent= (TextView) view.findViewById(R.id.articleContent);
        String url=thisarticle.getImgname();
        File appDir = new File(Environment.getExternalStorageDirectory(), "baiUtil");
        String fileName = url + ".jpg";
        File file = new File(appDir,fileName);
        if(file.exists()){

            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());

            currentImage.setImageBitmap(bm);
            //下面是图像

        }
         currentContent.setText("\u3000"+thisarticle.getContent());
        currentTitle.setText(thisarticle.getTitle());
        return  view;
    }

}
