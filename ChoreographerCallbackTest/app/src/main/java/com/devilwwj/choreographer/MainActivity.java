package com.devilwwj.choreographer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class MainActivity extends AppCompatActivity implements Choreographer.FrameCallback, View.OnClickListener{

    private static final String TAG = "MainActivity";
    private long lastFrameTimeNanos = 0;
    private long currentFrameTimeNanos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Choreographer.getInstance().postFrameCallback(this);
        }

        startActivity(new Intent(this, SecondActivity.class));
    }


    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause unhooking choreographer");
//        Choreographer.getInstance().removeFrameCallback(this);
    }

     /*
      * Choreographer callback, called near vsync.
      *
      * @see android.view.Choreographer.FrameCallback#doFrame(long)
      */
    @Override
    public void doFrame(long frameTimeNanos) {
//        Log.d(TAG, "frameTimeNanos = " + frameTimeNanos);
        if (lastFrameTimeNanos == 0) {
            lastFrameTimeNanos = frameTimeNanos;
            Choreographer.getInstance().postFrameCallback(this);
            return;
        }

        currentFrameTimeNanos = frameTimeNanos;
        long value = (currentFrameTimeNanos - lastFrameTimeNanos) / 1000000;
        // 大于16ms表示发生卡顿
        if (value > 16) {
            Log.d(TAG, "currentFrameTimeNanos - lastFrameTimeNanos = " + (currentFrameTimeNanos - lastFrameTimeNanos) / 1000000);
        }
        lastFrameTimeNanos = currentFrameTimeNanos;
        Choreographer.getInstance().postFrameCallback(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onClick of R.id.button1: ", e);
                }
                break;
            case R.id.button2:
                for (int i = 0; i < 1000; ++i) {
                    readFile();
                }
                break;
            case R.id.button3:
                double result = compute();
                System.out.println(result);
                break;
            default:
                break;
        }
    }

    private static double compute() {
        double result = 0;
        for (int i = 0; i < 1000000; ++i) {
            result += Math.acos(Math.cos(i));
            result -= Math.asin(Math.sin(i));
        }
        return result;
    }

    private static void readFile() {
        FileInputStream reader = null;
        try {
            reader = new FileInputStream("/proc/stat");
            while (reader.read() != -1) ;
        } catch (IOException e) {
            Log.e(TAG, "reade: /proc/stat", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, " on close reader ", e);
                }
            }
        }
    }
}
