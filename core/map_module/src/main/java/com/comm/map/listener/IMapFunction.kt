package com.comm.map.listener

import android.app.Activity
import android.graphics.Bitmap
import com.Wisky.map.annotation.*
import com.Wisky.map.bean.*
import com.Wisky.map.options.*
import com.comm.map.annotation.WiskyCircleAnnotation
import com.comm.map.annotation.WiskyPointAnnotation
import com.comm.map.annotation.WiskyPolyLineAnnotation
import com.comm.map.annotation.WiskyPolygonAnnotation
import com.comm.map.annotation.WiskyViewAnnotation
import com.comm.map.bean.WiskyCameraOptions
import com.comm.map.bean.WiskyGesturesSettings
import com.comm.map.bean.WiskyLatLng
import com.comm.map.bean.WiskyMapStyle
import com.comm.map.bean.WiskyScreenCoordinate
import com.comm.map.options.WiskyBaseOptions
import com.comm.map.options.WiskyCircleAnnotationOptions
import com.comm.map.options.WiskyPointAnnotationOptions
import com.comm.map.options.WiskyPolygonAnnotationOptions
import com.comm.map.options.WiskyPolylineAnnotationOptions
import com.comm.map.options.WiskyViewAnnotationOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.ScreenCoordinate
import java.util.*

/**
 * @date 2022/7/14.
 * @author xieyutuo
 * @description 地图绘制接口和其他设置类接口
 */
interface IMapFunction {

    /** 添加图标 */
    fun addPoint(options: WiskyPointAnnotationOptions): WiskyPointAnnotation
    /** 批量添加同类型图标 */
    fun addPoints(optionsList: List<WiskyPointAnnotationOptions>): List<WiskyPointAnnotation>

    /** 更新图标 */
    fun updatePoint(annotation: WiskyPointAnnotation)
    /** 批量更新同类型图标 */
    fun updatePoints(annotations: List<WiskyPointAnnotation>)

    /** 删除图标 */
    fun deletePoint(annotation: WiskyPointAnnotation)
    /**批量删除同类型的图标 */
    fun deletePoints(annotationList: List<WiskyPointAnnotation>)

    fun addPoint(optionList: List<WiskyPointAnnotationOptions>)

    /** 添加线段 */
    fun addPolyline(options: WiskyPolylineAnnotationOptions): WiskyPolyLineAnnotation

    /** 更新线段 */
    fun updatePolyline(annotation: WiskyPolyLineAnnotation)

    /** 删除线段 */
    fun deletePolyline(annotation: WiskyPolyLineAnnotation)

    /** 添加圆 */
    fun addCircle(options: WiskyCircleAnnotationOptions): WiskyCircleAnnotation

    /** 更新圆 */
    fun updateCircle(annotation: WiskyCircleAnnotation)

    /** 删除圆 */
    fun deleteCircle(annotation: WiskyCircleAnnotation)

    /** 添加多边形 */
    fun addPolygon(options: WiskyPolygonAnnotationOptions): WiskyPolygonAnnotation

    /** 更新多边形 */
    fun updatePolygon(annotation: WiskyPolygonAnnotation)

    /** 删除圆或多边形 */
    fun deletePolygonOption(annotation: WiskyBaseOptions)

    /** 删除多边形 */
    fun deletePolygon(annotation: WiskyPolygonAnnotation)

    /** 添加自定义view */
    fun addView(options: WiskyViewAnnotationOptions): WiskyViewAnnotation

    /** 更新自定义View */
    fun updateView(annotation: WiskyViewAnnotation)

    /** 删除自定义View */
    fun deleteView(annotation: WiskyViewAnnotation)

    /** 设置地图Camera参数*/
    fun setCamera(cameraOptions: WiskyCameraOptions)

    /** 设置地图样式*/
    fun loadStyle(activity: Activity, mapStyle: WiskyMapStyle, locale: Locale?, listener: WiskyStyleLoaded?)

    /**
     * 2D拼图
     */
    fun addImageRasterSource(path: String)

    /** 获取地图样式*/
    fun getMapStyle(): WiskyMapStyle

    /** 销毁地图组件*/
    fun onDestroy()

    /** 添加地图手势控制*/
    fun addGestureControl(settings: WiskyGesturesSettings)



    /** 经纬度转坐标*/
    fun pixelForCoordinate(latLng: WiskyLatLng): WiskyScreenCoordinate

    /** 坐标转经纬度*/
    fun coordinateForPixel(pixel: WiskyScreenCoordinate): WiskyLatLng

    /** 获取地图中心点经纬度*/
    fun cameraCenterCoordinate(): WiskyLatLng

    /** 获取相机的参数*/
    fun cameraState(): WiskyCameraOptions

    /** 设置地图的语言 */
    fun setLanguage(locale: Locale?)

    /** 地图处于空闲状态，没有绘制任务（1s内），默认会执行一次 */
    fun addOnCameraIdleListener(listener: WiskyCameraIdleListener)

    /** 控制指南针的显示和隐藏*/
    fun setCompassEnable(enable: Boolean)

    /** 控制logo的显示和隐藏*/
    fun setLogoEnable(enable: Boolean)

    /** 控制缩放尺的显示和隐藏*/
    fun setScaleBarEnable(enable: Boolean)

    fun pixelsForCoordinates(coordinates: List<Point>): List<ScreenCoordinate>

    fun addImageIconToStyle(name: String, image: Bitmap)

    /** 触发重新绘制*/
    fun triggerRepaint()

}