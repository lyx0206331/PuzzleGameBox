package com.adrian.modulegomoku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.adrian.commonlib.BuildConfig
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.modulegomoku_activity_main.*

@Route(path = "/modulegomoku/activity")
class MainActivity : AppCompatActivity() {

    @Autowired
    @JvmField var name: String? = null

    @Autowired
    @JvmField var value: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        if (BuildConfig.isModule) {
            ARouter.getInstance().inject(this)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modulegomoku_activity_main)

//        tvTest.text = "name:$name, value:$value"
    }
}
