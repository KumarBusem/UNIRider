package com.uni.rider.common

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

abstract class BaseActivity : Activity() {

    private val mJob: Job by lazy { Job() }
    protected val ioScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO + mJob) }
    protected val uiScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main + mJob) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUi()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        Handler().postDelayed({ hideSystemUi() }, SYSTEM_UI_HIDE_TIME_MILLI)
        return super.dispatchTouchEvent(ev)
    }

    private fun hideSystemUi() {
        // Set the IMMERSIVE flag.
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mJob.isLazyInitialized) mJob.cancel()
    }

    companion object {
        const val SYSTEM_UI_HIDE_TIME_MILLI = 1500L
    }
}