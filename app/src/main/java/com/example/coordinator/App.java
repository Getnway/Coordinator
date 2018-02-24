package com.example.coordinator;

import android.app.Application;
import android.util.Log;
import android.widget.ImageView;

import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.adapter.IWXImgLoaderAdapter;
import com.taobao.weex.common.WXException;
import com.taobao.weex.common.WXImageStrategy;
import com.taobao.weex.dom.WXImageQuality;

/**
 * Author: getnway on 2018-02-23.
 * Email: getnway@gmail.com
 */
public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        InitConfig config = new InitConfig.Builder()
                .setImgAdapter(new IWXImgLoaderAdapter() {
                    @Override
                    public void setImage(String s, ImageView imageView, WXImageQuality wxImageQuality, WXImageStrategy wxImageStrategy) {
                        imageView.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                })
                .build();
        WXSDKEngine.initialize(this, config);
    }
}
