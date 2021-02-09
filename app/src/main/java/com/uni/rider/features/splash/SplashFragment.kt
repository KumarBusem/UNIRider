package com.uni.rider.features.splash

import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uni.rider.BuildConfig
import com.uni.rider.R
import com.uni.rider.common.BaseFragment
import com.uni.rider.common.PermissionManager
import com.uni.rider.common.PermissionManager.Companion.PERMISSION_REQUEST_CODE
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.delay


class SplashFragment : BaseFragment() {

    private val mPermissionManager: PermissionManager by lazy { PermissionManager(this@SplashFragment) }
    private var snackbarShow: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        toggleBottomBarVisibility(false)
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateNext()
    }

    private fun navigateNext() {

        uiScope.launch {
            if (mPermissionManager.areAllPermissionsGranted()) {
                delay(SPLASH_DURATION)
                checkUserAuth()
            } else {
                mPermissionManager.requestAllPermissions()
            }
        }
    }

    private fun checkUserAuth() {
        val user = repoPrefs.getLoggedInUser()
        if (user == null || user.token.isNullOrEmpty()) {
            navigateById(R.id.action_splashFragment_to_loginFragment)
        } else {
            navigateById(R.id.action_splashFragment_to_homeFragment)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var arePermissionsGranted = true
        if (requestCode == PERMISSION_REQUEST_CODE) {
            grantResults.forEach { if (it != PERMISSION_GRANTED) arePermissionsGranted = false }
            if (!arePermissionsGranted) {
                snackbarShow = true
                showSettingSnackBar()
            } else navigateNext()
        }
    }

    private fun showSettingSnackBar() {
        if (snackbarShow)
            Snackbar.make(requireView(), "Please give permissions", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Settings") {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null)
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    }
                    .show()
    }

    override fun onResume() {
        if (mPermissionManager.areAllPermissionsGranted()) {
            checkUserAuth()

        } else {
            showSettingSnackBar()
        }
        super.onResume()
    }


    companion object {
        private const val SPLASH_DURATION: Long = 2000
    }

}
