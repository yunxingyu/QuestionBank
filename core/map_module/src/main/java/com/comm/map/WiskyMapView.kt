package com.comm.map

import androidx.annotation.DrawableRes
import com.comm.map.bean.WiskyLatLng
import com.comm.map.listener.IMapFunction
import com.comm.map.listener.IMapListener
import com.mapbox.geojson.Point
import com.mapbox.maps.ScreenCoordinate

/**
 * @date 2022/7/14.
 * @author xieyutuo
 * @description 地图组件的抽象类
 */
abstract class WiskyMapView : IMapFunction, IMapListener {

    /** 控制地图的基本设置，如指南针和logo的隐藏，手势的控制*/
    abstract fun setDefaultMapSettings()

    /**
     * 移动地图到指定的位置
     * @param zoom 缩放等级
     * @param duration 动画时长，大于0，有动画
     */
    abstract fun moveCameraTo(latLng: WiskyLatLng, zoom: Double?, duration: Long?)


    /**
     * 屏幕坐标投影到地图坐标
     */
    abstract fun fromCoordinate2Point(coordinate: ScreenCoordinate): Point

    /**
     * 移动地图到指定的位置,带旋转角度
     * @param zoom 缩放等级
     * @param duration 动画时长，大于0，有动画
     */
    abstract fun moveBearingCameraTo(latLng: WiskyLatLng, zoom: Double? = null, duration: Long = 0, bearing: Double? = null)

    /**
     * 设置相关点在地图中可见 中心点可变
     */
    abstract fun visualArea(list: List<WiskyLatLng>, duration: Long?, left: Double, top: Double, right: Double, bottom: Double): Double?

    /**
     * 获取地图四个角的坐标
     */
    abstract fun getMapVertexPoint(): MutableList<Point>

    /**
     * 设置相关点地图中可见，中心点由多个点决定
     */
    abstract fun visualAreaPoint(list: List<WiskyLatLng>, duration: Long?, left: Double, top: Double, right: Double, bottom: Double)

    /**
     * 设置地图缩放
     */
    abstract fun setZoom(zoom: Double?, duration: Long?)

    /**
     * 获取地图缩放级别
     */
    abstract fun getZoom(): Double

    /***
     * 获取地图旋转角度
     */
    abstract fun getBearing(): Double

    /**
     * 设置最大缩放级别
     */
    abstract fun setMaxZoom(zoom: Double)

    abstract fun setScaleMap(zoom: Double, screenCoordinate: ScreenCoordinate, duration: Long)

    /**
     * 获取比例尺
     */
    abstract fun getScalePerPixel(): Double

    /**
     * 计算两点之间的屏幕距离
     */
    abstract fun calculatePointPixel(startPoint: WiskyLatLng, endpoint: WiskyLatLng): Double

    /**
     * 旋转地图
     * @param bearing 旋转角度
     */
    abstract fun rotate(bearing: Double, duration: Long?)

    /**
     * 显示用户的位置
     * @param resId 用户位置对应的图标
     */
    abstract fun showUserLocation(@DrawableRes resId: Int)

    /**
     * 隐藏用户位置
     */
    abstract fun hideUserLocation()

    abstract fun onMapStart()

    abstract fun onMapStop()

    abstract fun onMapDestroy()

    abstract fun onMapLowMemory()

    abstract fun addOnMapIdleListener(listener: (start: Long, end: Long) -> Unit)

}