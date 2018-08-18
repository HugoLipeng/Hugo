package com.devilwwj.swiperefreshlayoutdemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

public class MainActivity extends AppCompatActivity implements OnRefreshListener{
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;
    private ArrayList<String> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getData()));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        // 设置刷新监听器
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    private ArrayList<String> getData() {
        list.add("Hello");
        list.add("This is devilwwj");
        list.add("An Android Developer");
        list.add("Love Open Source");
        list.add("My GitHub: devilwwj");
        list.add("weibo: IT_xiao小巫");
        return list;
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);

    }
}
