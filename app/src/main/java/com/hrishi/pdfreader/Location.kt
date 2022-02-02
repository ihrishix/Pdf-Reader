package com.hrishi.pdfreader

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

const val RC_LOCATION = 0   //Request Code Location
var LocationUpdateInterval:Long = 10000 //milliSec

class Location : AppCompatActivity() {

    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        requestPermissions()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if(checkCoarseLocationPermission()){
            fusedLocationClient.getCurrentLocation(
        }


    }

    //Need coarse location permission for Fine Location

    private fun checkCoarseLocationPermission() = ActivityCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun checkFineLocationPermission() = ActivityCompat.checkSelfPermission(
        this, android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun checkApiVersion() =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M

    private fun requestPermissions() {
        if (checkApiVersion()) {

            val permissionList = mutableListOf<String>()

            if (!checkCoarseLocationPermission()) {
                permissionList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            if (!checkFineLocationPermission()) {
                permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }

            if (permissionList.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), RC_LOCATION)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RC_LOCATION) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                    if(checkApiVersion()){
                        if(shouldShowRequestPermissionRationale(permissions[i])){
                            showPermissionExplanation()
                        }
                    }
                }
            }
        }
    }

    private fun showPermissionExplanation(){
        Toast.makeText(this, "Need that Permission", Toast.LENGTH_SHORT).show()
    }

    fun createLocationRequest() {
        val locationRequest = LocationRequest.create().apply {
            interval = LocationUpdateInterval
            fastestInterval = LocationUpdateInterval
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

}