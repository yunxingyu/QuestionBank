package com.comm.map.listener

import com.mapbox.maps.extension.observable.eventdata.CameraChangedEventData

/**
 * @date 2022/8/17.
 * @author xieyutuo
 * @description 地图Camera 变化
 */
interface OnWiskyCameraChangeListener {

    fun onCameraChange(eventData: CameraChangedEventData)
}