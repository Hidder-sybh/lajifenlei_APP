package Util;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
//工具类，基础活动，用以实现页面之间跳转的动画
public abstract class BaseActivity extends SuperActivity {
    private GestureDetector mGestureDetector ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mGestureDetector=new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                if (e1.getRawX()-e2.getRawX()>30) {
                    nextPage();//右滑
                }else if (e2.getRawX()-e1.getRawX()>30) {
                    prePage();//左滑

                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });


    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    public  void nextpage(View view){
        nextPage();

    };
    public  void prepage(View view){

        prePage();
    };
    public abstract void nextPage();
    public abstract void prePage();

}
