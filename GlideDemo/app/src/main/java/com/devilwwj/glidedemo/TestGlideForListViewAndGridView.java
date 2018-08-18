package com.devilwwj.glidedemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

/**
 * com.devilwwj.glidedemo
 * Created by devilwwj on 16/3/14.
 */
public class TestGlideForListViewAndGridView extends Activity {
    public static String[] eatFoodyImages = {
            "http://i.imgur.com/rFLNqWI.jpg",
            "http://i.imgur.com/C9pBVt7.jpg",
            "http://i.imgur.com/rT5vXE1.jpg",
            "http://i.imgur.com/aIy5R2k.jpg",
            "http://i.imgur.com/MoJs9pT.jpg",
            "http://i.imgur.com/S963yEM.jpg",
            "http://i.imgur.com/rLR2cyc.jpg",
            "http://i.imgur.com/SEPdUIx.jpg",
            "http://i.imgur.com/aC9OjaM.jpg",
            "http://i.imgur.com/76Jfv9b.jpg",
            "http://i.imgur.com/fUX7EIB.jpg",
            "http://i.imgur.com/syELajx.jpg",
            "http://i.imgur.com/COzBnru.jpg",
            "http://i.imgur.com/Z3QjilA.jpg",
    };

    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testfor_listview);

        listview = (ListView) findViewById(R.id.listview);

        listview.setAdapter(new ImageListAdapter(this, eatFoodyImages));

    }


    private class ImageListAdapter extends ArrayAdapter {
        private Context context;
        private LayoutInflater inflater;
        private String[] imageUrls;

        public ImageListAdapter(Context context, String[] imageUrls) {
            super(context, R.layout.listview_item_image, imageUrls);
            this.context = context;
            this.imageUrls = imageUrls;

            inflater = LayoutInflater.from(context);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.listview_item_image, parent, false);
            }

            // 3 ListAdater(ListView,GridView)



            // 4. listView中的加载,占位符、错误占位符、淡入淡出动画（crossFade())、dontAnimate()-直接显示图片
            Glide.with(context).load(imageUrls[position]).placeholder(R.mipmap.ic_launcher)
                    .override(600, 200)
                    .crossFade().into((ImageView) convertView);


            // 5. 图片重设大小和缩放
            // resize(x,y)调整图片大小
            // CenterCrop-缩放图片
            // FitCenter-裁剪



            return convertView;
        }
    }
}
