package com.comm.map.bean

/**
 * @date 2022/10/1.
 * @author 航线箭头坐标
 * @description 经纬度 ，角度
 */
data class ArrowlLatLng @JvmOverloads constructor(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var altitude: Double = 0.0,
    var rotate: Double = 0.0,
)



