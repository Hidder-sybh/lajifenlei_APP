package Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rubbishcatelog.R;

import java.util.List;

public class ImageViewAdapter   extends ArrayAdapter<ImageAndText> {
    private int recourceId;
    public ImageViewAdapter(@NonNull Context context, int resource, List<ImageAndText> objects) {
        super(context, resource, objects);
        recourceId = resource;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ImageAndText imageListArray = getItem(position); //得到集合中指定位置的一组数据，并且实例化
        View view = LayoutInflater.from(getContext()).inflate(recourceId,parent,false); //用布局裁剪器(又叫布局膨胀器)，将导入的布局裁剪并且放入到当前布局中
        ImageView imageView = (ImageView)view.findViewById(R.id.IamgeView_List);//从裁剪好的布局里获取ImageView布局ID
        TextView textView = (TextView)view.findViewById(R.id.TextView_List); //从裁剪好的布局里获取TextView布局Id
        imageView.setImageResource(imageListArray.getImageId());//将当前一组imageListArray类中的图片iamgeId导入到ImageView布局中
        textView.setText(imageListArray.getName());//将当前一组imageListArray类中的TextView内容导入到TextView布局中
        return view;
    }
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
