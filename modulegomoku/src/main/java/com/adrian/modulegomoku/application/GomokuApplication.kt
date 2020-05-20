package com.adrian.modulegomoku.application

import com.adrian.commonlib.application.BaseApplication
import com.adrian.commonlib.tools.CommUtil
import com.adrian.modulegomoku.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import com.miui.zeus.mimo.sdk.MimoSdk
import com.miui.zeus.mimo.sdk.api.IMimoSdkListener
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

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
 * date:2020/5/7 0007 17:10
 * description:
 **/
class GomokuApplication: BaseApplication() {
    companion object {
        const val APP_ID = "2882303761517539266"
        const val TEST_APP_ID = "2882303761517411490"

        const val APP_KEY = "fake_app_key"
        const val APP_TOKEN = "fake_app_token"

        val instance: GomokuApplication by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { GomokuApplication() }
    }

    var isDownloading = false

    override fun onCreate() {
        super.onCreate()

        initUmengSdk()
        initMimoSdk()
    }

    private fun initUmengSdk() {
        //初始化友盟SDK
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null)
        //选用MANUAL页面采集数据
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL)
        //集成测试
        if (BuildConfig.DEBUG) {
            //打开统计SDK调试模式
            UMConfigure.setLogEnabled(true)
        }
    }

    private fun initMimoSdk() {
        // 如果担心sdk自升级会影响开发者自身app的稳定性可以关闭，
        // 但是这也意味着您必须得重新发版才能使用最新版本的sdk, 建议开启自升级
        MimoSdk.setEnableUpdate(true)

        if (BuildConfig.DEBUG) {
            MimoSdk.setDebug(true) // 正式上线时候务必关闭debug模式
            MimoSdk.setStaging(true) // 正式上线时候务必关闭stage模式
        }

        // 如需要在本地预置插件,请在assets目录下添加mimo_asset.apk;
        MimoSdk.init(this, APP_ID, APP_KEY, APP_TOKEN, object : IMimoSdkListener {
            override fun onSdkInitSuccess() {
                CommUtil.logE(GomokuApplication::class.java, "MimoSdk init success")
            }

            override fun onSdkInitFailed() {
                CommUtil.logE(GomokuApplication::class.java, "MimoSdk init failed")
            }

        })
    }
}