package com.example.coordinator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.ui.view.refresh.core.WXSwipeLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: getnway on 2018-02-23.
 * Email: getnway@gmail.com
 */
public class WeexFragment extends Fragment implements IWXRenderListener {
    private static final String TAG = "WeexFragment";
    protected FrameLayout mContainer;
    protected WXSDKInstance mWXSDKInstance;
    protected Map<String, Object> mOptions = new HashMap<>();
    private NestedScrollView scroll_view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWXSDKInstance = new WXSDKInstance(getActivity());
        mWXSDKInstance.registerRenderListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weex_fragment, null);
        mContainer = (FrameLayout) rootView.findViewById(R.id.frame_layout);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mOptions.put("os", "android");
        render();
    }

    private void render() {
        mOptions.put(WXSDKInstance.BUNDLE_URL, getWeexUrl());
        mWXSDKInstance.renderByUrl(
                getClass().getSimpleName(),
                getWeexUrl(),
                mOptions,
                null,
                WXRenderStrategy.APPEND_ASYNC);
    }

    private String getWeexUrl() {
        return "https://raw.githubusercontent.com/apache/incubator-weex/master/android/playground/app/src/main/assets/about.weex.js";
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityDestroy();
        }
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        Log.d(TAG, String.format("call onViewCreated(): getChildCount = [%s], view = [%s]", ((ViewGroup) view).getChildCount(), view));
        enableWXSwipeLayoutNestedScrolling(view);
        mContainer.addView(view);
    }

    private void enableWXSwipeLayoutNestedScrolling(View view) {
        if (view instanceof WXSwipeLayout) {
            ((WXSwipeLayout) view).setNestedScrollingEnabled(true);
        } else if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); ++i) {
                enableWXSwipeLayoutNestedScrolling(((ViewGroup) view).getChildAt(i));
            }
        }
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
        Log.d(TAG, String.format("call onRenderSuccess(): instance = [%s], width = [%s], height = [%s]", instance, width, height));
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
        Log.d(TAG, String.format("call onRefreshSuccess(): instance = [%s], width = [%s], height = [%s]", instance, width, height));
    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {
        Log.e(TAG, String.format("call onException(): instance = [%s], errCode = [%s], msg = [%s]", instance, errCode, msg));
    }
}
