package com.devilwwj.glidedemo;

/**
 * com.devilwwj.glidedemo
 * Created by devilwwj on 16/3/15.
 */
public class CustomImageSizeModelFutureStudio implements CustomImageSizeModel {
    String baseImageUrl;

    public CustomImageSizeModelFutureStudio(String baseImageUrl) {
        this.baseImageUrl = baseImageUrl;
    }


    @Override
    public String requestCustomSizeUrl(int width, int height) {
        return baseImageUrl + "?w=" + width + "&h=" + height;
    }
}
