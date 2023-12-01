package com.comm.map.mapbox

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import com.Wisky.log.WiskyLog
import com.comm.map.WiskyMapView
import com.comm.map.annotation.*
import com.comm.map.bean.*
import com.comm.map.generated.WiskyTextAnchor
import com.comm.map.generated.WiskyTextJustify
import com.comm.map.listener.*
import com.comm.map.options.*
import com.comm.map.util.*
import com.comm.map.annotation.WiskyAnnotation
import com.comm.map.annotation.WiskyCircleAnnotation
import com.comm.map.annotation.WiskyImageAnnotation
import com.comm.map.annotation.WiskyPointAnnotation
import com.comm.map.annotation.WiskyPolyLineAnnotation
import com.comm.map.annotation.WiskyPolygonAnnotation
import com.comm.map.annotation.WiskyViewAnnotation
import com.comm.map.bean.WiskyCameraOptions
import com.comm.map.bean.WiskyEdgeInsets
import com.comm.map.bean.WiskyGesturesSettings
import com.comm.map.bean.WiskyLatLng
import com.comm.map.bean.WiskyMapStyle
import com.comm.map.bean.WiskyScaleGesture
import com.comm.map.bean.WiskyScreenCoordinate
import com.comm.map.listener.WiskyCameraIdleListener
import com.comm.map.listener.WiskyCameraMoveListener
import com.comm.map.listener.WiskyStyleLoaded
import com.comm.map.listener.OnWiskyAnnotationClickListener
import com.comm.map.listener.OnWiskyAnnotationDragLister
import com.comm.map.listener.OnWiskyAnnotationInteractionListener
import com.comm.map.listener.OnWiskyAnnotationLongClickListener
import com.comm.map.listener.OnWiskyCameraChangeListener
import com.comm.map.listener.OnWiskyMapClickLister
import com.comm.map.listener.OnWiskyPositionChangedListener
import com.comm.map.listener.OnWiskyScaleListener
import com.comm.map.options.WiskyBaseOptions
import com.comm.map.options.WiskyCircleAnnotationOptions
import com.comm.map.options.WiskyImageAnnotationOptions
import com.comm.map.options.WiskyPointAnnotationOptions
import com.comm.map.options.WiskyPolygonAnnotationOptions
import com.comm.map.options.WiskyPolylineAnnotationOptions
import com.comm.map.options.WiskyViewAnnotationOptions
import com.comm.map.util.BitmapUtils
import com.google.gson.Gson
import com.mapbox.android.gestures.AndroidGesturesManager
import com.mapbox.android.gestures.WiskyMoveGestureDetector
import com.mapbox.android.gestures.WiskyRotateGestureDetector
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.android.gestures.RotateGestureDetector
import com.mapbox.android.gestures.StandardScaleGestureDetector
import com.mapbox.geojson.Feature
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.*
import com.mapbox.maps.extension.localization.localizeLabels
import com.mapbox.maps.extension.observable.eventdata.CameraChangedEventData
import com.mapbox.maps.extension.observable.eventdata.MapLoadingErrorEventData
import com.mapbox.maps.extension.style.expressions.dsl.generated.literal
import com.mapbox.maps.extension.style.image.addImage
import com.mapbox.maps.extension.style.image.image
import com.mapbox.maps.extension.style.layers.*
import com.mapbox.maps.extension.style.layers.generated.*
import com.mapbox.maps.extension.style.layers.properties.generated.IconRotationAlignment
import com.mapbox.maps.extension.style.layers.properties.generated.SymbolPlacement
import com.mapbox.maps.extension.style.sources.*
import com.mapbox.maps.extension.style.sources.generated.*
import com.mapbox.maps.extension.style.terrain.generated.terrain
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.Plugin
import com.mapbox.maps.plugin.animation.*
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.attribution.AttributionPlugin
import com.mapbox.maps.plugin.compass.CompassPlugin
import com.mapbox.maps.plugin.delegates.listeners.OnCameraChangeListener
import com.mapbox.maps.plugin.delegates.listeners.OnMapLoadErrorListener
import com.mapbox.maps.plugin.gestures.*
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.logo.LogoPlugin
import com.mapbox.maps.plugin.scalebar.ScaleBarPlugin
import org.json.JSONObject
import java.lang.reflect.Field
import java.nio.ByteBuffer
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.sqrt


/**
 * @date 2022/7/14.
 * @author xieyutuo
 * @description MapBox的实现类
 */
class WiskyMapBoxImp constructor(private val mapView: MapView) : WiskyMapView() {

    companion object {
        private const val DOUBLE_CLICK_GAP = 500
        private const val QUERY_WAIT_TIME = 2L
        private const val SOURCE = "TERRAIN_SOURCE"
        private const val TERRAIN_URL_TILE_RESOURCE = "mapbox://mapbox.mapbox-terrain-dem-v1"
        private val TAG = WiskyMapBoxImp::class.java.simpleName
    }

    private val mapboxMap: MapboxMap = mapView.getMapboxMap()

    private val context: Context = mapView.context

    //检测地图多个事件同时上报
    private var lastClickEvent = 0L

    /** Annotation的标识id */
    private var currentId = 0L

    /** 当前拖动的Annotation */
    private var draggingAnnotation: WiskyAnnotation<*>? = null

    /** 已绘制图形的的集合*/
    private val annotationList = ArrayList<WiskyAnnotation<*>>()

    /** 拖动监听集合 */
    private val dragListeners = mutableListOf<OnWiskyAnnotationDragLister<WiskyAnnotation<*>>>()

    /** annotation点击监听集合 */
    private val clickListeners = mutableListOf<OnWiskyAnnotationClickListener<WiskyAnnotation<*>>>()

    /** 非annotation点击监听集合 */
    private val mapClickListeners = mutableListOf<OnWiskyMapClickLister>()

    /** 长按监听集合 */
    private val longClickListeners =
        mutableListOf<OnWiskyAnnotationLongClickListener<WiskyAnnotation<*>>>()

    /** 缩放监听集合 */
    private val scaleListeners = mutableListOf<OnWiskyScaleListener>()

    /** 地图相机移到监听 */
    private val cameraChangeListeners = mutableListOf<OnWiskyCameraChangeListener>()

    /** 位置变化监听集合 */
    private val locationChangeListeners = mutableListOf<OnWiskyPositionChangedListener>()

    private val cameraMoveListener = mutableListOf<WiskyCameraMoveListener>()

    /**
     * 图标，切换样式的时候需要再次添加图标
     */
    private val iconsMap = hashMapOf<String, Bitmap>()

    /** 选中和反选监听集合 */
    private val interactionListener =
        mutableListOf<OnWiskyAnnotationInteractionListener<WiskyAnnotation<*>>>()

    private var gesturesPlugin: GesturesPlugin = mapView.gestures
    private val mapMoveResolver = MapMove()
    private val mapClickResolver = MapClick()
    private val mapLongClickResolver = MapLongClick()
    private val mapScaleResolver = MapScale()
    private val locationChangeResolver = LocationChange()
    private val cameraChange = CameraChange()

    private var lowPolylineManager: PolylineAnnotationManager
    private var lowPointManager: PointAnnotationManager
    private var highPointManager: PointAnnotationManager
    private var polygonManager: PolygonAnnotationManager
    private var polylineManager: PolylineAnnotationManager

    private var annotationMap = mutableMapOf<WiskyAnnotation<*>, Any>()

    /**
     * draw animation
     */
    private val transitionOptions =
        TransitionOptions.Builder().duration(0).delay(0).enablePlacementTransitions(false).build()

    init {
        lowPolylineManager = mapView.annotations.createPolylineAnnotationManager()
        lowPointManager = mapView.annotations.createPointAnnotationManager()
        polygonManager = mapView.annotations.createPolygonAnnotationManager()
        polylineManager = mapView.annotations.createPolylineAnnotationManager()
        highPointManager = mapView.annotations.createPointAnnotationManager()
        lowPointManager.iconRotationAlignment = IconRotationAlignment.MAP
        highPointManager.iconRotationAlignment = IconRotationAlignment.MAP
        highPointManager.symbolPlacement = SymbolPlacement.POINT
        replaceRotateGesture()
        replaceMoveGesture()
        addGesture()

        gesturesPlugin.addOnMoveListener(mapMoveResolver)
        gesturesPlugin.addOnMapClickListener(mapClickResolver)
        gesturesPlugin.addOnMapLongClickListener(mapLongClickResolver)
        gesturesPlugin.addOnScaleListener(mapScaleResolver)
        mapView.getMapboxMap().addOnCameraChangeListener(cameraChange)
    }

