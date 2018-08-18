package com.devilwwj.androidcrashdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btnCreateAnCrash);

        // 制造一个NullPointerException（空指针）
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "喜欢我就点我吧", Toast.LENGTH_SHORT).show();

                Object obj = null;
                obj.toString();
            }
        });

    }
}
