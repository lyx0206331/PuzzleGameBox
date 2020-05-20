package com.adrian.modulemain.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.adrian.commonlib.baseComponent.BaseActivity;
import com.adrian.commonlib.tools.CommUtil;
import com.adrian.commonlib.tools.PermissionHelper;
import com.adrian.modulemain.R;
import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.xiaomi.ad.common.pojo.AdType;

public class WelcomeActivity extends BaseActivity {

    private PermissionHelper mPermissionHelper;

    //以下的POSITION_ID 需要使用您申请的值替换下面内容
    private static final String POSITION_ID = "2222bcaa6b3c606df9756b7fd12cfafd";
    private static final String TEST_POSITION_ID = "b373ee903da0c6fc9c9da202df95a500";
    private FrameLayout flAdContainer;
    private IAdWorker iAdWorker;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startActivity(RecyclerViewActivity.class);
                    finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initVariables() {
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.modulemain_activity_welcome);
//        handler.sendEmptyMessageDelayed(0, 2000);
        flAdContainer = findViewById(R.id.flAdContainer);
    }

    @Override
    protected void loadData() {
        // 当系统为6.0以上时，需要申请权限
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
//                Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
                runApp();
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
//            Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
            runApp();
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
//                Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
                runApp();
            } else {
                // 如果还有权限未申请，而且系统版本大于23，执行申请权限逻辑
//                Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 跑应用的逻辑
     */
    private void runApp() {
        //设置开屏
        setupSplashAd();
    }

    /**
     * 设置开屏广告
     */
    private void setupSplashAd() {
        try {
            iAdWorker = AdWorkerFactory.getAdWorker(this, flAdContainer, new MimoAdListener() {
                @Override
                public void onAdPresent() {
                    CommUtil.INSTANCE.logE(WelcomeActivity.class, "onAdPresent");
                }

                @Override
                public void onAdClick() {
                    CommUtil.INSTANCE.logE(WelcomeActivity.class, "onAdClick");
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onAdDismissed() {
                    CommUtil.INSTANCE.logE(WelcomeActivity.class, "onAdDismissed");
                    handler.sendEmptyMessage(0);
                }

                @Override
                public void onAdFailed(String s) {
                    CommUtil.INSTANCE.logE(WelcomeActivity.class, "onAdFailed:" + s);
                    handler.sendEmptyMessageDelayed(0, 3000);
                }

                @Override
                public void onAdLoaded(int i) {
                    CommUtil.INSTANCE.logE(WelcomeActivity.class, "onAdLoaded:" + i);
//                    handler.sendEmptyMessageDelayed(0, 3000);
                }

                @Override
                public void onStimulateSuccess() {
                    CommUtil.INSTANCE.logE(WelcomeActivity.class, "onStimulateSuccess");
                }
            }, AdType.AD_SPLASH);
            iAdWorker.loadAndShow(POSITION_ID);
        } catch (Exception e) {
            e.printStackTrace();
            flAdContainer.setVisibility(View.GONE);
            handler.sendEmptyMessageDelayed(0, 3000);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 捕获back键，在展示广告期间按back键，不跳过广告
            if (flAdContainer.getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onDestroy() {
        try {
            handler.removeMessages(0);
            super.onDestroy();
            iAdWorker.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
