package com.devilwwj.glidedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * com.devilwwj.glidedemo
 * Created by devilwwj on 16/3/15.
 */
public class RotateTransformation extends BitmapTransformation {

    private float rotateRotationAngle = 0f;

    public RotateTransformation(Context context, float rotateRotationAngle) {
        super(context);

        this.rotateRotationAngle = rotateRotationAngle;
    }

    @Override
    protected Bitmap transform(BitmapPool bitmapPool, Bitmap bitmap, int i, int i1) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateRotationAngle);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public String getId() {
        return "rotate" + rotateRotationAngle;
    }
}
