package com.devilwwj.recyclerviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> datas;

    private RecylerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recylerView);
        // 设置布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // 添加分割线
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new RecylerViewAdapter(this, datas);
        // 添加item动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置adapter
        recyclerView.setAdapter(mAdapter);
        // 设置点击事件
        mAdapter.setOnItemClickLister(new RecylerViewAdapter.OnItemClickLister() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, "long click", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initData() {
        datas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            datas.add("" + (char) i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id =  item.getItemId();
        switch (id) {
            case R.id.action_add:
                mAdapter.addData(1);
                break;

            case R.id.action_delete:
                mAdapter.deleteData(1);
                break;
            case R.id.action_listview:
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.action_gridview:
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.action_hor_gridview:
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.HORIZONTAL));
                break;
            case R.id.action_staggered:
                Intent intent = new Intent(this, StaggeredGridLayoutActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
