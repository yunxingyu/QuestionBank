package com.comm.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.DrawableRes
import com.comm.map.annotation.*
import com.comm.map.bean.*
import com.comm.map.listener.*
import com.comm.map.options.*
import com.comm.map.annotation.WiskyAnnotation
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
import com.comm.map.bean.MapType
import com.comm.map.listener.WiskyCameraIdleListener
import com.comm.map.listener.WiskyCameraMoveListener
import com.comm.map.listener.WiskyStyleLoaded
import com.comm.map.listener.IMapFunction
import com.comm.map.listener.IMapListener
import com.comm.map.listener.OnWiskyAnnotationClickListener
import com.comm.map.listener.OnWiskyAnnotationDragLister
import com.comm.map.listener.OnWiskyAnnotationInteractionListener
import com.comm.map.listener.OnWiskyAnnotationLongClickListener
import com.comm.map.listener.OnWiskyCameraChangeListener
import com.comm.map.listener.OnWiskyMapClickLister
import com.comm.map.listener.OnWiskyPositionChangedListener
import com.comm.map.listener.OnWiskyScaleListener
import com.comm.map.mapbox.WiskyMapBoxImp
import com.comm.map.options.WiskyBaseOptions
import com.comm.map.options.WiskyCircleAnnotationOptions
import com.comm.map.options.WiskyPointAnnotationOptions
import com.comm.map.options.WiskyPolygonAnnotationOptions
import com.comm.map.options.WiskyPolylineAnnotationOptions
import com.comm.map.options.WiskyViewAnnotationOptions
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.WellKnownTileServer
import com.mapbox.maps.*
import com.mapbox.maps.plugin.delegates.listeners.OnMapLoadedListener
import java.util.*

/**
 * @date 2022/7/14.
 * @author xieyutuo
 * @description 地图的绘制和交互的manager，供外面调用
 */
