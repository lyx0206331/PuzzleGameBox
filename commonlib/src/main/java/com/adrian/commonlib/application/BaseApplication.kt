package com.adrian.commonlib.application

import android.app.Application
import com.adrian.commonlib.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter

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
 * date:2020/5/7 0007 17:01
 * description:
 **/
open class BaseApplication: Application() {

    companion object {
        val instance: BaseApplication by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { BaseApplication() }
    }

    override fun onCreate() {
        super.onCreate()
    }
}