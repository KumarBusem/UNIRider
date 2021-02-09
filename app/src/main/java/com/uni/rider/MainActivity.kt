package com.uni.rider

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.dcartlogistics.rider.common.*
import com.uni.data.dataSources.definitions.DataSourceFirestore
import com.uni.data.dataSources.repos.RepoFirestore
import com.uni.rider.common.hide
import com.uni.rider.common.isGpsAvailable
import com.uni.rider.common.isNetworkAvailable
import com.uni.rider.common.show
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.partial_blocked_version.view.*

class MainActivity : AppCompatActivity() {

    protected val repoFirestore: DataSourceFirestore by lazy { RepoFirestore() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()

        getAppSettings()

        setAppUpdteButtons()

        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/WorkSans-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build())
                ).build())

        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun getAppSettings() {
        repoFirestore.getAppSettings { settings ->
            Log.e("SETTINGS::", settings.toString())
            if (settings == null) return@getAppSettings
            if (settings.latestVersion != BuildConfig.VERSION_NAME) {
                plBlockedVersion.show()
                plBlockedVersion.tvHeading.text = "Unsupported App Version\n${BuildConfig.VERSION_NAME}"

            } else {
                plBlockedVersion.hide()
            }
            if (settings.underMaintenance == true) {
                plUnderMaintenance.show()
            } else {
                plUnderMaintenance.hide()
            }
        }
    }

    private fun setupNavigation() {
        val navController = Navigation.findNavController(this, R.id.mainNavigationFragmentLicker)
        NavigationUI.setupWithNavController(bottomNavigationViewLicker, navController)
    }

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.mainNavigationFragmentLicker).navigateUp()

    fun setBottomBarVisibility(shouldDisplay: Boolean) {
        if (shouldDisplay) bottomNavigationViewLicker?.visibility =
                View.VISIBLE else bottomNavigationViewLicker?.visibility = View.GONE
    }

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isNetworkAvailable(this@MainActivity)) plNoInternet.hide()
            else plNoInternet.show()
        }
    }
    private val gpsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isGpsAvailable(this@MainActivity)) plNoGPS.hide()
            else plNoGPS.show()
        }
    }

    override fun onStart() {
        super.onStart()
        val networkIntentFilter = IntentFilter(CONNECTION)
        val gpsIntentFilter = IntentFilter(GPS)
        this.registerReceiver(networkReceiver, networkIntentFilter)
        this.registerReceiver(gpsReceiver, gpsIntentFilter)

        if (isGpsAvailable(this@MainActivity)) plNoGPS.hide()
        else plNoGPS.show()
    }

    override fun onStop() {
        super.onStop()
        this.unregisterReceiver(networkReceiver)
        this.unregisterReceiver(gpsReceiver)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    companion object {
        const val CONNECTION = "android.net.conn.CONNECTIVITY_CHANGE"
        const val GPS = "android.location.PROVIDERS_CHANGED"
    }

    private fun setAppUpdteButtons() {
        plBlockedVersion.btnUpdatePlaystore.setOnClickListener {
            val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.dcartlogistics.rider")
            )
            startActivity(browserIntent)
        }
        plBlockedVersion.btnUpdate.setOnClickListener {
            val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://drive.google.com/file/d/1NZ0sfyqtGq7gZxb2N0E1Z16I3EmQY-3Q/view?usp=sharing")
            )
            startActivity(browserIntent)
        }
    }
}
