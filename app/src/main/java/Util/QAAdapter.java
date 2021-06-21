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
import com.example.rubbishcatelog.model.Wrongtask;

import java.util.List;

public class QAAdapter  extends ArrayAdapter<Wrongtask> {

    private int recourceId;
    public QAAdapter(@NonNull Context context, int resource, List<Wrongtask> objects) {
        super(context, resource, objects);
        recourceId = resource;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Wrongtask imageListArray = getItem(position); //得到集合中指定位置的一组数据，并且实例化
        View view = LayoutInflater.from(getContext()).inflate(recourceId,parent,false); //用布局裁剪器(又叫布局膨胀器)，将导入的布局裁剪并且放入到当前布局中
        TextView q = (TextView) view.findViewById(R.id.q);//从裁剪好的布局里获取ImageView布局ID
        TextView a = (TextView)view.findViewById(R.id.a); //从裁剪好的布局里获取TextView布局Id
       q.setText(imageListArray.getQus().trim());//将当前一组imageListArray类中的TextView内容导入到TextView布局中
        a.setText(imageListArray.getAns());
        return view;
    }
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
