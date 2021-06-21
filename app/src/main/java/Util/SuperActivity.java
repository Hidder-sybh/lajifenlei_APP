package Util;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 所有activity都继承此类，同时也就等于注册了广播，
 * 当需要完全退出系统的时候就可以发送广播，
 * action为com.example.exitsystem.system_exit（自定义），
 * 这样就可以随时退出所有的activity了
 * @author LinZhiquan
 *
 */
public class SuperActivity extends AppCompatActivity {
    /** 广播action */
    public static final String SYSTEM_EXIT = "com.example.exitsystem.system_exit";
    /** 接收器 */
    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册广播，用于退出程序
        IntentFilter filter = new IntentFilter();
        filter.addAction(SYSTEM_EXIT);
        receiver = new MyReceiver();
        this.registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        //记得取消广播注册
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }
}
