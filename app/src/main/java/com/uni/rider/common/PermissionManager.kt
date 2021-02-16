package com.uni.rider.common

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class PermissionManager(private val host: Fragment) {

    fun areAllPermissionsGranted(): Boolean = checkPermissions()

    fun requestAllPermissions() {
        takePermissions()
    }

    private fun checkPermissions(): Boolean {
        return !(ActivityCompat.checkSelfPermission(host.requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(host.requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(host.requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(host.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(host.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }

    private fun takePermissions() {
        host.requestPermissions(arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_REQUEST_CODE)
    }

    companion object {
        const val PERMISSION_REQUEST_CODE: Int = 100
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }
}