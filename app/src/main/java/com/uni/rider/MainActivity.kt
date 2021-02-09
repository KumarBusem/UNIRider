package com.uni.rider

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.uni.rider.common.*
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
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.partial_blocked_version.view.*

class MainActivity : AppCompatActivity() {

    private val repoFirestore: DataSourceFirestore by lazy { RepoFirestore() }
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setUpNavigation()

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

    private fun setUpNavigation() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.homeFragment, R.id.salaryFragment, R.id.feedbackFragment, R.id.profileFragment), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main2, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
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

    fun setBottomBarVisibility(shouldDisplay: Boolean) {

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
