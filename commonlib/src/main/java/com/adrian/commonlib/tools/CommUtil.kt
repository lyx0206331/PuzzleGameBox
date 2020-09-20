package com.adrian.commonlib.tools

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.WindowManager
import com.adrian.commonlib.BuildConfig
import com.adrian.commonlib.application.BaseApplication

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//   ┃　　　┃   神兽保佑
//   ┃　　　┃   代码无BUG！
//   ┃　　　┗━━━┓
//   ┃　　　　　　　┣┓
//   ┃　　　　　　　┏┛
//   ┗┓┓┏━┳┓┏┛
//     ┃┫┫　┃┫┫
//     ┗┻┛　┗┻┛
/**
 * author:RanQing
 * date:2020/5/8 0008 18:00
 * description:
 **/
object CommUtil {
    fun <T : Any> logE(cls: Class<T>, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(cls.simpleName, msg)
        }
    }

    fun getScreenWH(): Display {
        val wm = BaseApplication.instance.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return wm.defaultDisplay
    }

    fun getScreenInfo(context: Context): DisplayMetrics? {
        return context.resources?.displayMetrics
    }

    fun getAppName(context: Context): String? {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun getVersionName(context: Context): String? {
        try {
            return context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    fun isHasSdcard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 安装apk
     * @param context
     * @param apkPath 安装文件路径
     */
    fun installApk(context: Context, apkPath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(
            Uri.parse("file://$apkPath"),
            "application/vnd.android.package-archive"
        )
        context.startActivity(intent)
    }

    /**
     * 卸载apk
     * @param context
     * @param pkgName 应用包名
     */
    fun uninstallApk(context: Context, pkgName: String) {
        val intent = Intent(Intent.ACTION_DELETE, Uri.parse("package:$pkgName"))
        return context.startActivity(intent)
    }
}