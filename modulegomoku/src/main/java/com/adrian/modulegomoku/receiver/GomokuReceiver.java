package com.adrian.modulegomoku.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.adrian.modulegomoku.service.DownloadService;

public class GomokuReceiver extends BroadcastReceiver {

    public static final String ACTION_UPDATE = "com.adrian.gomoku.start_update";

    public GomokuReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
//        Log.e("RECEIVER", action);
        Uri uri = intent.getData();
        if (action.equals(ACTION_UPDATE)) {
            Intent i = new Intent(context, DownloadService.class);
            i.setAction(DownloadService.ACTION_START_DOWNLOAD);
            i.putExtras(intent.getExtras());
            String url = intent.getExtras().getString("url");
//            Log.e("URL", "url:" + url);
            context.startService(i);
        } else if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
            System.out.println(uri + "被安装了");
        } else if (action.equals(Intent.ACTION_PACKAGE_REPLACED)) {
            System.out.println(uri + "被更新了");
        } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
            System.out.println(uri + "被卸载了");
        }
    }
}
