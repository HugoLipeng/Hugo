package com.devilwwj.hotfixdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.devilwwj.hotfixlib.HotFix;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_fix:
                // 准备补丁，从assert里拷贝到dex里
                File dexPath = new File(getDir("dex", Context.MODE_PRIVATE), "patch_dex.jar");
                Utils.prepareDex(this.getApplicationContext(), dexPath, "patch_dex.jar");
                HotFix.patch(this, dexPath.getAbsolutePath(), "com.devilwwj.hotfixdemo.BugClass");
                try {
                    this.getClassLoader().loadClass("com.devilwwj.hotfixdemo.BugClass");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.action_test:
                LoadBugClass bugClass = new LoadBugClass();
                Toast.makeText(this, "测试调用方法：" + bugClass.getBugString(), Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
