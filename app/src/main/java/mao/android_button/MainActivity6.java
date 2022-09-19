package mao.android_button;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.LockSupport;

public class MainActivity6 extends AppCompatActivity
{

    private Button button;

    public static final String TAG = "MainActivity6";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        button = findViewById(R.id.button6);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handler();
            }
        });
    }

    private void handler()
    {
        Log.d(TAG, "onClick: 调用");
        if (!button.isEnabled())
        {
            //其实跑不到这里
            Log.d(TAG, "onClick: 不可用");
        }
        //当前可用
        button.setEnabled(false);
        final int[] i = {10};


        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                button.setText(String.valueOf(i[0]));
                if (i[0] < 0)
                {
                    MainActivity6.this.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            //Thread.sleep();方法调用会无响应
                            //LockSupport.park也是一样，原子操作(cas)也是一样
                            //Android系统中的视图组件并不是线程安全的，
                            // 如果要更新视图，必须在主线程中更新，不可以在子线程中执行更新的操作。
                            //Only the original thread that created a view hierarchy can touch its views.
                            //解决方案：MainActivity6.this.runOnUiThread
                            //或者使用Handler对象
                            button.setEnabled(true);
                            button.setText("重新发送验证码");
                            timer.cancel();
                        }
                    });
                }
                i[0]--;
            }
        }, 0, 1000);

    }
}