    override fun showUserLocation(@DrawableRes resId: Int) {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    context, resId
                ),

                )
        }
        locationComponentPlugin.addOnIndicatorPositionChangedListener(locationChangeResolver)
    }

    override fun hideUserLocation() {
        val locationComponentPlugin = mapView.location
        locationComponentPlugin.updateSettings {
            enabled = false
        }
        locationComponentPlugin.removeOnIndicatorPositionChangedListener(locationChangeResolver)
    }

    override fun onDestroy() {
        gesturesPlugin.removeOnMapClickListener(mapClickResolver)
        gesturesPlugin.removeOnMapLongClickListener(mapLongClickResolver)
        gesturesPlugin.removeOnMoveListener(mapMoveResolver)
        gesturesPlugin.removeOnScaleListener(mapScaleResolver)
        mapView.getMapboxMap().removeOnCameraChangeListener(cameraChange)
        mapView.location.removeOnIndicatorPositionChangedListener(locationChangeResolver)
        dragListeners.clear()
        clickListeners.clear()
        longClickListeners.clear()
        interactionListener.clear()
        scaleListeners.clear()
        locationChangeListeners.clear()
        cameraMoveListener.clear()
        annotationList.clear()

        annotationMap.clear()
        lowPointManager.deleteAll()
        highPointManager.deleteAll()
        polylineManager.deleteAll()
        polygonManager.deleteAll()
    }

    override fun addGestureControl(settings: WiskyGesturesSettings) {
        gesturesPlugin.updateSettings {
            rotateEnabled = settings.rotateEnabled
            pinchToZoomEnabled = settings.pinchToZoomEnabled
            scrollEnabled = settings.scrollEnabled
            simultaneousRotateAndPinchToZoomEnabled =
                settings.simultaneousRotateAndPinchToZoomEnabled
            pitchEnabled = settings.pitchEnabled
            scrollMode = settings.scrollMode
            doubleTapToZoomInEnabled = settings.doubleTapToZoomInEnabled
            doubleTouchToZoomOutEnabled = settings.doubleTouchToZoomOutEnabled
            quickZoomEnabled = settings.quickZoomEnabled
            focalPoint = settings.focalPoint
            pinchToZoomDecelerationEnabled = settings.pinchToZoomDecelerationEnabled
            rotateDecelerationEnabled = settings.rotateDecelerationEnabled
            scrollDecelerationEnabled = settings.scrollDecelerationEnabled
            increaseRotateThresholdWhenPinchingToZoom =
                settings.increaseRotateThresholdWhenPinchingToZoom
            increasePinchToZoomThresholdWhenRotating =
                settings.increasePinchToZoomThresholdWhenRotating
            zoomAnimationAmount = settings.zoomAnimationAmount
            pinchScrollEnabled = settings.pinchScrollEnabled
        }
    }

    override fun pixelForCoordinate(latLng: WiskyLatLng): WiskyScreenCoordinate {
        val pixel =
            mapboxMap.pixelForCoordinate(Point.fromLngLat(latLng.longitude, latLng.latitude))
        return WiskyScreenCoordinate(pixel.x, pixel.y)
    }

    override fun coordinateForPixel(pixel: WiskyScreenCoordinate): WiskyLatLng {
        var pixelx = pixel.x
        var pixely = pixel.y
        if (pixel.x.isNaN()) {
            pixelx = 0.0
        }
        if (pixel.y.isNaN()) {
            pixely = 0.0
        }
        val point = mapboxMap.coordinateForPixel(ScreenCoordinate(pixelx, pixely))
        return pointToWiskyLatLng(point)
    }

    override fun cameraCenterCoordinate(): WiskyLatLng {
        val point = mapboxMap.cameraState.center
        return pointToWiskyLatLng(point)
    }

    private fun pointToWiskyLatLng(point: Point): WiskyLatLng {
        var alt = point.altitude()
        if (alt.isNaN()) {
            alt = 0.0
        }
        return WiskyLatLng(point.latitude(), point.longitude(), alt)
    }

    override fun cameraState(): WiskyCameraOptions {
        val state = mapboxMap.cameraState
        val point = state.center
        val padding = state.padding
        val center = pointToWiskyLatLng(point)
        val WiskyPadding = WiskyEdgeInsets(padding.top, padding.left, padding.bottom, padding.right)
        return WiskyCameraOptions.Builder().center(center).zoom(state.zoom).padding(WiskyPadding)
            .pitch(state.pitch).bearing(state.bearing).build()
    }

    override fun setLanguage(locale: Locale?) {
        locale?.let {
            mapboxMap.getStyle()?.localizeLabels(it)
        }
    }

    override fun setDefaultMapSettings() {
        setCompassEnable(false)
        setLogoEnable(false)
        setScaleBarEnable(false)
        setMapMaxMinZoom(19.0, 0.0)
        addGestureControl(
            WiskyGesturesSettings(
                doubleTapToZoomInEnabled = false,
                doubleTouchToZoomOutEnabled = false,
                rotateEnabled = false,
                pitchEnabled = false
            )
        )
    }

    private fun setMapMaxMinZoom(max: Double, min: Double) {
        val cameraBounds = CameraBoundsOptions.Builder().maxZoom(max).minZoom(min).build()
        mapboxMap.setBounds(cameraBounds)
    }

    override fun moveCameraTo(latLng: WiskyLatLng, zoom: Double?, duration: Long?) {
        val cameraOptions = CameraOptions.Builder()
            .center(Point.fromLngLat(latLng.longitude, latLng.latitude))
            .zoom(zoom)
            .build()
        if (duration == null || duration == 0L) {
            setCamera(cameraOptions)
        } else {
            flyTo(cameraOptions, MapAnimationOptions.Builder().duration(duration).build())
        }
//        mapboxMap.unproject()

    }

    /**
     * 屏幕坐标投影到地图坐标
     */
    override fun fromCoordinate2Point(coordinate: ScreenCoordinate): Point {
        return mapboxMap.coordinateForPixel(coordinate)
    }


    override fun moveBearingCameraTo(
        latLng: WiskyLatLng,
        zoom: Double?,
        duration: Long,
        bearing: Double?
    ) {
        val cameraOptions = CameraOptions.Builder()
            .center(Point.fromLngLat(latLng.longitude, latLng.latitude))
            .zoom(zoom)
            .bearing(bearing)
            .build()
        if (duration == null || duration == 0L) {
            setCamera(cameraOptions)
        } else {
            flyTo(cameraOptions, MapAnimationOptions.Builder().duration(duration).build())
        }
    }

    private fun setCamera(cameraOptions: CameraOptions) {
//        Log.e(TAG, "相机移动 setCamera1：${System.currentTimeMillis()} ")
//        timeSample = System.currentTimeMillis()
        mapboxMap.setCamera(cameraOptions)
//        if (System.currentTimeMillis() - timeSample > 5) {
//            Log.e(TAG, "相机移动 卡顿： ${cameraOptions.anchor}--->${System.currentTimeMillis()}")
//        }

    }

    fun flyTo(cameraOptions: CameraOptions, animationOptions: MapAnimationOptions) {
//        Log.e(TAG, "相机移动 flyTo1：${System.currentTimeMillis()} --->${animationOptions.duration}")
        mapboxMap.flyTo(cameraOptions, animationOptions)
//        Log.e(TAG, "相机移动 flyTo2： ${cameraOptions.anchor}--->${System.currentTimeMillis()}--->${animationOptions.duration}")

    }

    /**
     * 获取四个顶点的坐标
     */
    override fun getMapVertexPoint(): MutableList<Point> {
        var bounds =
            mapboxMap.coordinateBoundsZoomForCamera(mapboxMap.cameraState.toCameraOptions())
        var northwest = bounds.bounds.northwest()
        var southWest = bounds.bounds.southwest
        var northeast = bounds.bounds.northeast
        var southeast = bounds.bounds.southeast()

        var list = mutableListOf<Point>()
        list.add(northwest)
        list.add(northeast)
        list.add(southeast)
        list.add(southWest)
        return list

    }

    override fun visualArea(
        list: List<WiskyLatLng>,
        duration: Long?,
        left: Double,
        top: Double,
        right: Double,
        bottom: Double
    ): Double? {
        val listPoint = arrayListOf<Point>()
        list.forEach { latLng ->
            listPoint.add(Point.fromLngLat(latLng.longitude, latLng.latitude))
        }
        val cameraPosition =
            mapboxMap.cameraForCoordinates(listPoint, EdgeInsets(left, top, right, bottom))

//        mapboxMap.flyTo(cameraPosition, MapAnimationOptions.Builder().duration(duration ?: 0).build())
        return cameraPosition.zoom
    }

    override fun visualAreaPoint(
        list: List<WiskyLatLng>,
        duration: Long?,
        left: Double,
        top: Double,
        right: Double,
        bottom: Double
    ) {
        val listPoint = arrayListOf<Point>()
        list.forEach { latLng ->
            listPoint.add(Point.fromLngLat(latLng.longitude, latLng.latitude))
        }
        val cameraPosition =
            mapboxMap.cameraForCoordinates(listPoint, EdgeInsets(left, top, right, bottom))

        mapboxMap.flyTo(
            cameraPosition,
            MapAnimationOptions.Builder().duration(duration ?: 0).build()
        )
    }

    override fun setZoom(zoom: Double?, duration: Long?) {
        val cameraOptions = CameraOptions.Builder().zoom(zoom).build()
        if (duration == null || duration == 0L) {
            setCamera(cameraOptions)
        } else {
            flyTo(cameraOptions, MapAnimationOptions.Builder().duration(duration).build())
        }

    }


    override fun setMaxZoom(zoom: Double) {

    }

    override fun setScaleMap(zoom: Double, screenCoordinate: ScreenCoordinate, duration: Long) {
//        mapboxMap.scaleBy(zoom, screenCoordinate, MapAnimationOptions.Builder().duration(duration).build())
        mapboxMap.moveBy(screenCoordinate, MapAnimationOptions.Builder().duration(duration).build())
    }


    override fun getZoom(): Double {
        return mapboxMap.cameraState.zoom
    }

    override fun getBearing(): Double {
        return mapboxMap.cameraState.bearing
    }


    override fun getScalePerPixel(): Double {
//        mapboxMap.addOnMapIdleListener()
        return mapboxMap.getMetersPerPixelAtLatitude(
            mapboxMap.getFreeCameraOptions().position?.x ?: 0.0
        )
    }


    override fun calculatePointPixel(startPoint: WiskyLatLng, endPoint: WiskyLatLng): Double {
        var startPixel = mapboxMap.pixelForCoordinate(
            Point.fromLngLat(
                startPoint.longitude,
                startPoint.latitude
            )
        )
        var endPixel =
            mapboxMap.pixelForCoordinate(Point.fromLngLat(endPoint.longitude, endPoint.latitude))

        (startPixel.y - endPixel.y)
        var raduis =
            sqrt(
                abs((abs(startPixel.y) - abs(endPixel.y))) * abs((abs(startPixel.y) - abs(endPixel.y))) +
                        abs(abs(startPixel.x) - abs(endPixel.x)) * abs(
                    abs(startPixel.x) - abs(
                        endPixel.x
                    )
                )
            )
        return raduis
    }


    override fun rotate(bearing: Double, duration: Long?) {
        val cameraOptions = CameraOptions.Builder().bearing(bearing).build()
        if (duration == null || duration == 0L) {
            setCamera(cameraOptions)
        } else {
            flyTo(cameraOptions, MapAnimationOptions.Builder().duration(duration).build())
        }
    }

    @SuppressLint("Lifecycle")
    override fun onMapStart() {
        mapView.onStart()
    }

    @SuppressLint("Lifecycle")
    override fun onMapStop() {
        mapView.onStop()
    }

    override fun onMapDestroy() {
        onDestroy()
        mapView.onDestroy()
    }

    override fun onMapLowMemory() {
        mapView.onLowMemory()
    }

    var count: Long = 0
    var uiHandler = Handler(Looper.getMainLooper())

    override fun addImageRasterSource(path: String) {
        mapboxMap.getStyle()?.let { style ->
            WiskyLog.i("MapperManager", " addImageRasterSource url ： " + path)
            count++
            val currentId = "${"mapper-id"}-${count}"

            WiskyLog.i("瓦片加载时间 进入：", System.currentTimeMillis().toString())
            val tileSet =
                TileSet.Builder(currentId, arrayListOf(path)).maxZoom(0).maxZoom(20).build()
            val source = RasterSource.Builder(currentId).tileSet(tileSet).tileSize(256).build()

            style.addSource(source)
            val rasterLayer = RasterLayer(currentId, currentId)
//                rasterLayer.rasterOpacity(0.0)
            style.addLayerAt(rasterLayer, 10)

            uiHandler.postDelayed({

                if (path.contains("/tile")) {
                    WiskyLog.i("MapperManager", "remove real： " + path)
                    for (i in 0 until count - 1) {
                        val preId = "${"mapper-id"}-${i}"
                        if (style.styleLayerExists(preId) == true) {
                            style.removeStyleLayer(preId)
                        }

                        if (style.styleSourceExists(preId) == true) {
                            style.removeStyleSource(preId)
                        }
                    }
                } else {
                    WiskyLog.i("MapperManager", "remove deal： " + path)
                    for (i in 0 until count) {
                        val preId = "${"mapper-id"}-${i}"
                        if (style.styleLayerExists(preId) == true) {
                            style.removeStyleLayer(preId)
                        }

                        if (style.styleSourceExists(preId) == true) {
                            style.removeStyleSource(preId)
                        }
                    }
                }
            }, 3000)

        }
    }

    override fun addOnMapIdleListener(listener: (start: Long, end: Long) -> Unit) {
        mapboxMap.addOnMapIdleListener { eventData ->
            listener.invoke(
                eventData.begin,
                eventData.end ?: 0
            )
        }
    }

    override fun addPoint(options: WiskyPointAnnotationOptions): WiskyPointAnnotation {
        val annotation = WiskyPointAnnotation(currentId, options)
        val pointOptions = PointAnnotationOptions()
        options.getImageBitmap()?.let { bitmap ->
            options.imageId?.let { imageId ->
                addImageIconToStyle(imageId, bitmap)
            }
        }
        updateAnnotationOptions(pointOptions, options)

        val mapboxAnnotation: PointAnnotation =
            if (options.symbolSortKey != null && options.symbolSortKey!! > 1) {
                highPointManager.create(pointOptions)
            } else {
                lowPointManager.create(pointOptions)
            }
        annotationMap[annotation] = mapboxAnnotation
        addAnnotationSuccess(annotation)
        return annotation
    }

    override fun addPoints(optionsList: List<WiskyPointAnnotationOptions>): List<WiskyPointAnnotation> {
        if (optionsList.isEmpty()) {
            return mutableListOf()
        }
        var pointList = mutableListOf<PointAnnotationOptions>()
        var WiskyPointAnnotations = mutableListOf<WiskyPointAnnotation>()

        optionsList.forEach {
            //如果传入的不是图片id，传入的是bitmap，需要将bitmap先加入到style中
            it.getImageBitmap()?.let { bitmap ->
                it.imageId?.let { imageId ->
                    addImageIconToStyle(imageId, bitmap)
                }
            }
            val annotation = WiskyPointAnnotation(currentId, it)
            val pointOptions = PointAnnotationOptions()
            updateAnnotationOptions(pointOptions, it)

            pointList.add(pointOptions)
            addAnnotationSuccess(annotation)
            WiskyPointAnnotations.add(annotation)
        }

        if (optionsList.isEmpty()) {
            return mutableListOf()
        }

        var options = optionsList[0]
        val mapboxAnnotation: List<PointAnnotation> =
            if (options.symbolSortKey != null && options.symbolSortKey!! > 1) {
                highPointManager.create(pointList)
            } else {
                lowPointManager.create(pointList)
            }

        WiskyPointAnnotations.mapIndexed { index, item ->
            annotationMap[item] = mapboxAnnotation[index]
        }
        return WiskyPointAnnotations
    }

    /** 可以跟随地图自动缩放的图片*/
    fun addImageSource(options: WiskyImageAnnotationOptions) {
        val annotation = WiskyImageAnnotation(currentId, options)
        mapboxMap.getStyle { style ->
            options.getSourceId().let { it ->
                val imageSource = imageSource(it) {
                    options.points?.let { point ->
                        coordinates(point)
                    }
                }
                val layer = rasterLayer(options.getLayerId(), options.getSourceId()) {
                }
                style.addSource(imageSource)
                style.addLayer(layer)
                options.bitmap?.let {
                    val byteBuffer = ByteBuffer.allocate(it.byteCount)
                    it.copyPixelsToBuffer(byteBuffer)
                    imageSource.updateImage(Image(it.width, it.height, byteBuffer.array()))
                }
            }
        }
        addAnnotationSuccess(annotation)
    }

    /** 添加图片到style */
    private fun addIconToStyle(style: Style, options: WiskyPointAnnotationOptions) {
        options.imageId?.let {
            if (it.startsWith(WiskyPointAnnotationOptions.ICON_DEFAULT_NAME_PREFIX)) {
                options.getImageBitmap()?.let { bitmap ->
                    val imagePlugin = image(it) {
                        bitmap(bitmap)
                    }
                    style.addImage(imagePlugin)
                }
            }
        }
    }

    /** 添加Layer到style*/
    private fun addLayerToStyle(style: Style, layer: Layer, options: WiskyBaseOptions) {
        var layerPosition: LayerPosition? = null
        options.belowLayerId?.let { belowLayerId ->
            if (style.styleLayerExists(belowLayerId)) {
                layerPosition = LayerPosition(null, belowLayerId, null)
            }
        }
        if (!style.styleLayerExists(options.getLayerId())) {
            style.addPersistentLayer(layer, layerPosition)
        }
    }

    private fun updateAnnotationOptions(
        mapOptions: PointAnnotationOptions,
        options: WiskyPointAnnotationOptions,
    ) {
        mapOptions.apply {
            val point = Point.fromLngLat(options.latLng.longitude, options.latLng.latitude)
            withGeometry(point)
            options.iconOpacity?.let {
                withIconOpacity(it)
            }
            options.imageId?.let {
                withIconImage(it)
            }
            options.iconAnchor?.let {
                withIconAnchor(it)
            }
            options.iconOffset?.let {
                withIconOffset(it)
            }
            options.iconSize?.let {
                withIconSize(it)
            }
            options.iconRotate?.let {
                withIconRotate(it)
            }
            options.symbolSortKey?.let {
                withSymbolSortKey(it)
            }
            options.textField?.let {
                withTextField(it)
            }
            options.textAnchor?.let {
                withTextAnchor(WiskyTextAnchor.findMapboxEnum(it.value))
            }
            options.textJustify?.let {
                withTextJustify(WiskyTextJustify.findMapboxEnum(it.value))
            }
            options.textLineHeight?.let {
                withTextLineHeight(it)
            }
            options.textMaxWidth?.let {
                withTextMaxWidth(it)
            }
            options.textOffset?.let {
                withTextOffset(it)
            }
            options.textRadialOffset?.let {
                withTextRadialOffset(it)
            }
            options.textRotate?.let {
                withTextRotate(it)
            }
            options.textSize?.let {
                withTextSize(it)
            }
            options.textColorInt?.let {
                withTextColor(it)
            }
            options.textColorString?.let {
                withTextColor(it)
            }
            options.textHaloBlur?.let {
                withTextHaloBlur(it)
            }
            options.textHaloColorInt?.let {
                withTextHaloColor(it)
            }
            options.textHaloColorString?.let {
                withTextHaloColor(it)
            }
            withDraggable(options.isDraggable)
        }
    }

    /** 添加Annotation之后 */
    private fun addAnnotationSuccess(annotation: WiskyAnnotation<*>) {
        currentId++
        annotationList.add(annotation)
    }

    /** 删除Annotation之后 */
    private fun removeAnnotationSuccess(annotation: WiskyAnnotation<*>) {
        annotationList.remove(annotation)
    }

    override fun addPoint(optionList: List<WiskyPointAnnotationOptions>) {
        for (bean in optionList) {
            addPoint(bean)
        }
    }

    override fun updatePoint(annotation: WiskyPointAnnotation) {
        val options = annotation.options
        val existId = annotationMap[annotation] as? PointAnnotation

//        val existAnnotation = highPointManager.annotations.firstOrNull { it == existId }

        existId?.apply {
            val point = Point.fromLngLat(options.latLng.longitude, options.latLng.latitude)
            geometry = point
            options.imageId?.let {
                iconImage = it
            }
            options.iconAnchor?.let {
                iconAnchor = it
            }
            options.iconOffset?.let {
                iconOffset = it
            }
            options.iconSize?.let {
                iconSize = it
            }
            options.iconRotate?.let {
                iconRotate = it
            }
            options.symbolSortKey?.let {
                symbolSortKey = it
            }
            options.textField?.let {
                textField = it
            }
            options.textAnchor?.let {
                textAnchor = (WiskyTextAnchor.findMapboxEnum(it.value))
            }
            options.textJustify?.let {
                textJustify = (WiskyTextJustify.findMapboxEnum(it.value))
            }
            options.textLineHeight?.let {
                textLineHeight = it
            }
            options.textMaxWidth?.let {
                textMaxWidth = it
            }
            options.textOffset?.let {
                textOffset = it
            }
            options.textRadialOffset?.let {
                textRadialOffset = it
            }
            options.textRotate?.let {
                textRotate = it
            }
            options.textSize?.let {
                textSize = it
            }
            options.textColorInt?.let {
                textColorInt = it
            }
            options.textColorString?.let {
                textColorString = it
            }
            options.textHaloBlur?.let {
                textHaloBlur = it
            }
            options.textHaloColorInt?.let {
                textHaloColorInt = it
            }
            options.textHaloColorString?.let {
                textHaloColorString = it
            }
            isDraggable = options.isDraggable
            if (options.symbolSortKey != null && options.symbolSortKey!! > 1.0) {
                highPointManager.update(this)
            } else {
                lowPointManager.update(this)
            }
        }

//        mapboxMap.getStyle { style ->
//            val point = Point.fromLngLat(options.latLng.longitude, options.latLng.latitude)
//            val source = style.getSourceAs<GeoJsonSource>(options.getSourceId())
//            source?.feature(Feature.fromGeometry(point))
//            addIconToStyle(style, options)
//            val layer = style.getLayer(options.getLayerId())
//            if (layer is SymbolLayer) {
//                updateSymbolLayer(layer, options)
//            }
//        }
    }


    override fun updatePoints(annotations: List<WiskyPointAnnotation>) {
        if (annotations.isEmpty()) {
            return
        }
        var pointList = mutableListOf<PointAnnotation>()
        annotations.forEach {
            val options = it.options
            val existId = annotationMap[it] as? PointAnnotation
            existId?.apply {
                val point = Point.fromLngLat(options.latLng.longitude, options.latLng.latitude)
                geometry = point
                options.imageId?.let {
                    iconImage = it
                }
                options.iconAnchor?.let {
                    iconAnchor = it
                }
                options.iconOffset?.let {
                    iconOffset = it
                }
                options.iconSize?.let {
                    iconSize = it
                }
                options.iconRotate?.let {
                    iconRotate = it
                }
                options.symbolSortKey?.let {
                    symbolSortKey = it
                }
                options.textField?.let {
                    textField = it
                }
                options.textAnchor?.let {
                    textAnchor = (WiskyTextAnchor.findMapboxEnum(it.value))
                }
                options.textJustify?.let {
                    textJustify = (WiskyTextJustify.findMapboxEnum(it.value))
                }
                options.textLineHeight?.let {
                    textLineHeight = it
                }
                options.textMaxWidth?.let {
                    textMaxWidth = it
                }
                options.textOffset?.let {
                    textOffset = it
                }
                options.textRadialOffset?.let {
                    textRadialOffset = it
                }
                options.textRotate?.let {
                    textRotate = it
                }
                options.textSize?.let {
                    textSize = it
                }
                options.textColorInt?.let {
                    textColorInt = it
                }
                options.textColorString?.let {
                    textColorString = it
                }
                options.textHaloBlur?.let {
                    textHaloBlur = it
                }
                options.textHaloColorInt?.let {
                    textHaloColorInt = it
                }
                options.textHaloColorString?.let {
                    textHaloColorString = it
                }
                isDraggable = options.isDraggable
                pointList.add(existId)
            }
        }
        if (annotations.isEmpty()) {
            return
        }
        var options = annotations[0].options
        if (options.symbolSortKey != null && options.symbolSortKey!! > 1.0) {
            highPointManager.update(pointList)
        } else {
            lowPointManager.update(pointList)
        }
    }


    override fun deletePoint(annotation: WiskyPointAnnotation) {
        deleteAnnotation(annotation)
        removeAnnotationSuccess(annotation)
    }

    /***
     * 批量删除同层级的点元素
     */
    override fun deletePoints(annotationList: List<WiskyPointAnnotation>) {
        if (annotationList.isEmpty()) {
            return
        }

        var deletePointList = mutableListOf<PointAnnotation>()

        annotationList.forEach {
            deletePointList.add(annotationMap[it] as PointAnnotation)
            mapboxMap.getStyle { style ->
                if (style.styleLayerExists(it.options.getLayerId())) {
                    style.removeStyleLayer(it.options.getLayerId())
                }
                style.removeStyleSource(it.options.getSourceId())
            }
        }


        if (annotationList.isEmpty()) {
            return
        }
        val annotation = annotationList[0]
        val mapAnno = annotationMap[annotation]
        val point = highPointManager.annotations.firstOrNull { it == mapAnno }
        if (point != null) {
            highPointManager.delete(deletePointList)
        }
        val point1 = lowPointManager.annotations.firstOrNull { it == mapAnno }
        if (point1 != null) {
            lowPointManager.delete(deletePointList)
        }
        annotationList.forEach {
            removeAnnotationSuccess(it)
        }
    }

    override fun addPolyline(options: WiskyPolylineAnnotationOptions): WiskyPolyLineAnnotation {
        val annotation = WiskyPolyLineAnnotation(currentId, options)
        val mapOptions = PolylineAnnotationOptions()
        updateLine(options, mapOptions)
        val polyline = if (options.lineSortKey > 1) {
            polylineManager.create(mapOptions)
        } else {
            lowPolylineManager.create(mapOptions)
        }
        annotationMap[annotation] = polyline
        addAnnotationSuccess(annotation)
        return annotation
    }

    private fun updateLine(
        options: WiskyPolylineAnnotationOptions,
        mapOptions:
        PolylineAnnotationOptions,
    ) {
        var dots = options.getPoints()
        if (dots.size == 1) {
            dots.add(dots[0])
        }
        mapOptions.apply {
            val lineString = (LineString.fromLngLats(convertLatLngToPoint(dots)))
            withGeometry(lineString)
            options.lineWidth?.let {
                lineWidth = it
            }
            options.lineColor?.let {
                lineColor = it
            }
            options.lineOpacity?.let {
                lineOpacity = it
            }
            options.lineOffset?.let {
                lineOffset = it
            }
            options.lineBlur?.let {
                lineBlur = it
            }
            options.lineGapWidth?.let {
                lineGapWidth = it
            }
            options.lineDashArray?.let {

            }
            lineSortKey = options.lineSortKey

            options.linePattern?.let {
                linePattern = it
            }
            options.lineJoin?.let {
                lineJoin = it
            }
        }
    }

    /** 经纬度转Point */
    private fun convertLatLngToPoint(list: List<WiskyLatLng>): List<Point> {
        val pointList = ArrayList<Point>()
        list.map {
            val point = Point.fromLngLat(it.longitude, it.latitude)
            pointList.add(point)
        }
        return pointList
    }

    override fun updatePolyline(annotation: WiskyPolyLineAnnotation) {
        val options = annotation.options
        var dots = options.getPoints()
        if (dots.size == 1) {
            dots.add(dots[0])
        }
        val point = LineString.fromLngLats(convertLatLngToPoint(dots))
        val existId = annotationMap[annotation] as? PolylineAnnotation
//        val existsLine = polylineManager.annotations.firstOrNull { it == existId }
        existId?.apply {
            geometry = point
            options.lineWidth?.let {
                lineWidth = it
            }
            options.lineColor?.let {
                lineColorString = it
            }
            options.lineOpacity?.let {
                lineOpacity = it
            }
            options.lineOffset?.let {
                lineOffset = it
            }
            options.lineBlur?.let {
                lineBlur = it
            }
            options.lineGapWidth?.let {
                lineGapWidth = it
            }
            options.lineDashArray?.let {

            }
            lineSortKey = options.lineSortKey
            options.linePattern?.let {
                linePattern = it
            }
            options.lineJoin?.let {
                lineJoin = it
            }
            if (options.lineSortKey > 1) {
                polylineManager.update(this)
            } else {
                lowPolylineManager.update(this)
            }
        }
    }

    /**
     * 删除Annotation
     */
    private fun deleteAnnotation(annotation: WiskyAnnotation<*>) {
        val options = annotation.options
        mapboxMap.getStyle { style ->
            if (style.styleLayerExists(options.getLayerId())) {
                style.removeStyleLayer(options.getLayerId())
            }
            style.removeStyleSource(annotation.options.getSourceId())
        }

        val mapAnno = annotationMap[annotation]
        val point = highPointManager.annotations.firstOrNull { it == mapAnno }
        if (point != null) {
            highPointManager.delete(point)
        }
        val point1 = lowPointManager.annotations.firstOrNull { it == mapAnno }
        if (point1 != null) {
            lowPointManager.delete(point1)
        }

        val line = polylineManager.annotations.firstOrNull { it == mapAnno }
        if (line != null) {
            polylineManager.delete(line)
        }
        val line1 = lowPolylineManager.annotations.firstOrNull { it == mapAnno }
        if (line1 != null) {
            lowPolylineManager.delete(line1)
        }

        val polygon = polygonManager.annotations.firstOrNull { it == mapAnno }
        if (polygon != null) {
            polygonManager.delete(polygon)
        }
    }

    override fun deletePolyline(annotation: WiskyPolyLineAnnotation) {
        deleteAnnotation(annotation)
    }

    override fun addCircle(options: WiskyCircleAnnotationOptions): WiskyCircleAnnotation {
        val annotation = WiskyCircleAnnotation(currentId, options)
        val mapOptions = PolygonAnnotationOptions()
        updateCircle(options, mapOptions)
        val polygon = polygonManager.create(mapOptions)
        annotationMap[annotation] = polygon
//        mapboxMap.getStyle { style ->
//            options.circleRadius?.let { radius ->
//                val list = MapBoxUtils.generateCirclePolyline(options.latLng, radius)
//                val point = Polygon.fromLngLats(convertLatLngToPoints(mutableListOf(list)))
//                style.addSource(geoJsonSource(options.getSourceId()) {
//                    feature(Feature.fromGeometry(point))
//                })
//                val layer = fillLayer(options.getLayerId(), options.getSourceId()) {
//                    updateCircleLayer(this, options)
//                }
//                addLayerToStyle(style, layer, options)
//            }
//        }
        addAnnotationSuccess(annotation)
        return annotation
    }

    private fun updateCircle(
        options: WiskyCircleAnnotationOptions,
        mapOptions:
        PolygonAnnotationOptions,
    ) {
        mapOptions.apply {
            val list =
                MapBoxUtils.generateCirclePolyline(options.latLng, options.circleRadius ?: 1.0)
            val point = Polygon.fromLngLats(convertLatLngToPoints(mutableListOf(list)))
            withGeometry(point)
            options.circleColor?.let {
                fillColor = it
            }
            options.circleOpacity?.let {
                fillOpacity = it
            }
            options.circleSortKey?.let {
                fillSortKey = it
            }
        }
    }

    /** 设置CircleLayer的属性 */
    private fun updateCircleLayer(layer: FillLayerDsl, options: WiskyCircleAnnotationOptions) {
        layer.apply {
            options.circleColor?.let {
                fillColor(it)
            }
            options.circleOpacity?.let {
                fillOpacity(it)
            }
            options.circleSortKey?.let {
                fillSortKey(it)
            }
        }
    }

    override fun addImageIconToStyle(name: String, image: Bitmap) {
        mapboxMap.getStyle { style ->
            if (style.getStyleImage(name) == null) {
                style.addImage(name, image)
                iconsMap[name] = image
//                image.recycle()
            }
        }
    }

    override fun triggerRepaint() {
        mapboxMap.triggerRepaint()
    }

    override fun updateCircle(annotation: WiskyCircleAnnotation) {
        val options = annotation.options
        val existId = annotationMap[annotation]
        val existAn = polygonManager.annotations.firstOrNull { it == existId }
        existAn?.apply {
            val list = MapBoxUtils.generateCirclePolyline(
                options.latLng,
                options.circleRadius ?: 1.0
            )

            val points = Polygon.fromLngLats(convertLatLngToPoints(mutableListOf(list)))
            geometry = points
            options.circleColor?.let {
                fillColorString = it
            }
            options.circleOpacity?.let {
                fillOpacity = it
            }
            options.circleSortKey?.let {
                fillSortKey = it
            }
            polygonManager.update(this)
        }

//        mapboxMap.getStyle { style ->
//            options.circleRadius?.let { radius ->
//                val list = MapBoxUtils.generateCirclePolyline(options.latLng, radius)
//                val point = Polygon.fromLngLats(convertLatLngToPoints(mutableListOf(list)))
//                val source = style.getSourceAs<GeoJsonSource>(options.getSourceId())
//                source?.let {
//                    it.feature(Feature.fromGeometry(point))
//                    if (annotation.getType() == WiskyAnnotationType.PolygonAnnotation) {
//                        val layer = style.getLayer(options.getLayerId())
//                        if (layer is FillLayer) {
//                            updateCircleLayer(layer, options)
//                        }
//                    }
//                }
//            }
//        }
    }

    override fun deleteCircle(annotation: WiskyCircleAnnotation) {
        deleteAnnotation(annotation)
    }

    override fun addPolygon(options: WiskyPolygonAnnotationOptions): WiskyPolygonAnnotation {
        val annotation = WiskyPolygonAnnotation(currentId, options)
        val mapOptions = PolygonAnnotationOptions()
        updateFillOptions(options, mapOptions)
        val polygon = polygonManager.create(mapOptions)
        annotationMap[annotation] = polygon
//        mapboxMap.getStyle { style ->
//            style.addSource(geoJsonSource(options.getSourceId()) {
//                feature(
//                    Feature.fromGeometry(Polygon.fromLngLats(convertLatLngToPoints(options.getPoints())))
//                )
//            })
//            val layer = fillLayer(options.getLayerId(), options.getSourceId()) {
//                updateFillLayer(this, options)
//            }
//            addLayerToStyle(style, layer, options)
//        }
        addAnnotationSuccess(annotation)
        return annotation
    }

    private fun updateFillOptions(
        options: WiskyPolygonAnnotationOptions,
        mapOptions:
        PolygonAnnotationOptions,
    ) {
        mapOptions.apply {
            val points = Polygon.fromLngLats(convertLatLngToPoints(options.getPoints()))
            withGeometry(points)
            options.fillColor?.let {
                fillColor = it
            }
            options.fillOpacity?.let {
                fillOpacity = it
            }
            options.fillSortKey?.let {
                fillSortKey = it
            }
            options.fillPattern?.let {
                fillPattern = it
            }
            options.fillOutlineColor?.let {
                fillOutlineColor = it
            }
        }
    }

    /** 设置FillLayer的属性 */
    private fun updateFillLayer(layer: FillLayerDsl, options: WiskyPolygonAnnotationOptions) {
        layer.apply {
            options.fillColor?.let {
                fillColor(it)
            }
            options.fillOpacity?.let {
                fillOpacity(it)
            }
            options.fillSortKey?.let {
                fillSortKey(it)
            }
            options.fillPattern?.let {
                fillPattern(it)
            }
            options.fillOutlineColor?.let {
                fillOutlineColor(it)
            }
        }
    }

    private fun convertLatLngToPoints(list: List<List<WiskyLatLng>>): List<List<Point>> {
        return list.map {
            it.map { latLng ->
                Point.fromLngLat(latLng.longitude, latLng.latitude)
            }
        }
    }

    override fun updatePolygon(annotation: WiskyPolygonAnnotation) {
        val options = annotation.options
        val existId = annotationMap[annotation]
        val existAn = polygonManager.annotations.firstOrNull { it == existId }
        existAn?.apply {
            val points = Polygon.fromLngLats(convertLatLngToPoints(options.getPoints()))
            geometry = points
            options.fillColor?.let {
                fillColorString = it
            }
            options.fillOpacity?.let {
                fillOpacity = it
            }
            options.fillSortKey?.let {
                fillSortKey = it
            }
            options.fillPattern?.let {
                fillPattern = it
            }
            options.fillOutlineColor?.let {
                fillOutlineColorString = it
            }
            polygonManager.update(this)
        }
//        mapboxMap.getStyle { style ->
//            val source = style.getSourceAs<GeoJsonSource>(options.getSourceId())
//            source?.let {
//                it.feature(Feature.fromGeometry(point))
//                if (annotation.getType() == WiskyAnnotationType.PolygonAnnotation) {
//                    val layer = style.getLayer(options.getLayerId())
//                    if (layer is FillLayer) {
//                        updateFillLayer(layer, options)
//                    }
//                }
//            }
//        }
    }

    override fun deletePolygonOption(options: WiskyBaseOptions) {
        mapboxMap.getStyle { style ->
            if (style.styleLayerExists(options.getLayerId())) {
                style.removeStyleLayer(options.getLayerId())
            }
            style.removeStyleSource(options.getSourceId())
        }
    }

    override fun deletePolygon(annotation: WiskyPolygonAnnotation) {
        deleteAnnotation(annotation)
    }

    override fun addView(options: WiskyViewAnnotationOptions): WiskyViewAnnotation {
        val annotation = WiskyViewAnnotation(currentId, options)
        val view = LayoutInflater.from(context).inflate(options.getLayout(), null, false)
        val bitmap = BitmapUtils.createBitmapByView(context as Activity, view)
        val point = Point.fromLngLat(options.latLng.longitude, options.latLng.latitude)
        mapboxMap.getStyle { style ->
            options.imageId?.let { imageId ->
                style.addImage(imageId, bitmap)
                style.addSource(
                    geoJsonSource(options.getSourceId()) {
                        feature(Feature.fromGeometry(point))
                    }
                )
                style.addLayer(
                    symbolLayer(options.getLayerId(), options.getSourceId()) {
                        iconImage(imageId)
                        iconIgnorePlacement(true)
                        iconAllowOverlap(true)
                    }
                )
            }
        }
        addAnnotationSuccess(annotation)
        bitmap.recycle()
        return annotation
    }

    override fun updateView(annotation: WiskyViewAnnotation) {
        val options = annotation.options
        mapboxMap.getStyle { style ->
            val point = Point.fromLngLat(options.latLng.longitude, options.latLng.latitude)
            val source = style.getSourceAs<GeoJsonSource>(options.getSourceId())
            source?.feature(Feature.fromGeometry(point))
//            addIconToStyle(style, options)
//            val layer = style.getLayer(options.getLayerId()) as SymbolLayer
//            updateSymbolLayer(layer, options)
        }
    }

    override fun deleteView(annotation: WiskyViewAnnotation) {
        deleteAnnotation(annotation)
    }

    override fun setCamera(cameraOptions: WiskyCameraOptions) {
        setCamera(convertCameraOptions(cameraOptions))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun loadStyle(
        activity: Activity,
        mapStyle: WiskyMapStyle,
        locale: Locale?,
        listener: WiskyStyleLoaded?
    ) {
        if (MapBoxNetWorksUtils.isInternetAvailable(activity)) {
            onLineMapStyle(mapStyle, listener, locale)
        } else {
            /**
             * 没有网络的情况下，加载离线地图，如果没有离线地图，则加载mapbox自己的缓存，
             * 注意：离线地图中必须要求根目录下有0/0/0.jpg或则1/1/0.jpg(因为有些离线地图是从0开始，有些是从1开始),不然没办法识别到是否有离线地图
             */
            var path1 =
                UDiskCheckUtils.uDiskName(activity.applicationContext, 1).toString() + "/0/0/0.jpg"
            var path2 =
                UDiskCheckUtils.uDiskName(activity.applicationContext, 1).toString() + "/1/1/0.jpg"
            var path3 =
                UDiskCheckUtils.uDiskName(activity.applicationContext, 1).toString() + "/0/0/0.png"
            var path4 =
                UDiskCheckUtils.uDiskName(activity.applicationContext, 1).toString() + "/1/1/0.png"
            if (MapBoxFileUtils.isExist(path1) || MapBoxFileUtils.isExist(path2)) {
                offlineMapStyle(activity, listener, locale, ".jpg")
            } else if (MapBoxFileUtils.isExist(path3) || MapBoxFileUtils.isExist(path4)) {
                offlineMapStyle(activity, listener, locale, ".png")
            } else {
                onLineMapStyle(mapStyle, listener, locale)
            }
        }

    }

    private fun offlineMapStyle(
        activity: Activity,
        listener: WiskyStyleLoaded?,
        locale: Locale?,
        style: String
    ) {
        WiskyLog.i(
            "OfflineMapPath---》",
            "filepath===>${Build.VERSION.SDK_INT}====>jsonPath:${Build.VERSION_CODES.N}"
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val jsonPath: String =
                UDiskCheckUtils.getAppDir(activity).toString() + "/map/mapbox.json"
            val path = "file:/" + UDiskCheckUtils.uDiskName(activity.applicationContext, 1)
                .toString() + "/{z}/{x}/{y}$style"
            val offlineMapOptions = OfflineMapOptions()
            if (!offlineMapOptions.tiles.contains(path)) {
                offlineMapOptions.tiles.add(path)
                val bounds: MutableList<Int> = ArrayList()
                bounds.add(-180)
                bounds.add(-85)
                bounds.add(180)
                bounds.add(85)
                offlineMapOptions.bounds = bounds
                MapBoxFileUtils.writeFile(jsonPath, Gson().toJson(offlineMapOptions), false)
            }
            WiskyLog.i("OfflineMapPath---》", "filepath===>$path====>jsonPath:$jsonPath")

            mapboxMap.loadStyleJson(Gson().toJson(offlineMapOptions)) { style ->
                val tileSet =
                    TileSet.Builder("modelx", arrayListOf(path)).maxZoom(0).maxZoom(20).build()
                val source = RasterSource.Builder("modelx").tileSet(tileSet).tileSize(256).build()
                style.addSource(source)
                val rasterLayer = RasterLayer("raster", "modelx")
                style.addLayer(rasterLayer)
                setCamera(CameraOptions.Builder().pitch(0.0).build())
                gesturesPlugin.updateSettings {
                    pitchEnabled = true
                }
                listener?.onStyleLoaded(true, "")
                locale?.let {
                    style.localizeLabels(it)
                }
            }

        }
    }


    private fun onLineMapStyle(
        mapStyle: WiskyMapStyle,
        listener: WiskyStyleLoaded?,
        locale: Locale?,
    ) {
        mapboxMap.loadStyleUri(convertToMapStyle(mapStyle), { style ->
            mapboxMap.loadStyleJson(replaceUri(style.styleJSON))
            style.styleTransition = transitionOptions
            if (mapStyle == WiskyMapStyle.THREE_DIMENSIONAL) {
                val demSource = rasterDemSource(SOURCE) {
                    url(TERRAIN_URL_TILE_RESOURCE)
                    tileSize(514)
                }
                val ter = terrain(SOURCE)
                if (!style.styleSourceExists(SOURCE)) {
                    demSource.bindTo(style)
                    ter.bindTo(style)
                }
                gesturesPlugin.updateSettings {
                    pitchEnabled = true
                }
            } else {
                if (style.styleSourceExists(SOURCE)) {
                    style.removeStyleSource(SOURCE)
                }
//                setCamera(CameraOptions.Builder().pitch(0.0).zoom(0.0).build())
                gesturesPlugin.updateSettings {
                    pitchEnabled = false
                }
            }
            locale?.let {
                resetLanguage(style, it)
            }
            //切换之后，需要重新再次添加一次icon
            iconsMap.forEach {
                addImageIconToStyle(it.key, it.value)
            }

            listener?.onStyleLoaded(true, "")

        }, onMapLoadErrorListener = object : OnMapLoadErrorListener {
            override fun onMapLoadError(eventData: MapLoadingErrorEventData) {
                Log.e(TAG, "eventData,${eventData.message}")
                listener?.onStyleLoaded(false, eventData.message)
            }

        })
    }

    private fun replaceUri(uriPath: String): String {
        var srcUri = JSONObject(uriPath)
        //添加字体
        srcUri.put("glyphs", "asset://mapfonts/Open Sans Bold,Arial Unicode MS Bold/{range}.pbf")
        var path = srcUri.toString()
        return path
    }

    private fun resetLanguage(style: Style, value: Locale) {
        var list = arrayListOf<String>()
        list.add("country-label")
        list.add("poi-label")
        list.add("settlement-label")
        list.add("settlement-subdivision-label")
        list.add("road-label")
        list.add("airport-label")
        list.add("water-point-label")
        list.add("water-line-label")
        list.add("natural-point-label")
        list.add("waterway-label")
        list.add("natural-line-label")
        list.add("state-label")
        style.localizeLabels(value, list)
    }

    private fun convertToMapStyle(mapStyle: WiskyMapStyle): String {
        return when (mapStyle) {
            WiskyMapStyle.NORMAL -> Style.OUTDOORS
            WiskyMapStyle.MIX -> Style.SATELLITE_STREETS
            WiskyMapStyle.THREE_DIMENSIONAL -> Style.SATELLITE_STREETS
            else -> {
                Style.OUTDOORS
            }
        }
    }

    override fun getMapStyle(): WiskyMapStyle {
        var mapStyle = WiskyMapStyle.NORMAL
        mapboxMap.getStyle()?.let {
            val styleUri = it.styleURI
            if (styleUri == Style.SATELLITE_STREETS) {
                mapStyle = if (it.styleSourceExists(SOURCE)) {
                    WiskyMapStyle.THREE_DIMENSIONAL
                } else {
                    WiskyMapStyle.MIX
                }
            }
        }
        return mapStyle
    }


    override fun addOnCameraIdleListener(listener: WiskyCameraIdleListener) {
        mapboxMap.addOnMapIdleListener { listener.onCameraIdle() }
    }

    override fun setCompassEnable(enable: Boolean) {
        val compassPlugin = mapView.getPlugin<CompassPlugin>(Plugin.MAPBOX_COMPASS_PLUGIN_ID)
        compassPlugin?.enabled = enable
    }

    override fun setLogoEnable(enable: Boolean) {
        val logoPlugin = mapView.getPlugin<LogoPlugin>(Plugin.MAPBOX_LOGO_PLUGIN_ID)
        logoPlugin?.enabled = enable
        val attributionPlugin =
            mapView.getPlugin<AttributionPlugin>(Plugin.MAPBOX_ATTRIBUTION_PLUGIN_ID)
        attributionPlugin?.enabled = enable
    }

    override fun setScaleBarEnable(enable: Boolean) {
        val scaleBarPlugin = mapView.getPlugin<ScaleBarPlugin>(Plugin.MAPBOX_SCALEBAR_PLUGIN_ID)
        scaleBarPlugin?.enabled = false
    }

    override fun pixelsForCoordinates(coordinates: List<Point>): List<ScreenCoordinate> {
        return mapboxMap.pixelsForCoordinates(coordinates)
    }

    override fun addMapClickLister(listener: OnWiskyMapClickLister) {
        mapClickListeners.add(listener)
    }

    override fun removeMapClickLister(listener: OnWiskyMapClickLister) {
        mapClickListeners.remove(listener)
    }

    private fun convertCameraOptions(cameraOptions: WiskyCameraOptions): CameraOptions {
        var point: Point? = null
        cameraOptions.center?.let {
            point = Point.fromLngLat(it.longitude, it.latitude)
        }
        var edgeInsets: EdgeInsets? = null
        cameraOptions.padding?.let {
            edgeInsets = EdgeInsets(it.top, it.left, it.bottom, it.right)
        }
        var anchor: ScreenCoordinate? = null
        cameraOptions.anchor?.let {
            anchor = ScreenCoordinate(it.x, it.y)
        }
        return CameraOptions.Builder()
            .bearing(cameraOptions.bearing)
            .center(point)
            .pitch(cameraOptions.pitch)
            .zoom(cameraOptions.zoom)
            .padding(edgeInsets)
            .anchor(anchor)
            .build()

    }

    private fun getWiskyAnnotation(annotation: Annotation<*>): WiskyAnnotation<*>? {
        return annotationMap.filterValues { it == annotation }.keys.firstOrNull()
    }

    private fun replaceRotateGesture() {
        val detectorList = gesturesPlugin.getGesturesManager().detectors
        val rotateGestureIndex = detectorList.indexOfFirst { it is RotateGestureDetector }
        val newRotateDetector = WiskyRotateGestureDetector(context, gesturesPlugin.getGesturesManager())
        detectorList.removeAt(rotateGestureIndex)
        detectorList.add(rotateGestureIndex, newRotateDetector)

        val field: Field = AndroidGesturesManager::class.java.getDeclaredField("rotateGestureDetector")
        field.setAccessible(true)

        field.set(gesturesPlugin.getGesturesManager(), newRotateDetector)
    }

    private fun replaceMoveGesture() {
        val detectorList = gesturesPlugin.getGesturesManager().detectors
        val moveGestureIndex = detectorList.indexOfFirst { it is MoveGestureDetector }
        val newMoveDetector = WiskyMoveGestureDetector(context, gesturesPlugin.getGesturesManager())
        detectorList.removeAt(moveGestureIndex)
        detectorList.add(moveGestureIndex, newMoveDetector)

        val field: Field = AndroidGesturesManager::class.java.getDeclaredField("moveGestureDetector")
        field.setAccessible(true)

        field.set(gesturesPlugin.getGesturesManager(), newMoveDetector)
    }

    private fun addGesture() {
        highPointManager.addClickListener(object : OnPointAnnotationClickListener {
            override fun onAnnotationClick(annotation: PointAnnotation): Boolean {
                Log.d("mapbox", "mapbox onAnnotationClick")
                if (checkDoubleClick()) {
                    return false
                }
                getWiskyAnnotation(annotation)?.let {
                    if (clickAnnotation(it)) {
                        return true
                    }
                    selectAnnotation(it)
                }
                return false
            }
        })
        highPointManager.addDragListener(object : OnPointAnnotationDragListener {
            override fun onAnnotationDrag(annotation: Annotation<*>) {
                val point = (annotation as PointAnnotation).point
                draggingAnnotation?.let { anno ->
                    if (!anno.options.isDraggable) {
                        stopDragging()
                        return
                    }
                    if (anno is WiskyPointAnnotation) {
                        var alt = point.altitude()
                        if (alt.isNaN()) {
                            alt = 0.0
                        }
                        anno.options.withLatLng(
                            point.longitude(),
                            point.latitude(),
                            alt
                        )
                    }
                    dragListeners.forEach {
                        it.onAnnotationDrag(anno)
                    }
                }
            }

            override fun onAnnotationDragFinished(annotation: Annotation<*>) {
                stopDragging()
            }

            override fun onAnnotationDragStarted(annotation: Annotation<*>) {
                getWiskyAnnotation(annotation)?.let {
                    selectAnnotation(it)
                    startDragging(it)
                }
            }
        })

        polylineManager.addClickListener(object : OnPolylineAnnotationClickListener {
            override fun onAnnotationClick(annotation: PolylineAnnotation): Boolean {
//                if(checkDoubleClick()){
//                    return false
//                }

                getWiskyAnnotation(annotation)?.let {
                    if (clickAnnotation(it) || !it.options.isDraggable) {
                        return false
                    }
                    selectAnnotation(it)
                }
                return false
            }
        })
        polygonManager.addClickListener(object : OnPolygonAnnotationClickListener {
            override fun onAnnotationClick(annotation: PolygonAnnotation): Boolean {
//                if(checkDoubleClick()){
//                    return false
//                }
                getWiskyAnnotation(annotation)?.let {
                    if (clickAnnotation(it) || !it.options.isDraggable) {
                        return false
                    }
                    selectAnnotation(it)
                }
                return false
            }
        })
    }

    private fun checkDoubleClick(): Boolean {
        if (abs(System.currentTimeMillis() - lastClickEvent) < DOUBLE_CLICK_GAP) {
            Log.i("mapbox", "mapbox double event happen, ignore return")
            return true
        }
        lastClickEvent = System.currentTimeMillis()
        return false
    }

    override fun addAnnotationClickLister(listener: OnWiskyAnnotationClickListener<WiskyAnnotation<*>>) {
        clickListeners.add(listener)
    }

    override fun removeAnnotationClickLister(listener: OnWiskyAnnotationClickListener<WiskyAnnotation<*>>) {
        clickListeners.remove(listener)
    }

    override fun addAnnotationInteractionLister(listener: OnWiskyAnnotationInteractionListener<WiskyAnnotation<*>>) {
        interactionListener.add(listener)
    }

    override fun removeAnnotationInteractionLister(listener: OnWiskyAnnotationInteractionListener<WiskyAnnotation<*>>) {
        interactionListener.remove(listener)
    }

    override fun addAnnotationLongClickListener(listener: OnWiskyAnnotationLongClickListener<WiskyAnnotation<*>>) {
        longClickListeners.add(listener)
    }

    override fun removeAnnotationLongClickListener(listener: OnWiskyAnnotationLongClickListener<WiskyAnnotation<*>>) {
        longClickListeners.remove(listener)
    }

    override fun addAnnotationDragListener(listener: OnWiskyAnnotationDragLister<WiskyAnnotation<*>>) {
        dragListeners.add(listener)
    }

    override fun removeAnnotationDragListener(listener: OnWiskyAnnotationDragLister<WiskyAnnotation<*>>) {
        dragListeners.remove(listener)
    }

    override fun addScaleListener(listener: OnWiskyScaleListener) {
        scaleListeners.add(listener)
    }

    override fun removeScaleListener(listener: OnWiskyScaleListener) {
        scaleListeners.remove(listener)
    }

    override fun addPositionChangeListener(listener: OnWiskyPositionChangedListener) {
        locationChangeListeners.add(listener)
    }

    override fun removePositionChangeListener(listener: OnWiskyPositionChangedListener) {
        locationChangeListeners.remove(listener)
    }

    override fun addOnCameraMoveListener(listener: WiskyCameraMoveListener) {
        cameraMoveListener.add(listener)
    }

    override fun removeCameraMoveListener(listener: WiskyCameraMoveListener) {
        cameraMoveListener.remove(listener)
    }

    override fun addOnCameraChangeListener(listener: OnWiskyCameraChangeListener) {
        cameraChangeListeners.add(listener)
    }

    override fun removeCameraChangeListener(listener: OnWiskyCameraChangeListener) {
        cameraChangeListeners.remove(listener)
    }

    /**
     * Class handle the map move event
     */
    inner class MapMove : OnMoveListener {
        /**
         * Called when the move gesture is starting.
         */
        override fun onMoveBegin(detector: MoveGestureDetector) {
        }

        /**
         * Called when the move gesture is ending.
         */
        override fun onMoveEnd(detector: MoveGestureDetector) {
        }

        /**
         * Called when the move gesture is executing.
         */
        override fun onMove(detector: MoveGestureDetector): Boolean {
            cameraMoveListener.forEach {
                it.onCameraMove()
            }
            return false
        }
    }

    private fun startDragging(annotation: WiskyAnnotation<*>): Boolean {
        dragListeners.forEach { it.onAnnotationDragStarted(annotation) }
        draggingAnnotation = annotation
        return true
    }

    private fun stopDragging() {
        draggingAnnotation?.let { annotation ->
            dragListeners.forEach { it.onAnnotationDragFinished(annotation) }
            draggingAnnotation = null
        }
    }

    /**
     * 获取当前坐标对应的Annotation
     * @param screenCoordinate 坐标
     */
    private fun queryMapForFeatures(
        screenCoordinate: ScreenCoordinate,
        isDrag: Boolean,
    ): WiskyAnnotation<*>? {
        var annotation: WiskyAnnotation<*>? = null
        val latch = CountDownLatch(1)
        mapView.getMapboxMap().executeOnRenderThread {
            mapView.getMapboxMap().queryRenderedFeatures(
                RenderedQueryGeometry.valueOf(screenCoordinate),
                RenderedQueryOptions(getLayerList(), literal(true))
            ) {
                if (it.isValue && it.value!!.isNotEmpty()) {
                    for (a in it.value!!) {
                        annotation = getClickAnnotation(a.source, isDrag)
                        if (annotation != null) {
                            break
                        }
                    }
                }
                latch.countDown()
            }
        }
        println("----mapbox:queryMap:anno:${annotation?.id}")
        latch.await(QUERY_WAIT_TIME, TimeUnit.SECONDS)
        return annotation
    }

    private fun getLayerList(): List<String> {
        return ArrayList<String>()
    }

    private fun getClickAnnotation(source: String, isDrag: Boolean): WiskyAnnotation<*>? {
        var annotation: WiskyAnnotation<*>? = null
        for (bean in annotationList) {
            if (bean.options.mSourceId == source) {
                if (isDrag) {
                    if (bean.options.isDraggable) {
                        annotation = bean
                        break
                    }
                } else {
                    annotation = bean
                    break
                }
            }
        }
        return annotation
    }


    inner class CameraChange : OnCameraChangeListener {
        override fun onCameraChanged(eventData: CameraChangedEventData) {
            cameraChangeListeners.forEach {
                it.onCameraChange(eventData)
            }
        }
    }

    /**
     * Class handle the map click event
     */
    inner class MapClick : OnMapClickListener {
        /**
         * Called when the user clicks on the map view.
         * Note that calling this method is blocking main thread until querying map for features is finished.
         *
         * @param point The projected map coordinate the user clicked on.
         * @return True if this click should be consumed and not passed further to other listeners registered afterwards,
         * false otherwise.
         */
        override fun onMapClick(point: Point): Boolean {
            Log.e("mapbox", "mapbox onMapClick")
            if (checkDoubleClick()) {
                return false
            }
            mapClickListeners.forEach {
                it.onMapClick(pointToWiskyLatLng(point))
            }
            return false
        }
    }

    private fun clickAnnotation(annotation: WiskyAnnotation<*>): Boolean {
        var process = false
        clickListeners.forEach {
            process = it.onAnnotationClick(annotation)
        }
        return process
    }

    private fun selectAnnotation(annotation: WiskyAnnotation<*>) {
        interactionListener.forEach {
            it.onSelectAnnotation(annotation)
        }
    }

    /**
     * Class handle the map long click event
     */
    inner class MapLongClick : OnMapLongClickListener {
        /**
         * Called when the user long clicks on the map view.
         *
         * @param point The projected map coordinate the user clicked on.
         * @return True if this click should be consumed and not passed further to other listeners registered afterwards,
         * false otherwise.
         */
        override fun onMapLongClick(point: Point): Boolean {
            if (longClickListeners.isEmpty()) {
                return false
            }
            queryMapForFeatures(mapboxMap.pixelForCoordinate(point), false)?.let {
                longClickListeners.forEach { listener ->
                    if (listener.onAnnotationLongClick(it)) {
                        return true
                    }
                }
            }
            return false
        }
    }

    inner class MapScale : OnScaleListener {
        override fun onScale(detector: StandardScaleGestureDetector) {
            scaleListeners.forEach {
                it.onScale(WiskyScaleGesture(detector.scaleFactor))
            }
        }

        override fun onScaleBegin(detector: StandardScaleGestureDetector) {
            scaleListeners.forEach {
                it.onScaleBegin(WiskyScaleGesture(detector.scaleFactor))
            }
        }

        override fun onScaleEnd(detector: StandardScaleGestureDetector) {
            scaleListeners.forEach {
                it.onScaleEnd(WiskyScaleGesture(detector.scaleFactor))
            }
        }
    }

    inner class LocationChange : OnIndicatorPositionChangedListener {
        override fun onIndicatorPositionChanged(point: Point) {
            locationChangeListeners.forEach {
                it.onIndicatorPositionChanged(pointToWiskyLatLng(point))
            }
        }
    }
}