package com.comm.map.listener

import com.comm.map.bean.WiskyLatLng

/**
 * @date 2022/8/25.
 * @author xieyutuo
 * @description 手机位置发生变化时的监听
 */
interface OnWiskyPositionChangedListener {

    fun onIndicatorPositionChanged(latLng: WiskyLatLng)
}