@SuppressLint("ClickableViewAccessibility")
class MapManager constructor(val context: Context, var type: MapType? = MapType.MAPTILER) :
    IMapFunction,
    IMapListener {

    private lateinit var WiskyMapView: WiskyMapView
    private var mapView: MapView? = null
    private var mapTilerView: com.mapbox.mapboxsdk.maps.MapView? = null
    private var mapHasLoaded = false
    private var onTouchListener: View.OnTouchListener? = null
    private val uiHandler = Handler(Looper.getMainLooper())

    private val mapLoadListener = OnMapLoadedListener {
        mapHasLoaded = true
        WiskyLog.d("MapManager", "onMapLoaded")
    }

    init {
        if (type == MapType.MAPBOX) {
            mapHasLoaded = false
            mapView = MapView(context)
            mapView?.setOnTouchListener { v, event ->
                onTouchListener?.let {
                    it.onTouch(v, event)
                }
                false
            }
            WiskyMapView = WiskyMapBoxImp(mapView!!)
            mapView?.getMapboxMap()?.addOnMapLoadedListener(mapLoadListener)
        } else if (type == MapType.MAPTILER) {
            Mapbox.getInstance(context, WiskyMapTilerImp.mapTileKey, WellKnownTileServer.MapTiler)
            mapTilerView = com.mapbox.mapboxsdk.maps.MapView(context)
            mapTilerView?.let {
                WiskyMapView = WiskyMapTilerImp(it)
            }
        }
    }

    fun getMapView(): View? {
        if (type == MapType.MAPBOX) {
            return mapView
        } else if (type == MapType.MAPTILER) {
            return mapTilerView
        }
        return null
    }

    override fun addPoint(options: WiskyPointAnnotationOptions): WiskyPointAnnotation {
        WiskyLog.d("MapManager", "mapHasLoaded=$mapHasLoaded")
        return WiskyMapView.addPoint(options)
    }

    override fun addPoints(optionsList: List<WiskyPointAnnotationOptions>): List<WiskyPointAnnotation> {
        return WiskyMapView.addPoints(optionsList)
    }

    @Deprecated(message = "无法真正批量添加,可以使用addPoints")
    override fun addPoint(optionList: List<WiskyPointAnnotationOptions>) {
        WiskyMapView.addPoint(optionList)
    }

    fun addOnTouchListener(onTouchListener: View.OnTouchListener) {
        this.onTouchListener = onTouchListener
    }

    override fun onDestroy() {
        WiskyMapView.onDestroy()
        mapView?.getMapboxMap()?.let {
            it.removeOnMapLoadedListener(mapLoadListener)
        }
    }

    override fun addGestureControl(settings: WiskyGesturesSettings) {
        WiskyMapView.addGestureControl(settings)
    }

    override fun pixelForCoordinate(latLng: WiskyLatLng): WiskyScreenCoordinate {
        return WiskyMapView.pixelForCoordinate(latLng)
    }

    override fun coordinateForPixel(pixel: WiskyScreenCoordinate): WiskyLatLng {
        return WiskyMapView.coordinateForPixel(pixel)
    }

    override fun cameraCenterCoordinate(): WiskyLatLng {
        return WiskyMapView.cameraCenterCoordinate()
    }

    override fun cameraState(): WiskyCameraOptions {
        return WiskyMapView.cameraState()
    }

    override fun setLanguage(locale: Locale?) {
        WiskyMapView.setLanguage(locale)
    }

    override fun addOnCameraIdleListener(listener: WiskyCameraIdleListener) {
        WiskyMapView.addOnCameraIdleListener(listener)
    }

    override fun setCompassEnable(enable: Boolean) {
        WiskyMapView.setCompassEnable(enable)
    }

    override fun setLogoEnable(enable: Boolean) {
        WiskyMapView.setLogoEnable(enable)
    }

    override fun setScaleBarEnable(enable: Boolean) {
        WiskyMapView.setScaleBarEnable(enable)
    }

    override fun pixelsForCoordinates(coordinates: List<Point>): List<ScreenCoordinate> {
        return WiskyMapView.pixelsForCoordinates(coordinates)
    }

    override fun addImageIconToStyle(name: String, image: Bitmap) {
        uiHandler.post { WiskyMapView.addImageIconToStyle(name, image) }
    }

    override fun triggerRepaint() {
        WiskyMapView.triggerRepaint()
    }

    override fun updatePoint(annotation: WiskyPointAnnotation) {
        WiskyMapView.updatePoint(annotation)
    }

    override fun updatePoints(annotations: List<WiskyPointAnnotation>) {
        WiskyMapView.updatePoints(annotations)
    }

    override fun deletePoint(annotation: WiskyPointAnnotation) {
        WiskyMapView.deletePoint(annotation)
    }

    override fun deletePoints(annotationList: List<WiskyPointAnnotation>) {
        WiskyMapView.deletePoints(annotationList)
    }

    override fun deletePolygonOption(annotation: WiskyBaseOptions) {
        WiskyMapView.deletePolygonOption(annotation)
    }

    override fun addPolyline(options: WiskyPolylineAnnotationOptions): WiskyPolyLineAnnotation {
        return WiskyMapView.addPolyline(options)
    }

    override fun updatePolyline(annotation: WiskyPolyLineAnnotation) {
        WiskyMapView.updatePolyline(annotation)
    }

    override fun deletePolyline(annotation: WiskyPolyLineAnnotation) {
        WiskyMapView.deletePolyline(annotation)
    }

    override fun addPolygon(options: WiskyPolygonAnnotationOptions): WiskyPolygonAnnotation {
        return WiskyMapView.addPolygon(options)
    }

    override fun updatePolygon(annotation: WiskyPolygonAnnotation) {
        WiskyMapView.updatePolygon(annotation)
    }

    override fun deletePolygon(annotation: WiskyPolygonAnnotation) {
        WiskyMapView.deletePolygon(annotation)
    }

    override fun addCircle(options: WiskyCircleAnnotationOptions): WiskyCircleAnnotation {
        return WiskyMapView.addCircle(options)
    }

    override fun updateCircle(annotation: WiskyCircleAnnotation) {
        WiskyMapView.updateCircle(annotation)
    }

    override fun deleteCircle(annotation: WiskyCircleAnnotation) {
        WiskyMapView.deleteCircle(annotation)
    }

    override fun addView(options: WiskyViewAnnotationOptions): WiskyViewAnnotation {
        return WiskyMapView.addView(options)
    }

    override fun updateView(annotation: WiskyViewAnnotation) {
        WiskyMapView.updateView(annotation)
    }

    override fun deleteView(annotation: WiskyViewAnnotation) {
        WiskyMapView.deleteView(annotation)
    }

    override fun setCamera(cameraOptions: WiskyCameraOptions) {
        WiskyMapView.setCamera(cameraOptions)
    }

    override fun loadStyle(
        activity: Activity,
        mapStyle: WiskyMapStyle,
        locale: Locale?,
        listener: WiskyStyleLoaded?
    ) {
        WiskyMapView.loadStyle(activity, mapStyle, locale, listener)
    }

    override fun addImageRasterSource(path: String) {
        WiskyMapView.addImageRasterSource(path)
    }

    override fun getMapStyle(): WiskyMapStyle {
        return WiskyMapView.getMapStyle()
    }

    override fun addMapClickLister(listener: OnWiskyMapClickLister) {
        WiskyMapView.addMapClickLister(listener)
    }

    override fun removeMapClickLister(listener: OnWiskyMapClickLister) {
        WiskyMapView.removeMapClickLister(listener)
    }

    override fun addAnnotationClickLister(listener: OnWiskyAnnotationClickListener<WiskyAnnotation<*>>) {
        WiskyMapView.addAnnotationClickLister(listener)
    }

    override fun removeAnnotationClickLister(listener: OnWiskyAnnotationClickListener<WiskyAnnotation<*>>) {
        WiskyMapView.removeAnnotationClickLister(listener)
    }

    override fun addAnnotationInteractionLister(listener: OnWiskyAnnotationInteractionListener<WiskyAnnotation<*>>) {
        WiskyMapView.addAnnotationInteractionLister(listener)
    }

    override fun removeAnnotationInteractionLister(listener: OnWiskyAnnotationInteractionListener<WiskyAnnotation<*>>) {
        WiskyMapView.removeAnnotationInteractionLister(listener)
    }

    override fun addAnnotationLongClickListener(listener: OnWiskyAnnotationLongClickListener<WiskyAnnotation<*>>) {
        WiskyMapView.addAnnotationLongClickListener(listener)
    }

    override fun removeAnnotationLongClickListener(listener: OnWiskyAnnotationLongClickListener<WiskyAnnotation<*>>) {
        WiskyMapView.removeAnnotationLongClickListener(listener)
    }

    override fun addAnnotationDragListener(listener: OnWiskyAnnotationDragLister<WiskyAnnotation<*>>) {
        WiskyMapView.addAnnotationDragListener(listener)
    }

    override fun removeAnnotationDragListener(listener: OnWiskyAnnotationDragLister<WiskyAnnotation<*>>) {
        WiskyMapView.removeAnnotationDragListener(listener)
    }

    override fun addScaleListener(listener: OnWiskyScaleListener) {
        WiskyMapView.addScaleListener(listener)
    }

    override fun removeScaleListener(listener: OnWiskyScaleListener) {
        WiskyMapView.removeScaleListener(listener)
    }

    override fun addPositionChangeListener(listener: OnWiskyPositionChangedListener) {
        WiskyMapView.addPositionChangeListener(listener)
    }

    override fun removePositionChangeListener(listener: OnWiskyPositionChangedListener) {
        WiskyMapView.removePositionChangeListener(listener)
    }

    override fun addOnCameraMoveListener(listener: WiskyCameraMoveListener) {
        WiskyMapView.addOnCameraMoveListener(listener)
    }

    override fun removeCameraMoveListener(listener: WiskyCameraMoveListener) {
        WiskyMapView.removeCameraMoveListener(listener)
    }

    override fun addOnCameraChangeListener(listener: OnWiskyCameraChangeListener) {
        WiskyMapView.addOnCameraChangeListener(listener)
    }

    override fun removeCameraChangeListener(listener: OnWiskyCameraChangeListener) {
        WiskyMapView.removeCameraChangeListener(listener)
    }


    fun onMapStart() {
        WiskyMapView.onMapStart()
    }

    fun onMapStop() {
        WiskyMapView.onMapStop()
    }

    fun onMapDestroy() {
        WiskyMapView.onMapDestroy()
    }

    fun onMapLowMemory() {
        WiskyMapView.onMapLowMemory()
    }

    fun setDefaultMapSettings() {
        WiskyMapView.setDefaultMapSettings()
    }

    fun moveCameraTo(latLng: WiskyLatLng, zoom: Double? = null, duration: Long = 0) {
        WiskyMapView.moveCameraTo(latLng, zoom, duration)
    }

    fun fromCoordinate2Point(coordinate: ScreenCoordinate): Point {
        return WiskyMapView.fromCoordinate2Point(coordinate)
    }

    fun moveBearingCameraTo(
        latLng: WiskyLatLng,
        zoom: Double? = null,
        duration: Long = 0,
        bearing: Double? = null
    ) {
        WiskyMapView.moveBearingCameraTo(latLng, zoom, duration, bearing)
    }

    fun getBearing(): Double {
        return WiskyMapView.getBearing()
    }

    fun addOnMapIdleListener(listener: (start: Long, end: Long) -> Unit) {
        WiskyMapView.addOnMapIdleListener(listener)
    }

    fun setZoom(zoom: Double, duration: Long) {
        WiskyMapView.setZoom(zoom, duration)
    }

    fun calculatePointPixel(startPoint: WiskyLatLng, endpoint: WiskyLatLng): Double {
        return WiskyMapView.calculatePointPixel(startPoint, endpoint)
    }

    fun getZoom(): Double {
        return WiskyMapView.getZoom()
    }

    fun setScaleMap(zoom: Double, screenCoordinate: ScreenCoordinate, duration: Long) {
        WiskyMapView.setScaleMap(zoom, screenCoordinate, duration)
    }

    fun getScalePerPixel(): Double {
        return WiskyMapView.getScalePerPixel()
    }


    fun visualArea(
        list: List<WiskyLatLng>,
        duration: Long?,
        left: Double,
        top: Double,
        right: Double,
        bottom: Double
    ): Double? {
        return WiskyMapView.visualArea(list, duration, left, top, right, bottom)
    }

    fun getMapVertexPoint(): MutableList<Point> {
        return WiskyMapView.getMapVertexPoint()
    }


    fun visualAreaPoint(
        list: List<WiskyLatLng>,
        duration: Long?,
        left: Double,
        top: Double,
        right: Double,
        bottom: Double
    ) {
        return WiskyMapView.visualAreaPoint(list, duration, left, top, right, bottom)
    }

    fun rotate(bearing: Double, duration: Long = 0) {
        WiskyMapView.rotate(bearing, duration)
    }

    fun showUserLocation(@DrawableRes resId: Int) {
        WiskyMapView.showUserLocation(resId)
    }

    fun hideUserLocation() {
        WiskyMapView.hideUserLocation()
    }

    fun setOnSnapshot(listener: (Bitmap) -> Unit) {
        if (type == MapType.MAPBOX) {
            mapView?.snapshot { bitmap ->
                bitmap?.let {
                    listener.invoke(bitmap)
                }
            }
        }
        when (type) {
            MapType.MAPBOX -> {
                mapView?.snapshot { bitmap ->
                    bitmap?.let {
                        listener.invoke(bitmap)
                    }
                }
            }

            MapType.MAPTILER -> {
                mapTilerView?.let {
                    it.getMapAsync { mapbox ->
                        mapbox.snapshot { bitmap ->
                            listener.invoke(bitmap)
                        }
                    }
                }
            }

            else -> {}
        }


    }

    fun getStyleUri(style: WiskyMapStyle): String{

        return when (style) {
            WiskyMapStyle.NORMAL -> {
                Style.OUTDOORS
            }
            WiskyMapStyle.MIX -> {
                Style.MAPBOX_STREETS
            }
            WiskyMapStyle.THREE_DIMENSIONAL -> {
                Style.OUTDOORS
            }
            WiskyMapStyle.UNKNOWN -> {
                Style.OUTDOORS
            }
        }
    }
}