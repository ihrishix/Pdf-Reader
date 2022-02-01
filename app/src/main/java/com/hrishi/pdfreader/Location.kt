package com.hrishi.pdfreader

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

const val RC_LOCATION = 0   //Request Code Location

class Location : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        requestPermissions()
    }

    //Need coarse location permission for Fine Location

    private fun checkCoarseLocation() = ActivityCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun checkFineLocation() = ActivityCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun checkApiVersion() =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M

    private fun requestPermissions() {
        if (checkApiVersion()) {

            var permissionList = mutableListOf<String>()

            if (!checkCoarseLocation()) {
                permissionList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            if (!checkFineLocation()) {
                permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }

            if (permissionList.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), RC_LOCATION)
            }
        }
    }

}