package com.adrian.modulegomoku.tools;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.adrian.commonlib.tools.CommUtil;
import com.adrian.modulegomoku.application.GomokuApplication;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by adrian on 17-1-11.
 */

@RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
public class DownloadTask extends AsyncTask<String, Integer, String> {
    private Context context;
    private PowerManager.WakeLock mWakeLock;
    private String apkPath;

    public DownloadTask(Context context, String apkPath) {
        this.context = context;
        this.apkPath = apkPath;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        GomokuApplication.Companion.getInstance().setDownloading(true);
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }
            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();
            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(apkPath);
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return "download stoped!";
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
            output.flush();
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
//        Log.e("DOWNLOAD", "download percent:" + progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        GomokuApplication.Companion.getInstance().setDownloading(false);
        mWakeLock.release();
        if (!TextUtils.isEmpty(result)) {
            Toast.makeText(context, "download failed!" + result, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "download success!", Toast.LENGTH_SHORT).show();
            CommUtil.INSTANCE.installApk(context, apkPath);
        }
    }
}
