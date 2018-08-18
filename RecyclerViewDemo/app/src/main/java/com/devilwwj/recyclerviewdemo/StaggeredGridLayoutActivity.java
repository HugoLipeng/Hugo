package com.devilwwj.recyclerviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class StaggeredGridLayoutActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<String> datas;
    private StaggeredGridAdapter mAdapter;

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
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        // 添加分割线
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new StaggeredGridAdapter(this, datas);
        // 设置adapter
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickLister(new RecylerViewAdapter.OnItemClickLister() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onItemLongClick(View view, int position) {
                mAdapter.deleteData(position);
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
        }
        return true;
    }
}
