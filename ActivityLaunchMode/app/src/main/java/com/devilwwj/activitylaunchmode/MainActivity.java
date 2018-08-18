package com.devilwwj.activitylaunchmode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/**
 * @desc: Activity启动模式
 * @author devilwwj
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
    }

    /**
     * 以standard模式来启动Activity
     * @param view
     */
    public void openActivityWithStandard(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    /**
     * 以singleTop模式来启动Activity
     * @param view
     */
    public void openActivityWithSingleTop(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /**
     * 以singleTask模式来启动Activity
     * @param view
     */
    public void openActivityWithSingleTask(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



}
