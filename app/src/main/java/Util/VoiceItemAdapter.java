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

public class VoiceItemAdapter  extends ArrayAdapter<VoiceItem> {
    private int recourceId;
    public VoiceItemAdapter(@NonNull Context context, int resource, List<VoiceItem> objects) {
        super(context, resource, objects);
        recourceId = resource;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       VoiceItem voiceItem = getItem(position); //得到集合中指定位置的一组数据，并且实例化
        View view = LayoutInflater.from(getContext()).inflate(recourceId,parent,false); //用布局裁剪器(又叫布局膨胀器)，将导入的布局裁剪并且放入到当前布局中
        TextView voiceitem = (TextView)view.findViewById(R.id.voiceitem); //从裁剪好的布局里获取TextView布局Id
        TextView voicecatelog = (TextView)view.findViewById(R.id.catelog); //从裁剪好的布局里获取TextView布局Id;
        voiceitem.setText(voiceItem.getItem());//将当前一组imageListArray类中的TextView内容导入到TextView布局中
        voicecatelog.setText(voiceItem.getCatelog());
        //根据类别码，生成对应的类别属性
        if(voiceItem.getCatelog().equals("3"))

        {
            voicecatelog.setText("[有害垃圾]");
            voicecatelog.setTextColor(getContext().getResources().getColor(R.color.red));

        }
        else if(voiceItem.getCatelog().equals("2"))
        {
            voicecatelog.setText("[干垃圾]");
            voicecatelog.setTextColor(getContext().getResources().getColor(R.color.gray));

        }
        else if(voiceItem.getCatelog().equals("4"))
        {
            voicecatelog.setText("[湿垃圾]");
            voicecatelog.setTextColor(getContext().getResources().getColor(R.color.olive));

        } else if(voiceItem.getCatelog().equals("1"))
        {
            voicecatelog.setText("[可回收垃圾]");
            voicecatelog.setTextColor(getContext().getResources().getColor(R.color.powderblue));

        }
        return view;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}
