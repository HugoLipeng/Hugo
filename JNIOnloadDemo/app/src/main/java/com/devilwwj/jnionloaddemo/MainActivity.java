package com.devilwwj.jnionloaddemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.devilwwj.library.JavaHello;

public class MainActivity extends Activity {

    static {
        System.loadLibrary("hello");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("native print", new JavaHello().hello());
    }
}
