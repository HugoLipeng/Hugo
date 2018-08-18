package com.devilwwj.glidedemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

public class MainActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID = 0;
    private ImageView imageView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageview);

        String internetUrl = "https://raw.githubusercontent.com/fanatic-mobile-developer-for-android/A-week-to-develop-android-app-plan/master/images/wwjblog.jpg";

        // 1.  初步用法
//        Glide.with(this).load(internetUrl).into(imageView);



        // 2.1 从资源中加载
//        int resourceId = R.mipmap.ic_launcher;
//        Glide.with(this).load(resourceId).into(imageView);

        // 2.2 从文件中加载
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Running.jpg");
//        Glide.with(this).load(file).into(imageView);

        // 2.3 从Uri中加载
//        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.mipmap.ic_launcher);
//        Glide.with(this).load(uri).into(imageView);


        // 6.1 显示gif 6.2 Gif检查-asGif() 6.3 转为Bitmap asBitmap()
//        String gifUrl = "http://i.kinja-img.com/gawker-media/image/upload/s--B7tUiM5l--/gf2r69yorbdesguga10i.gif";
//        Glide.with(this).load(gifUrl).asGif().into(imageView);

        // 6.4 显示本地视频
//        String filePath = "/storage/emulated/0/Pictures/example_video.mp4";
//        Glide.with(this).load(Uri.fromFile(new File(filePath))).into(imageView);


        // 7 缓存基础
        // 7.1 内存缓存
//        Glide.with(this).load(eatFoodyImages[0])
//                .skipMemoryCache(true).into(imageView);


        // 7.2 磁盘缓存
//        Glide.with(this).load(eatFoodyImages[0]).diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(imageView);

        /*
         * DiskCacheStrategy.NONE 什么都不缓存
         * DiskCacheStrategy.SOURCE 仅仅只缓存原来的全分辨率的图像
         * DiskCacheStrategy.RESULT 仅仅缓存最终的图像
         * DiskCacheStrategy.ALL 缓存所有版本的图像
         */


        // 8 请求优先级
        // Priority.LOW Priority.NORMAL Priority.HIGH Priority.IMMEDIATE

//        Glide.with(this).load(eatFoodyImages[0]).priority(Priority.HIGH).into(imageView);

        // 9. 缩略图
//        Glide.with(this).load(eatFoodyImages[0]).thumbnail(0.1f).into(imageView);

        // setup Glide request without the into() method
//        DrawableRequestBuilder<String> thumbnailRequest = Glide.with(this).load(eatFoodyImages[2]);

        // pass the request as a parameter to the thumail request
//        Glide.with(this).load(eatFoodyImages[0]).thumbnail(thumbnailRequest).into(imageView);



        // 10. SimpleTarget和ViewTarget用于自定义视图类
//        SimpleTarget target = new SimpleTarget<Bitmap>(250, 250) {
//
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                imageView.setImageBitmap(resource);
//            }
//        };
//
//        // Target可以指定大小
//        Glide.with(this).load(eatFoodyImages[0]).asBitmap().into(target);

//
//        FutureStudioView customView = (FutureStudioView) findViewById(R.id.custom_view);
//
//        ViewTarget viewTarget = new ViewTarget<FutureStudioView, GlideDrawable>(customView) {
//            @Override
//            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                this.view.setImage(resource.getCurrent());
//            }
//
//        };
//
//        Glide.with(this).load(eatFoodyImages[2]).into(viewTarget);


        // 11. 加载图片到通知栏和应用小部件中

        testNotification();


        // 12. 调试和错误处理
        // adb shell setprop log.tag.GenericRequest DEBUG
//        RequestListener<String, GlideDrawable> requestListener = new RequestListener<String, GlideDrawable>() {
//            @Override
//            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//
//                // important to return false so the error placeholder can be placed
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                return false;
//            }
//        };
//
//        Glide.with(this).load(eatFoodyImages[0])
//                .listener(requestListener)
//                .error(R.mipmap.ic_launcher)
//                .into(imageView);



        // 13. 自定义转换
//        Glide.with(this).load(eatFoodyImages[0])
//                .transform(new BlurTransformation(this)).into(imageView);

        // 14. 用animate()自定义动画
//        Glide.with(this).load(eatFoodyImages[0])
//                .animate(android.R.anim.slide_in_left)
//                .into(imageView);

//        ViewPropertyAnimation.Animator animator = new ViewPropertyAnimation.Animator() {
//
//            @Override
//            public void animate(View view) {
//                view.setAlpha(0f);
//
//                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f,1f);
//                fadeAnim.setDuration(2500);
//                fadeAnim.start();
//            }
//        };
//
//        Glide.with(this).load(eatFoodyImages[1]).animate(animator).into(imageView);

        // 19. 用自定义尺寸优化加载的图片
//        String baseImageUrl = "https://futurestud.io/images/example.png";
//        CustomImageSizeModel customImageRequest = new CustomImageSizeModelFutureStudio(baseImageUrl);
//
//        Glide.with(this).load(customImageRequest).into(imageView);


        // 21. 旋转图片
        Glide.with(this).load(eatFoodyImages[0]).transform(new RotateTransformation(this, 90f))
                .into(imageView);

    }

    private void testNotification() {
        final RemoteViews rv = new RemoteViews(this.getPackageName(), R.layout.remoteview_notification);
        rv.setImageViewResource(R.id.remoteview_notification_icon, R.mipmap.ic_launcher);

        rv.setTextViewText(R.id.remoteview_notification_headline, "Headline");
        rv.setTextViewText(R.id.remoteview_notification_short_message, "Short Message");

        // build notification
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Content Title")
                .setContentText("Content Text")
                .setContent(rv)
                .setPriority(NotificationCompat.PRIORITY_MIN);

        final Notification notification = mBuilder.build();

        // set big content view for newer androids
        if (Build.VERSION.SDK_INT >= 16) {
            notification.bigContentView = rv;
        }
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, notification);

        NotificationTarget notificationTarget = new NotificationTarget(this, rv, R.id.remoteview_notification_icon, notification, NOTIFICATION_ID);

        Glide.with(this.getApplicationContext())// safer!
        .load(eatFoodyImages[3]).asBitmap().into(notificationTarget);
    }


}
