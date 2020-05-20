package com.adrian.commonlib.baseComponent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final String TAG = "GOMOKU";

    protected Context mContext;

    private ProgressDialog mPd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        initVariables();
        initViews();
        loadData();
    }

    protected void startActivityForResult(Class<? extends Activity> dstAct, int reqCode) {
        Intent intent = new Intent(getApplicationContext(), dstAct);
        startActivityForResult(intent, reqCode);
    }

    protected void startActivityForResult(Class<? extends Activity> dstAct, int reqCode, @NonNull Bundle bundle) {
        Intent intent = new Intent(getApplicationContext(), dstAct);
        intent.putExtras(bundle);
        startActivityForResult(intent, reqCode);
    }

    protected void startActivity(Class<? extends Activity> dstAct) {
        Intent intent = new Intent(getApplicationContext(), dstAct);
        startActivity(intent);
    }

    protected void startActivity(Class<? extends Activity> dstAct, @NonNull Bundle bundle) {
        Intent intent = new Intent(getApplicationContext(), dstAct);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void finish() {
        super.finish();
    }

    protected void showProgress(boolean show) {
        if (mPd == null) {
            mPd = new ProgressDialog(this);
            mPd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mPd.setMessage("Loading...");
            mPd.setIndeterminate(false);
            mPd.setCancelable(false);
        }
        if (show && !mPd.isShowing()) {
            mPd.show();
        } else if (!show && mPd.isShowing()) {
            mPd.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 初始化变量
     */
    protected abstract void initVariables();

    /**
     * 初始化UI
     */
    protected abstract void initViews();

    /**
     * 数据加载
     */
    protected abstract void loadData();

    /**
     * 打印调试级别日志
     *
     * @param format
     * @param args
     */
    protected void logDebug(String format, Object... args) {
        logMessage(Log.DEBUG, format, args);
    }

    /**
     * 打印信息级别日志
     *
     * @param format
     * @param args
     */
    protected void logInfo(String format, Object... args) {
        logMessage(Log.INFO, format, args);
    }

    /**
     * 打印错误级别日志
     *
     * @param format
     * @param args
     */
    protected void logError(String format, Object... args) {
        logMessage(Log.ERROR, format, args);
    }

    /**
     * 展示短时Toast
     *
     * @param format
     * @param args
     */
    protected void showShortToast(String format, Object... args) {
        showToast(Toast.LENGTH_SHORT, format, args);
    }

    /**
     * 展示长时Toast
     *
     * @param format
     * @param args
     */
    protected void showLongToast(String format, Object... args) {
        showToast(Toast.LENGTH_LONG, format, args);
    }

    /**
     * 打印日志
     *
     * @param level
     * @param format
     * @param args
     */
    private void logMessage(int level, String format, Object... args) {
        String formattedString = String.format(format, args);
        switch (level) {
            case Log.DEBUG:
                Log.d(TAG, formattedString);
                break;
            case Log.INFO:
                Log.i(TAG, formattedString);
                break;
            case Log.ERROR:
                Log.e(TAG, formattedString);
                break;
        }
    }

    /**
     * 展示Toast
     *
     * @param duration
     * @param format
     * @param args
     */
    private void showToast(int duration, String format, Object... args) {
        Toast.makeText(mContext, String.format(format, args), duration).show();
    }
}
