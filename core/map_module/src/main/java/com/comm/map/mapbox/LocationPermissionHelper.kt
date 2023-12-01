package com.comm.map.mapbox

import android.app.Activity
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager

/**
 * @date 2022/8/25.
 * @author xieyutuo
 * @description 定位权限帮助类
 */
class LocationPermissionHelper() {
    private lateinit var permissionsManager: PermissionsManager

    fun checkPermissions(context: Activity, onMapReady: () -> Unit) {
        if (PermissionsManager.areLocationPermissionsGranted(context)) {
            onMapReady()
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {
                override fun onExplanationNeeded(permissionsToExplain: List<String?>?) {

                }

                override fun onPermissionResult(granted: Boolean) {
                    if (granted) {
                        onMapReady()
                    }
                }
            })
            permissionsManager.requestLocationPermissions(context)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions.copyOf(permissions.size), grantResults)
    }
}