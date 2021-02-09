package com.uni.rider

import android.app.Application
import com.flurry.android.FlurryAgent
import com.google.firebase.FirebaseApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


class RiderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/WorkSans-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build())
                ).build())
        FirebaseApp.initializeApp(this)

        FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "QXZ9BNCK9KQ2WGZHQ59Q")
    }

    companion object {
        lateinit var application: Application
    }
}