package com.comm.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import com.Wisky.log.WiskyLog
import com.comm.map.annotation.*
import com.comm.map.bean.*
import com.comm.map.listener.*
import com.comm.map.options.*
import com.comm.map.util.BitmapUtils
import com.comm.map.mapbox.MapBoxFileUtils
import com.comm.map.mapbox.MapBoxNetWorksUtils
import com.comm.map.util.UDiskCheckUtils
import com.comm.map.annotation.WiskyAnnotation
import com.comm.map.annotation.WiskyCircleAnnotation
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
import com.comm.map.bean.LayerPriority
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
import com.comm.map.options.WiskyPointAnnotationOptions
import com.comm.map.options.WiskyPolygonAnnotationOptions
import com.comm.map.options.WiskyPolylineAnnotationOptions
import com.comm.map.options.WiskyViewAnnotationOptions
import com.google.gson.Gson
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.android.gestures.StandardScaleGestureDetector
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.*
import com.mapbox.mapboxsdk.plugins.localization.LocalizationPlugin
import com.mapbox.mapboxsdk.plugins.localization.MapLocale
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.RasterLayer
import com.mapbox.mapboxsdk.style.sources.RasterSource
import com.mapbox.mapboxsdk.style.sources.TileSet
import com.mapbox.maps.ScreenCoordinate
import com.mapbox.maps.extension.style.expressions.dsl.generated.properties
import org.json.JSONObject
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.reflect.Field
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.log2
import kotlin.math.sqrt


/**
 * @Author create by LJ
 * @Date 2023/8/3 11
 */
class WiskyMapTilerImp(private val mapView: MapView) : WiskyMapView() {

    companion object {
        private const val DOUBLE_CLICK_GAP = 500
        private const val TAG = "WiskyMapTilerImp"
        const val mapTileKey = "ofAyyMUddJFgZnxhBKFF"

        private const val QUERY_WAIT_TIME = 1L
    }

    private var mapboxMap: MapboxMap? = null

    /**
     * Annotation的标识id
     */
    private var currentId = 0L

    /** 已绘制图形的的集合*/
    private val annotationList = ArrayList<WiskyAnnotation<*>>()


    /**
     * 地图点击事件
     */
    private val mapClickListeners = mutableListOf<OnWiskyMapClickLister>()

    /**
     * point 拖拽监听
     */
    private val pointDragListener = SymbolDragListener()

    /** 缩放监听集合 */
    private val scaleListeners = mutableListOf<OnWiskyScaleListener>()

    /** 拖动监听集合 */
    private val dragListeners = mutableListOf<OnWiskyAnnotationDragLister<WiskyAnnotation<*>>>()

    /** 当前拖动的Annotation */
    private var draggingAnnotation: WiskyAnnotation<*>? = null

    /** annotation点击监听集合 */
    private val clickListeners = mutableListOf<OnWiskyAnnotationClickListener<WiskyAnnotation<*>>>()

    /** 长按监听集合 */
    private val longClickListeners =
        mutableListOf<OnWiskyAnnotationLongClickListener<WiskyAnnotation<*>>>()

    /** 位置变化监听集合 */
    private val locationChangeListeners = mutableListOf<OnWiskyPositionChangedListener>()

    /**
     * 获取context
     */
    private val context: Context = mapView.context

    /**
     * 存储对应的Annotation
     */
    private var annotationMap =
        mutableMapOf<WiskyAnnotation<*>, com.mapbox.mapboxsdk.plugins.annotation.Annotation<*>>()

    /** 选中和反选监听集合 */
    private val interactionListener =
        mutableListOf<OnWiskyAnnotationInteractionListener<WiskyAnnotation<*>>>()


    /**
     * 默认的移动手势
     */
    private var preMoveGestureDetector: MoveGestureDetector? = null

    /**
     * 地图移动手势监听
     */
    private val onMoveGestureListener = MapMoveGestureListener()

    /**
     * 地图点击监听
     */
    private val onMapClickListener = MapClickListener()

    /**
     * 地图长按监听
     */
    private val mapLongClickListener = MapLongClickListener()

    /**
     * 地图缩放监听
     */
    private val mapScaleListener = MapScaleListener()

    /**
     * 地图点击事件间隔
     */
    private var lastClickEvent = 0L

    /**
     * 记录在style未初始化之前被添加的Annotation
     */
    private var noAddMapAnnotation = ConcurrentHashMap<Long, WiskyAnnotation<*>>()

    /**
     * 图标，切换样式的时候需要再次添加图标
     */
    private val iconsMap = hashMapOf<String, Bitmap>()

    /**
     * 镜头移动监听
     */
    private val cameraMoveListener = mutableListOf<WiskyCameraMoveListener>()

    private var lowPointManager: SymbolManager? = null
    private var midPointManager: SymbolManager? = null
    private var highPointManager: SymbolManager? = null

    private var lowPolygonManager: FillManager? = null
    private var midPolygonManager: FillManager? = null
    private var highPolygonManager: FillManager? = null

    private var lowPolylineManager: LineManager? = null
    private var midPolylineManager: LineManager? = null
    private var highPolylineManager: LineManager? = null

    private var lowCircleManager: CircleManager? = null
    private var midCircleManager: CircleManager? = null
    private var highCircleManager: CircleManager? = null

    override fun setDefaultMapSettings() {
        setCompassEnable(false)
        setLogoEnable(false)
        setScaleBarEnable(false)
        setMapMaxMinZoom(19.0, 0.0)
    }


    private fun setMapMaxMinZoom(max: Double, min: Double) {
        mapboxMap?.let {
            it.setMaxZoomPreference(max)
            it.setMinZoomPreference(min)
        }
    }

    override fun moveCameraTo(latLng: WiskyLatLng, zoom: Double?, duration: Long?) {
        val position = CameraPosition.Builder()
            .target(LatLng(latLng.latitude, latLng.longitude))
            .zoom(zoom ?: 0.0)
            .build()
        val update = CameraUpdateFactory.newCameraPosition(position)
        moveCamera(update, duration)
    }

    override fun fromCoordinate2Point(coordinate: ScreenCoordinate): Point {
        val latLng = mapboxMap?.projection?.fromScreenLocation(
            PointF(
                coordinate.x.toFloat(),
                coordinate.y.toFloat()
            )
        )
        return Point.fromLngLat(latLng?.longitude ?: 0.0, latLng?.latitude ?: 0.0)
    }

    override fun moveBearingCameraTo(
        latLng: WiskyLatLng,
        zoom: Double?,
        duration: Long,
        bearing: Double?
    ) {
        val position = CameraPosition.Builder()
            .target(LatLng(latLng.latitude, latLng.longitude))
            .zoom(zoom ?: 0.0)
            .bearing(bearing ?: 0.0)
            .build()
        val update = CameraUpdateFactory.newCameraPosition(position)
        moveCamera(update, duration)
    }

    override fun visualArea(
        list: List<WiskyLatLng>,
        duration: Long?,
        left: Double,
        top: Double,
        right: Double,
        bottom: Double
    ): Double? {
        val latLngList = arrayListOf<LatLng>()
        list.forEach {
            latLngList.add(LatLng(it.latitude, it.longitude, it.altitude))
        }
        var padding = IntArray(4)
        padding[0] = left.toInt()
        padding[1] = top.toInt()
        padding[2] = right.toInt()
        padding[3] = bottom.toInt()
        var bounds = LatLngBounds.fromLatLngs(latLngList)
        var cameraBounds = mapboxMap?.getCameraForLatLngBounds(bounds, padding)
        return cameraBounds?.zoom
    }

    override fun getMapVertexPoint(): MutableList<Point> {
        //TODO 获取相机四个角


        return mutableListOf()
    }

    override fun visualAreaPoint(
        list: List<WiskyLatLng>,
        duration: Long?,
        left: Double,
        top: Double,
        right: Double,
        bottom: Double
    ) {

        val latLngList = arrayListOf<LatLng>()
        list.forEach {
            latLngList.add(LatLng(it.latitude, it.longitude, it.altitude))
        }
        var bounds = LatLngBounds.fromLatLngs(latLngList)
        val center = bounds.center
        var padding = IntArray(4)
        padding[0] = left.toInt()
        padding[1] = top.toInt()
        padding[2] = right.toInt()
        padding[3] = bottom.toInt()
        var cameraBounds = mapboxMap?.getCameraForLatLngBounds(bounds, padding)
        val position =
            CameraPosition.Builder().target(center).zoom(cameraBounds?.zoom ?: 0.0).build()
        val update = CameraUpdateFactory.newCameraPosition(position)
        easeCamera(update, 0)
    }

    private fun getZoomLevel(bounds: LatLngBounds): Double {
        val worldWidth = 256.0
        val zoomMax = mapboxMap?.maxZoomLevel
        val zoomMin = mapboxMap?.minZoomLevel

        val ne = bounds.northEast
        val sw = bounds.northWest
        val boundsWidth = Math.abs(ne.longitude - sw.longitude)
        val boundsHeight = Math.abs(ne.latitude - sw.latitude)

        val zoomWidth = Math.floor(log2(worldWidth / boundsWidth))
        val zoomHeight = Math.floor(log2(worldWidth / boundsHeight))

        return maxOf(zoomWidth, zoomHeight, zoomMax ?: 0.0, zoomMin ?: 0.0)
    }

    override fun setZoom(zoom: Double?, duration: Long?) {
        val position = CameraPosition.Builder()
            .zoom(zoom ?: 0.0)
            .build()
        val update = CameraUpdateFactory.newCameraPosition(position)
        moveCamera(update, duration)

    }

    override fun getZoom(): Double {
        return mapboxMap?.cameraPosition?.zoom ?: 0.0
    }

    override fun getBearing(): Double {
        return mapboxMap?.cameraPosition?.bearing ?: 0.0
    }

    override fun setMaxZoom(zoom: Double) {
        mapboxMap?.maxZoomLevel
    }

    override fun setScaleMap(zoom: Double, screenCoordinate: ScreenCoordinate, duration: Long) {
        val latLng = mapboxMap?.projection?.fromScreenLocation(
            PointF(
                screenCoordinate.x.toFloat(),
                screenCoordinate.y.toFloat()
            )
        )
        val position = CameraPosition.Builder()
            .zoom(zoom)
            .target(latLng)
            .build()
        val update = CameraUpdateFactory.newCameraPosition(position)
        easeCamera(update, duration)

    }

    override fun getScalePerPixel(): Double {
        return 0.0
    }

    override fun calculatePointPixel(startPoint: WiskyLatLng, endpoint: WiskyLatLng): Double {
        mapboxMap?.let {
            val startPointF =
                it.projection.toScreenLocation(LatLng(startPoint.latitude, startPoint.longitude))
            val endPointF =
                it.projection.toScreenLocation(LatLng(endpoint.latitude, endpoint.longitude))
            val dx: Double = (endPointF.x - startPointF.x).toDouble()
            val dy: Double = (endPointF.y - startPointF.y).toDouble()
            return sqrt(dx * dx + dy * dy)
        }
        return 0.0
    }

    override fun rotate(bearing: Double, duration: Long?) {
        val position = CameraPosition.Builder()
            .bearing(bearing)
            .build()
        val update = CameraUpdateFactory.newCameraPosition(position)
        duration?.let {
            if (it > 0) {
                easeCamera(update, it)
            } else {
                easeCamera(update, 0)
            }
        } ?: kotlin.run {
            easeCamera(update, 0)
        }
    }

    override fun showUserLocation(resId: Int) {
    }

    override fun hideUserLocation() {
    }

    override fun onMapStart() {
        mapView.onStart()
    }

    override fun onMapStop() {
        mapView.onStop()
    }

    override fun onMapDestroy() {
        mapView.onDestroy()
        longClickListeners.clear()
        mapClickListeners.clear()
        scaleListeners.clear()
        dragListeners.clear()
        clickListeners.clear()
        interactionListener.clear()
        locationChangeListeners.clear()
        cameraMoveListener.clear()
        highPointManager?.apply {
            deleteAll()
            removeDragListener(pointDragListener)
        }
        midPointManager?.apply {
            deleteAll()
            removeDragListener(pointDragListener)
        }
        lowPointManager?.apply {
            deleteAll()
            removeDragListener(pointDragListener)
        }
        highCircleManager?.deleteAll()
        midCircleManager?.deleteAll()
        lowCircleManager?.deleteAll()

        highPolylineManager?.deleteAll()
        midPolylineManager?.deleteAll()
        lowPolylineManager?.deleteAll()

        highPolygonManager?.deleteAll()
        midPolygonManager?.deleteAll()
        lowPolygonManager?.deleteAll()

    }

    override fun onMapLowMemory() {
        mapView.onLowMemory()
    }

    override fun addOnMapIdleListener(listener: (start: Long, end: Long) -> Unit) {
        mapboxMap?.addOnCameraIdleListener {
            listener.invoke(0, 0)
        }
    }

    override fun addPoint(options: WiskyPointAnnotationOptions): WiskyPointAnnotation {
        val annotation = WiskyPointAnnotation(currentId, options)
        val symbolOptions = SymbolOptions()
        options.getImageBitmap()?.let { bitmap ->
            options.imageId?.let { imageId ->
                addImageIconToStyle(imageId, bitmap)
            }
        }
        updateAnnotationOptions(symbolOptions, options)
        val symbol = if (options.symbolSortKey != null && options.symbolSortKey!! > 1) {
            getPointManager(LayerPriority.MIDDLE)?.create(symbolOptions)
        } else {
            getPointManager(LayerPriority.LOW)?.create(symbolOptions)
        }
        symbol?.let {
            annotationMap[annotation] = it
        } ?: kotlin.run {
            noAddMapAnnotation[currentId] = annotation
        }
        addAnnotationSuccess(annotation)

        return annotation
    }

    override fun addPoint(optionList: List<WiskyPointAnnotationOptions>) {
        for (bean in optionList) {
            addPoint(bean)
        }
    }

    override fun addPoints(optionsList: List<WiskyPointAnnotationOptions>): List<WiskyPointAnnotation> {
        if (optionsList.isEmpty()) {
            return mutableListOf()
        }
        var pointList = mutableListOf<SymbolOptions>()
        var WiskyPointAnnotations = mutableListOf<WiskyPointAnnotation>()

        optionsList.forEach {
            //如果传入的不是图片id，传入的是bitmap，需要将bitmap先加入到style中
            it.getImageBitmap()?.let { bitmap ->
                it.imageId?.let { imageId ->
                    addImageIconToStyle(imageId, bitmap)
                }
            }
            val annotation = WiskyPointAnnotation(currentId, it)
            val pointOptions = SymbolOptions()
            updateAnnotationOptions(pointOptions, it)
            pointList.add(pointOptions)
            addAnnotationSuccess(annotation)
            WiskyPointAnnotations.add(annotation)
        }

        if (optionsList.isEmpty()) {
            return mutableListOf()
        }

        var options = optionsList[0]
        val symbols = if (options.symbolSortKey != null && options.symbolSortKey!! > 1) {
            getPointManager(LayerPriority.MIDDLE)?.create(pointList)
        } else {
            getPointManager(LayerPriority.LOW)?.create(pointList)
        }

        WiskyPointAnnotations.mapIndexed { index, item ->
            symbols?.get(index)?.let {
                annotationMap[item] = it
            } ?: kotlin.run {
                noAddMapAnnotation[item.id] = item
            }
        }
        return WiskyPointAnnotations
    }

    override fun updatePoint(annotation: WiskyPointAnnotation) {
        val options = annotation.options
        val existId = annotationMap[annotation] as? Symbol
        existId?.apply {
            val point = Point.fromLngLat(options.latLng.longitude, options.latLng.latitude)
            geometry = point
            options.imageId?.let {
                iconImage = it
            }
            options.iconAnchor?.let {
                iconAnchor = it.value
            }
            options.iconOffset?.let {
                var pointf = PointF(it[0].toFloat(), it[1].toFloat())
                iconOffset = pointf
            }
            options.iconSize?.let {
                iconSize = it.toFloat()
            }
            options.iconRotate?.let {
                iconRotate = it.toFloat()
            }
            options.symbolSortKey?.let {
                symbolSortKey = it.toFloat()
            }
            options.textField?.let {
                textField = it
            }
            options.textAnchor?.let {
                textAnchor = it.value
            }
            options.textJustify?.let {
                textJustify = it.value
            }
            options.textLineHeight?.let {
                //TODO
                // textLineHeight = it
            }
            options.textMaxWidth?.let {
                textMaxWidth = it.toFloat()
            }
            options.textOffset?.let {
                textOffset = PointF(it[0].toFloat(), it[1].toFloat())
            }
            options.textRadialOffset?.let {
                textRadialOffset = it.toFloat()
            }
            options.textRotate?.let {
                textRotate = it.toFloat()
            }
            options.textSize?.let {
                textSize = it.toFloat()
            }
            options.textColorInt?.let {
                setTextColor(it)
            }
            options.textColorString?.let {
                textColor = it
            }
            options.textHaloBlur?.let {
                textHaloBlur = it.toFloat()
            }
            options.textHaloColorInt?.let {
                setIconHaloColor(it)
            }
            options.textHaloColorString?.let {
                textHaloColor = it
            }
//            isDraggable = options.isDraggable
            if (options.symbolSortKey != null && options.symbolSortKey!! > 1.0) {
                getPointManager(LayerPriority.MIDDLE)?.update(this)
            } else {
                getPointManager(LayerPriority.LOW)?.update(this)
            }
        }

    }

    override fun updatePoints(annotations: List<WiskyPointAnnotation>) {
        if (annotations.isEmpty()) {
            return
        }
        var pointList = mutableListOf<Symbol>()
        annotations.forEach {
            val options = it.options
            val existId = annotationMap[it] as? Symbol
            existId?.apply {
                val point = Point.fromLngLat(options.latLng.longitude, options.latLng.latitude)
                geometry = point
                options.imageId?.let {
                    iconImage = it
                }
                options.iconAnchor?.let {
                    iconAnchor = it.value
                }
                options.iconOffset?.let {
                    iconOffset = PointF(it[0].toFloat(), it[1].toFloat())
                }
                options.iconSize?.let {
                    iconSize = it.toFloat()
                }
                options.iconRotate?.let {
                    iconRotate = it.toFloat()
                }
                options.symbolSortKey?.let {
                    symbolSortKey = it.toFloat()
                }
                options.textField?.let {
                    textField = it
                }
                options.textAnchor?.let {
                    textAnchor = it.value
                }
                options.textJustify?.let {
                    textJustify = it.value
                }
                options.textLineHeight?.let {
//                    textLineHeight = it
                }
                options.textMaxWidth?.let {
                    textMaxWidth = it.toFloat()
                }
                options.textOffset?.let {
                    textOffset = PointF(it[0].toFloat(), it[1].toFloat())
                }
                options.textRadialOffset?.let {
                    textRadialOffset = it.toFloat()
                }
                options.textRotate?.let {
                    textRotate = it.toFloat()
                }
                options.textSize?.let {
                    textSize = it.toFloat()
                }
                options.textColorInt?.let {
                    setTextColor(it)
                }
                options.textColorString?.let {
                    textColor = it
                }
                options.textHaloBlur?.let {
                    textHaloBlur = it.toFloat()
                }
                options.textHaloColorInt?.let {
                    setTextHaloColor(it)
                }
                options.textHaloColorString?.let {
                    textHaloColor = it
                }
//                isDraggable = options.isDraggable
                pointList.add(existId)
            }
        }
        if (annotations.isEmpty()) {
            return
        }
        var options = annotations[0].options
        if (options.symbolSortKey != null && options.symbolSortKey!! > 1.0) {
            getPointManager(LayerPriority.MIDDLE)?.update(pointList)
        } else {
            getPointManager(LayerPriority.LOW)?.update(pointList)
        }
    }

    override fun deletePoint(annotation: WiskyPointAnnotation) {
        deleteAnnotation(annotation)
        removeAnnotationSuccess(annotation)
    }

    override fun deletePoints(annotationList: List<WiskyPointAnnotation>) {
        if (annotationList.isEmpty()) {
            return
        }

        var deletePointList = mutableListOf<Symbol>()

        annotationList.forEach {
            if (annotationMap[it] is Symbol) {
                deletePointList.add(annotationMap[it] as Symbol)
                mapboxMap?.getStyle { style ->
                    style.getLayer(it.options.getLayerId())?.let {
                        style.removeLayer(it)
                    }
                    style.removeSource(it.options.getSourceId())
                }
            }
        }
        if (annotationList.isEmpty()) {
            return
        }
        val annotation = annotationList[0]
        val mapAnno = annotationMap[annotation]
        val pointManager =
            if (annotation.options.symbolSortKey != null && annotation.options.symbolSortKey!! > 1.0) {
                getPointManager(LayerPriority.MIDDLE)
            } else {
                getPointManager(LayerPriority.LOW)
            }
        annotationList.forEach {
            removeAnnotationSuccess(it)
        }
        pointManager?.delete(deletePointList)

    }

    override fun addPolyline(options: WiskyPolylineAnnotationOptions): WiskyPolyLineAnnotation {
        val annotation = WiskyPolyLineAnnotation(currentId, options)
        val mapOptions = LineOptions()
        updateLine(options, mapOptions)
        val polyline = if (options.lineSortKey < 1) {
            getPolylineManager(LayerPriority.LOW)?.create(mapOptions)
        } else if (options.lineSortKey > 1 && options.lineSortKey <= 5) {
            getPolylineManager(LayerPriority.MIDDLE)?.create(mapOptions)
        } else {
            getPolylineManager(LayerPriority.HIGH)?.create(mapOptions)
        }
        polyline?.let {
            annotationMap[annotation] = it
        } ?: kotlin.run {
            noAddMapAnnotation[currentId] = annotation
        }
        addAnnotationSuccess(annotation)
        return annotation
    }

    override fun updatePolyline(annotation: WiskyPolyLineAnnotation) {
        val options = annotation.options
        var dots = options.getPoints()
        if (dots.size == 1) {
            dots.add(dots[0])
        }
        val point = LineString.fromLngLats(convertLatLngToPoint(dots))
        val existId = annotationMap[annotation] as? Line
        existId?.apply {
            geometry = point
            options.lineWidth?.let {
                lineWidth = it.toFloat()
            }
            options.lineColor?.let {
                lineColor = it
            }
            options.lineOpacity?.let {
                lineOpacity = it.toFloat()
            }
            options.lineOffset?.let {
                lineOffset = it.toFloat()
            }
            options.lineBlur?.let {
                lineBlur = it.toFloat()
            }
            options.lineGapWidth?.let {
                lineGapWidth = it.toFloat()
            }
            options.lineDashArray?.let {

            }

//            TODO
//            lineSortKey = options.lineSortKey
            options.linePattern?.let {
                linePattern = it
            }
            options.lineJoin?.let {
                lineJoin = it.value
            }
            if (options.lineSortKey < 1) {
                getPolylineManager(LayerPriority.LOW)?.update(this)
            } else if (options.lineSortKey > 1 && options.lineSortKey <= 5) {
                getPolylineManager(LayerPriority.MIDDLE)?.update(this)
            } else {
                getPolylineManager(LayerPriority.HIGH)?.update(this)
            }
        }
    }

    override fun deletePolyline(annotation: WiskyPolyLineAnnotation) {
        annotation.options.layerPriority
        deleteAnnotation(annotation)
    }

    override fun addCircle(options: WiskyCircleAnnotationOptions): WiskyCircleAnnotation {
        val annotation = WiskyCircleAnnotation(currentId, options)
        val mapOptions = CircleOptions()
        updateCircle(options, mapOptions)
        val polygon = getCircleManager(options.layerPriority)?.create(mapOptions)
        polygon?.let {
            annotationMap[annotation]
        } ?: kotlin.run {
            noAddMapAnnotation[currentId] = annotation
        }
        addAnnotationSuccess(annotation)
        return annotation
    }

    override fun updateCircle(annotation: WiskyCircleAnnotation) {
        val options = annotation.options
        val existId = annotationMap[annotation]
        var existAn: Circle? = null
        val circleManager = getCircleManager(LayerPriority.MIDDLE)
        val size = circleManager?.annotations?.size() ?: 0
        for (i in 0..size) {
            if (circleManager?.annotations?.valueAt(i) == existId) {
                existAn = existId as Circle
            }
        }
        existAn?.let {
            it.latLng = LatLng(options.latLng.latitude, options.latLng.longitude)
            it.circleColor = options.circleColor
            it.circleRadius = options.circleRadius?.toFloat() ?: 0f
            it.isDraggable = options.isDraggable
            it.circleOpacity = options.circleOpacity?.toFloat() ?: 0f
            circleManager?.update(it)
        }
    }

    override fun deleteCircle(annotation: WiskyCircleAnnotation) {
        deleteAnnotation(annotation)
    }

    override fun addPolygon(options: WiskyPolygonAnnotationOptions): WiskyPolygonAnnotation {
        val annotation = WiskyPolygonAnnotation(currentId, options)
        val mapOptions = FillOptions()
        updateFillOptions(options, mapOptions)
        val polygon = getPolygonManager(options.layerPriority)?.create(mapOptions)
        polygon?.let {
            annotationMap[annotation] = it
        } ?: kotlin.run {
            noAddMapAnnotation[currentId] = annotation
        }
        addAnnotationSuccess(annotation)
        return annotation
    }

    override fun updatePolygon(annotation: WiskyPolygonAnnotation) {
        val options = annotation.options
        val existId = annotationMap[annotation]
        var existAn: Fill? = null
        val polygonManager = getPolygonManager(annotation.options.layerPriority)
        val size = polygonManager?.annotations?.size() ?: 0
        for (i in 0..size) {
            if (existId != null && polygonManager?.annotations?.valueAt(i) == existId) {
                existAn = existId as Fill
                break
            }
        }
        existAn?.apply {
            val points = Polygon.fromLngLats(convertLatLngToPoints(options.getPoints()))
            geometry = points
            options.fillColor?.let {
                fillColor = it
            }
            options.fillOpacity?.let {
                fillOpacity = it.toFloat()
            }
            options.fillSortKey?.let {
//                fillSortKey = it

            }
            options.fillPattern?.let {
                fillPattern = it
            }
            options.fillOutlineColor?.let {
                fillOutlineColor = it
            }
            polygonManager?.update(this)
        }
    }

    override fun deletePolygonOption(annotation: WiskyBaseOptions) {
        mapboxMap?.getStyle { style ->
            style.getLayer(annotation.getLayerId())?.let {
                style.removeLayer(it)
            }
            style.removeSource(annotation.getSourceId())
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
        mapboxMap?.getStyle { style ->
            options.imageId?.let { imageId ->
                style.addImage(imageId, bitmap)

                /*           TODO 这里添加有有问题   style.addSource()

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
                )*/
            }
        }
        addAnnotationSuccess(annotation)
        bitmap.recycle()
        return annotation
    }

    override fun updateView(annotation: WiskyViewAnnotation) {
        val options = annotation.options
        mapboxMap?.getStyle { style ->
            val point = Point.fromLngLat(options.latLng.longitude, options.latLng.latitude)
            val source = style.getSource(options.getSourceId())
//
            //            TODO 更新View的时候需要根据修改
            //            source?.feature(Feature.fromGeometry(point))

        }
    }

    override fun deleteView(annotation: WiskyViewAnnotation) {
        deleteAnnotation(annotation)
    }

    override fun setCamera(cameraOptions: WiskyCameraOptions) {
        val update = CameraUpdateFactory.newCameraPosition(convertCameraOptions(cameraOptions))
        moveCamera(update, 0)
    }

    override fun loadStyle(
        activity: Activity,
        mapStyle: WiskyMapStyle,
        locale: Locale?,
        listener: WiskyStyleLoaded?
    ) {
        if (MapBoxNetWorksUtils.isInternetAvailable(activity)) {
            onlineMap(locale, listener)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                offlineMapStyle(activity, locale, listener)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun offlineMapStyle(
        activity: Activity, locale: Locale?,
        listener: WiskyStyleLoaded?
    ) {
        var path1 =
            UDiskCheckUtils.uDiskName(activity.applicationContext, 1).toString() + "/0/0/0.jpg"
        var path2 =
            UDiskCheckUtils.uDiskName(activity.applicationContext, 1).toString() + "/1/1/0.jpg"
        var path3 =
            UDiskCheckUtils.uDiskName(activity.applicationContext, 1).toString() + "/0/0/0.png"
        var path4 =
            UDiskCheckUtils.uDiskName(activity.applicationContext, 1).toString() + "/1/1/0.png"
        if (!offlineMap(activity, locale, listener)) {  //TODO 加载瓦片图的后续再做
            if (MapBoxFileUtils.isExist(path1) || MapBoxFileUtils.isExist(path2)) {
                offlineMapTiltles(activity, listener, locale, ".jpg")
            } else if (MapBoxFileUtils.isExist(path3) || MapBoxFileUtils.isExist(path4)) {
                offlineMapTiltles(activity, listener, locale, ".png")
            } else {
                onlineMap(locale, listener)
            }
        }


    }

    private fun offlineMapTiltles(
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
            mapView.getMapAsync { map ->
                mapboxMap = map
                mapboxMap?.setStyle(Gson().toJson(offlineMapOptions)) { style ->
                    val tileSet = TileSet("tilexyz", path)
                    val source = RasterSource("sourceXyz", tileSet, 256)
                    style.addSource(source)
                    val rasterLayer = RasterLayer("offline_tiles", source.uri)
                    style.addLayer(rasterLayer)
                    initStyleOver(locale, map, style, listener)
                }
            }
        }
    }

    /**
     * 加载离线地图
     */
    @RequiresApi(Build.VERSION_CODES.N)
    private fun offlineMap(
        activity: Activity, locale: Locale?,
        listener: WiskyStyleLoaded?
    ): Boolean {

        val offlinePath = "${
            UDiskCheckUtils.uDiskName(
                activity.applicationContext,
                1
            )
        }${File.separator}offline.mbtiles"
        WiskyLog.i(TAG, "离线地图路径-->$offlinePath")
        val offlineFile = File(offlinePath)
        //离线地图不存在，加载在线，会自动走缓存
        if (!offlineFile.exists()) {
            onlineMap(locale, listener)
            return true
        }
        val styleJsonInputStream = activity.resources.assets.open("offlinemap/raster_style.json")
        val dir = File(activity.filesDir.absolutePath)
        val styleFile = File(dir, "raster_style.json")
        copyStreamToFile(styleJsonInputStream, styleFile)
        val newFileStr = styleFile.inputStream().readToString()
            .replace("___FILE_URI___", "mbtiles:///${offlineFile.absolutePath}")

        val gpxWriter = FileWriter(styleFile)
        val out = BufferedWriter(gpxWriter)
        out.write(newFileStr)
        out.close()

        mapView.getMapAsync { map ->
            mapboxMap = map
            map.setStyle(Style.Builder().fromUri(Uri.fromFile(styleFile).toString())) { style ->
                initStyleOver(locale, map, style, listener)
            }
        }
        return true
    }


    /**
     * 加载在线地图
     */

    private fun onlineMap(
        locale: Locale?,
        listener: WiskyStyleLoaded?
    ) {
        //TODO 地图来源需要可修改
        val styleUrl = "https://api.maptiler.com/maps/streets-v2/style.json?key=$mapTileKey"
        mapView.getMapAsync { map ->
            mapboxMap = map
            map.setStyle(Style.getPredefinedStyle("Streets")) {
                initStyleOver(locale, map, it, listener)
            }
        }
    }

    private fun initStyleOver(
        locale: Locale?,
        map: MapboxMap,
        it: Style,
        listener: WiskyStyleLoaded?
    ) {
        setLanguage(locale)
        map.gesturesManager?.setMutuallyExclusiveGestures()
        addGestureControl(
            WiskyGesturesSettings(
                doubleTapToZoomInEnabled = false,
                doubleTouchToZoomOutEnabled = false,
                rotateEnabled = false,
                pitchEnabled = false
            )
        )
        iconsMap.forEach {
            addImageIconToStyle(it.key, it.value)
        }
        setDefaultMapSettings()
        initListener()
        initManager(it)
        listener?.onStyleLoaded(true, "")
        map.cameraPosition = CameraPosition.Builder().target(LatLng(0.0, 0.0)).zoom(1.0).build()
        WiskyLog.i(TAG, "初始化Style完成")
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        mapboxMap?.apply {
            addOnMapClickListener(onMapClickListener)
            addOnMapLongClickListener(mapLongClickListener)
            addOnScaleListener(mapScaleListener)
            gesturesManager.setMoveGestureListener(onMoveGestureListener)
        }

    }

    private fun initManager(style: Style) {
        mapboxMap?.let { map ->
            map.isDebugActive = false

            highPointManager = SymbolManager(mapView, map, style)
            midPointManager = SymbolManager(mapView, map, style, highPointManager?.layerId)
            lowPointManager = SymbolManager(mapView, map, style, midPointManager?.layerId)

            highPolylineManager = LineManager(mapView, map, style, lowPointManager?.layerId)
            midPolylineManager = LineManager(mapView, map, style, highPolylineManager?.layerId)
            lowPolylineManager = LineManager(mapView, map, style, midPolylineManager?.layerId)

            lowCircleManager = CircleManager(mapView, map, style, lowPolylineManager?.layerId)
            midCircleManager = CircleManager(mapView, map, style, lowCircleManager?.layerId)
            highCircleManager = CircleManager(mapView, map, style, midCircleManager?.layerId)

            lowPolygonManager = FillManager(mapView, map, style, lowPolylineManager?.layerId)
            midPolygonManager = FillManager(mapView, map, style, lowPolygonManager?.layerId)
            lowPolygonManager = FillManager(mapView, map, style, midPolygonManager?.layerId)

            lowPolygonManager?.apply {
                fillAntialias = true
            }
            midPolygonManager?.apply {
                fillAntialias = true
            }
            lowPolygonManager?.apply {
                fillAntialias = true
            }
            highPointManager?.apply {
                iconAllowOverlap = true
                textAllowOverlap = true
                iconIgnorePlacement = true
                textIgnorePlacement = true
            }
            midPointManager?.apply {
                iconAllowOverlap = true
                textAllowOverlap = true
                iconIgnorePlacement = true
                textIgnorePlacement = true
            }
            lowPointManager?.apply {
                iconAllowOverlap = true
                textAllowOverlap = true
                iconIgnorePlacement = true
                textIgnorePlacement = true
            }
            highPolylineManager?.apply {
                lineCap = Property.LINE_JOIN_ROUND
            }
            midPolylineManager?.apply {
                lineCap = Property.LINE_JOIN_ROUND
            }
            lowPolylineManager?.apply {
                lineCap = Property.LINE_JOIN_ROUND
            }

//            highPointManager?.addDragListener(pointDragListener)
//            midPointManager?.addDragListener(pointDragListener)
//            lowPointManager?.addDragListener(pointDragListener)
            /**
             * style初始化完之后，在未初始化之前添加的Annotation都将重新添加一次
             */
            noAddMapAnnotation.forEach {
                val value = it.value
                if (value is WiskyPointAnnotation) {
                    initAddPoint(value)
                }
                if (value is WiskyPolyLineAnnotation) {
                    initAddPolyLine(value)
                }
                if (value is WiskyCircleAnnotation) {
                    initAddCircle(value)
                }
                if (value is WiskyPolygonAnnotation) {
                    initAddPolygon(value)
                }
            }
        }

    }

    private fun initAddPolyLine(annotation: WiskyPolyLineAnnotation) {
        val options = annotation.options
        val mapOptions = LineOptions()
        updateLine(options, mapOptions)
        val polyline = if (options.lineSortKey < 1) {
            getPolylineManager(LayerPriority.LOW)?.create(mapOptions)
        } else if (options.lineSortKey > 1 && options.lineSortKey <= 5) {
            getPolylineManager(LayerPriority.MIDDLE)?.create(mapOptions)
        } else {
            getPolylineManager(LayerPriority.HIGH)?.create(mapOptions)
        }
        polyline?.let {
            annotationMap[annotation] = it
        }
    }

    private fun initAddCircle(annotation: WiskyCircleAnnotation) {
        val mapOptions = CircleOptions()
        updateCircle(annotation.options, mapOptions)
        val polygon = getCircleManager(annotation.options.layerPriority)?.create(mapOptions)
        polygon?.let {
            annotationMap[annotation]
        }
    }

    private fun initAddPolygon(annotation: WiskyPolygonAnnotation) {
        val mapOptions = FillOptions()
        updateFillOptions(annotation.options, mapOptions)
        val polygon = getPolygonManager(annotation.options.layerPriority)?.create(mapOptions)
        polygon?.let {
            annotationMap[annotation] = it
        }
    }

    private fun initAddPoint(WiskyPointAnnotation: WiskyPointAnnotation) {
        val options = WiskyPointAnnotation.options
        val symbolOptions = SymbolOptions()
        options.getImageBitmap()?.let { bitmap ->
            options.imageId?.let { imageId ->
                addImageIconToStyle(imageId, bitmap)
            }
        }
        updateAnnotationOptions(symbolOptions, options)
        val symbol = if (options.symbolSortKey != null && options.symbolSortKey!! > 1) {
            getPointManager(LayerPriority.MIDDLE)?.create(symbolOptions)
        } else {
            getPointManager(LayerPriority.LOW)?.create(symbolOptions)
        }
        symbol?.let {
            annotationMap[WiskyPointAnnotation] = it
        }
    }

    override fun addImageRasterSource(path: String) {

    }

    override fun getMapStyle(): WiskyMapStyle {
        return WiskyMapStyle.NORMAL
    }


    private fun replaceUri(uriPath: String): String {
        var srcUri = JSONObject(uriPath)
        //添加字体
        srcUri.put("glyphs", "asset://mapfonts/Open Sans Bold,Arial Unicode MS Bold/{range}.pbf")
        var path = srcUri.toString()
        return path
    }

    /**
     * 暂时使用mapbox的资源
     */
    private fun convertToMapStyle(mapStyle: WiskyMapStyle): String {
        return WiskyMapStyle.getStyleUri(mapStyle)
    }

    override fun onDestroy() {
        mapView.onDestroy()
    }

    override fun addGestureControl(settings: WiskyGesturesSettings) {
        mapboxMap?.uiSettings?.let {
            it.isDoubleTapGesturesEnabled = settings.doubleTapToZoomInEnabled
            it.isRotateGesturesEnabled = settings.rotateEnabled
            it.isQuickZoomGesturesEnabled = settings.quickZoomEnabled
            it.isScrollGesturesEnabled = settings.scrollEnabled
            it.isZoomGesturesEnabled = settings.pinchToZoomEnabled
            it.isTiltGesturesEnabled = settings.pitchEnabled
            it.isAttributionEnabled = false
        }
    }


    override fun pixelForCoordinate(latLng: WiskyLatLng): WiskyScreenCoordinate {
        val pointF =
            mapboxMap?.projection?.toScreenLocation(LatLng(latLng.latitude, latLng.longitude))
        return WiskyScreenCoordinate(pointF?.x?.toDouble() ?: 0.0, pointF?.y?.toDouble() ?: 0.0)
    }

    override fun coordinateForPixel(pixel: WiskyScreenCoordinate): WiskyLatLng {
        val lntLng =
            mapboxMap?.projection?.fromScreenLocation(PointF(pixel.x.toFloat(), pixel.y.toFloat()))
        return WiskyLatLng(
            lntLng?.latitude ?: 0.0,
            lntLng?.longitude ?: 0.0,
            lntLng?.altitude ?: 0.0
        )
    }

    override fun cameraCenterCoordinate(): WiskyLatLng {
        val latLng = mapboxMap?.cameraPosition?.target
        return WiskyLatLng(
            latLng?.latitude ?: 0.0,
            latLng?.longitude ?: 0.0,
            latLng?.altitude ?: 0.0
        )
    }

    override fun cameraState(): WiskyCameraOptions {
        var options = WiskyCameraOptions.Builder().build()
        mapboxMap?.cameraPosition?.let {
            val options = WiskyCameraOptions.Builder()
                .bearing(it.bearing)
                .zoom(it.zoom)
                .center(
                    WiskyLatLng(
                        it.target?.latitude ?: 0.0,
                        it.target?.longitude ?: 0.0,
                        it.target?.altitude ?: 0.0
                    )
                )
                .padding(
                    WiskyEdgeInsets(
                        it.padding?.get(0) ?: 0.0,
                        it.padding?.get(1) ?: 0.0,
                        it.padding?.get(2) ?: 0.0,
                        it.padding?.get(3) ?: 0.0,
                    )
                ).build()
        }
        return options
    }


    override fun setLanguage(locale: Locale?) {
        mapboxMap?.let { mapbox ->
            mapbox.style?.let { style ->
                val localizationPlugin = LocalizationPlugin(mapView, mapbox, style)
                locale?.let {
                    localizationPlugin.setMapLanguage(MapLocale(MapLocale.CHINESE))
                } ?: kotlin.run {
                    localizationPlugin.matchMapLanguageWithDeviceDefault()
                }
            }
        }
    }

    override fun addOnCameraIdleListener(listener: WiskyCameraIdleListener) {

    }

    override fun setCompassEnable(enable: Boolean) {
        mapboxMap?.uiSettings?.isCompassEnabled = enable
    }

    override fun setLogoEnable(enable: Boolean) {
        mapboxMap?.uiSettings?.isLogoEnabled = enable
    }

    override fun setScaleBarEnable(enable: Boolean) {
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun pixelsForCoordinates(coordinates: List<Point>): List<ScreenCoordinate> {
        val list = mutableListOf<ScreenCoordinate>()
        coordinates.forEach {
            val latlng =
                pixelForCoordinate(WiskyLatLng(it.latitude(), it.longitude(), it.altitude()))
            list.add(ScreenCoordinate(latlng.x, latlng.y))
        }
        return list.toList()
    }

    override fun addImageIconToStyle(name: String, image: Bitmap) {
        mapboxMap?.getStyle { style ->
            if (style.getImage(name) == null) {
                WiskyLog.i(TAG, "添加的图片ID $name")
                style.addImage(name, image)
                iconsMap[name] = image
            }
        }

    }

    override fun triggerRepaint() {
//
        //        TODO 这里会偶发崩溃
        //        mapboxMap?.triggerRepaint()
    }

    override fun addMapClickLister(listener: OnWiskyMapClickLister) {
        mapClickListeners.add(listener)
    }

    override fun removeMapClickLister(listener: OnWiskyMapClickLister) {
        mapClickListeners.remove(listener)
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

    }

    override fun removeCameraChangeListener(listener: OnWiskyCameraChangeListener) {

    }

    private fun updateFillOptions(
        options: WiskyPolygonAnnotationOptions,
        mapOptions:
        FillOptions,
    ) {
        mapOptions.apply {
            val points = Polygon.fromLngLats(convertLatLngToPoints(options.getPoints()))
            withGeometry(points)
            options.fillColor?.let {
                withFillColor(it)
            }
            options.fillOpacity?.let {
                withFillOpacity(it.toFloat())
            }
            options.fillSortKey?.let {
//                fillSortKey = it

            }
            options.fillPattern?.let {
                withFillPattern(it)
            }
            options.fillOutlineColor?.let {
                withFillOutlineColor(it)
            }
        }
    }

    private fun updateCircle(
        options: WiskyCircleAnnotationOptions,
        mapOptions:
        CircleOptions,
    ) {
        mapOptions.apply {
            withLatLng(LatLng(options.latLng.latitude, options.latLng.longitude))
            withCircleRadius(options.circleRadius?.toFloat() ?: 1.0f)
            withGeometry(this.geometry)
            options.circleColor?.let {
                withCircleColor(it)
            }
            options.circleOpacity?.let {
                withCircleOpacity(it.toFloat())
            }
            options.circleSortKey?.let {
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

    @SuppressLint("Range")
    private fun convertCameraOptions(cameraOptions: WiskyCameraOptions): CameraPosition {
        var point: Point? = null
        cameraOptions.center?.let {
            point = Point.fromLngLat(it.longitude, it.latitude)
        }
        var edgeInsets: DoubleArray = doubleArrayOf()
        cameraOptions.padding?.let {
            edgeInsets[0] = it.top
            edgeInsets[1] = it.left
            edgeInsets[2] = it.bottom
            edgeInsets[3] = it.right
        }
        return CameraPosition.Builder()
            .bearing(cameraOptions.bearing ?: 0.0)
            .target(
                LatLng(
                    point?.latitude() ?: 0.0,
                    point?.longitude() ?: 0.0,
                    point?.altitude() ?: 0.0
                )
            )
            .zoom(cameraOptions.zoom ?: 0.0)
            .padding(edgeInsets)
            .build()


    }

    /** 删除Annotation之后 */
    private fun removeAnnotationSuccess(annotation: WiskyAnnotation<*>) {
        annotationList.remove(annotation)
    }

    /**
     * 更新线段
     */
    private fun updateLine(
        options: WiskyPolylineAnnotationOptions,
        mapOptions:
        LineOptions,
    ) {
        var dots = options.getPoints()
        if (dots.size == 1) {
            dots.add(dots[0])
        }
        mapOptions.apply {
            val lineString = (LineString.fromLngLats(convertLatLngToPoint(dots)))
            withGeometry(lineString)
            options.lineWidth?.let {
                withLineWidth(it.toFloat())
            }
            options.lineColor?.let {
                withLineColor(it)
            }
            options.lineOpacity?.let {
                withLineOpacity(it.toFloat())
            }
            options.lineOffset?.let {
                withLineOffset(it.toFloat())
            }
            options.lineBlur?.let {
                withLineBlur(it.toFloat())
            }
            options.lineGapWidth?.let {
                withLineGapWidth(it.toFloat())
            }
            options.lineDashArray?.let {

            }
//            lineSortKey = options.lineSortKey

            options.linePattern?.let {
                withLinePattern(it)
            }
            options.lineJoin?.let {
                withLineJoin(it.value)
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

    /**
     * 删除Annotation
     */
    private fun deleteAnnotation(annotation: WiskyAnnotation<*>) {
        val options = annotation.options
        mapboxMap?.getStyle { style ->
            style.getLayer(options.getLayerId())?.let {
                style.removeLayer(it)
            }
            style.removeSource(annotation.options.getSourceId())
        }

        val mapAnno = annotationMap[annotation]
        if (mapAnno is Symbol) {
            if (mapAnno.symbolSortKey != null && mapAnno.symbolSortKey!! > 1) {
                getPointManager(LayerPriority.MIDDLE)?.delete(mapAnno)
            } else {
                getPointManager(LayerPriority.LOW)?.delete(mapAnno)
            }
        }

        if (mapAnno is Circle) {
            getCircleManager(LayerPriority.MIDDLE)?.delete(mapAnno)
            getCircleManager(LayerPriority.LOW)?.delete(mapAnno)
            getCircleManager(LayerPriority.HIGH)?.delete(mapAnno)
        }

        if (mapAnno is Line) {
            getPolylineManager(LayerPriority.MIDDLE)?.delete(mapAnno)
            getPolylineManager(LayerPriority.LOW)?.delete(mapAnno)
            getPolylineManager(LayerPriority.HIGH)?.delete(mapAnno)
        }
        if (mapAnno is Fill) {
            getPolygonManager(LayerPriority.LOW)?.delete(mapAnno)
            getPolygonManager(LayerPriority.MIDDLE)?.delete(mapAnno)
            getPolygonManager(LayerPriority.HIGH)?.delete(mapAnno)
        }
    }

    private fun updateAnnotationOptions(
        mapOptions: SymbolOptions,
        options: WiskyPointAnnotationOptions,
    ) {
        mapOptions.apply {
            val point = Point.fromLngLat(options.latLng.longitude, options.latLng.latitude)
            withGeometry(point)
            options.iconOpacity?.let {
                withIconOpacity(it.toFloat())
            }
            options.imageId?.let {
                withIconImage(it)
            }
            options.iconAnchor?.let {
                withIconAnchor(it.toString())
            }
            options.iconOffset?.let {
                withIconOffset(it.map { it.toFloat() }.toTypedArray())
            }
            options.iconSize?.let {
                withIconSize(it.toFloat())
            }
            options.iconRotate?.let {
                withIconRotate(it.toFloat())
            }
            options.symbolSortKey?.let {
                withSymbolSortKey(it.toFloat())
            }
            options.textField?.let {
                withTextField(it)
            }
            options.textAnchor?.let {
                withTextAnchor(it.value)
            }
            options.textJustify?.let {
                withTextJustify(it.value)
            }
            options.textLineHeight?.let {
//            TODO 行高，暂时没有支持哦
                //                withTextLineHeight(it)
            }
            options.textMaxWidth?.let {
                withTextMaxWidth(it.toFloat())
            }
            options.textOffset?.let {
                withTextOffset(it.map { it.toFloat() }.toTypedArray())
            }
            options.textRadialOffset?.let {
                withTextRadialOffset(it.toFloat())
            }
            options.textRotate?.let {
                withTextRotate(it.toFloat())
            }
            options.textSize?.let {
                withTextSize(it.toFloat())
            }
            options.textColorInt?.let {
                withTextColor(String.format("#%06X", it and 0xFFFFFF))
            }
            options.textColorString?.let {
                withTextColor(it)
            }
            options.textHaloBlur?.let {
                withTextHaloBlur(it.toFloat())
            }
            options.textHaloColorInt?.let {
                withTextHaloColor(String.format("#%06X", it and 0xFFFFFF))
            }
            options.textHaloColorString?.let {
                withTextHaloColor(it)
            }
            withDraggable(false)
        }
    }

    /** 添加Annotation之后 */
    private fun addAnnotationSuccess(annotation: WiskyAnnotation<*>) {
        currentId++
        annotationList.add(annotation)
    }

    /**
     * 移动地图镜头到指定的位置
     */
    private fun moveCamera(update: CameraUpdate, duration: Long?) {
        easeCamera(update, duration)
    }

    private fun easeCamera(update: CameraUpdate, duration: Long?) {

        duration?.let {
            if (duration > 0) {
                mapboxMap?.easeCamera(update, duration.toInt())
            } else {
                mapboxMap?.easeCamera(update)
            }
        } ?: kotlin.run {
            mapboxMap?.easeCamera(update)
        }
    }


    inner class MapMoveGestureListener : MoveGestureDetector.OnMoveGestureListener {
        override fun onMoveBegin(detector: MoveGestureDetector): Boolean {
            if (detector.pointersCount == 1) {
                queryMapForFeatures(
                    ScreenCoordinate(
                        detector.focalPoint.x.toDouble(),
                        detector.focalPoint.y.toDouble()
                    )
                )?.let { list ->
                    list.firstOrNull { it.options.isDraggable }?.let {
                        startDragging(it)
                        return true
                    }
                }
            }
            return true
        }

        override fun onMove(
            detector: MoveGestureDetector,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            draggingAnnotation?.let { annotation ->
                if (detector.pointersCount > 1) {
                    stopDragging()
                    return true
                }
                val moveObject = detector.getMoveObject(0)
                val x = moveObject.currentX
                val y = moveObject.currentY
                val pointF = PointF(x, y)

                if (pointF.x < 0 || pointF.y < 0 || pointF.x > mapView.width || pointF.y > mapView.height) {
                    stopDragging()
                    return true
                }
                val point = fromCoordinate2Point(ScreenCoordinate(x.toDouble(), y.toDouble()))
                if (annotation is WiskyPointAnnotation) {
                    annotation.options.withLatLng(
                        point.longitude(), point.latitude(), annotation.options.latLng.altitude
                    )
                }
                (annotationMap[annotation] as? Symbol)?.let {
                    it.latLng = LatLng(point.latitude(), point.longitude())
                    if (it.symbolSortKey != null && it.symbolSortKey!! > 1) {
                        getPointManager(LayerPriority.MIDDLE)?.update(it)
                    } else {
                        getPointManager(LayerPriority.LOW)?.update(it)
                    }
                }

                dragListeners.forEach {
                    it.onAnnotationDrag(annotation)
                }
                return true
            }
            mapboxMap?.let {
                if (!distanceX.isNaN() && !distanceY.isNaN() && (distanceX != 0f || distanceY != 0f)) {
                    mapboxMap?.scrollBy(-distanceX, -distanceY)
                }

            }
            cameraMoveListener.forEach {
                it.onCameraMove()
            }
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector, velocityX: Float, velocityY: Float) {
            stopDragging()
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

    private fun selectAnnotation(annotation: WiskyAnnotation<*>) {
        interactionListener.forEach {
            it.onSelectAnnotation(annotation)
        }
    }

    private fun clickAnnotation(annotation: WiskyAnnotation<*>): Boolean {
        var process = false
        clickListeners.forEach {
            process = it.onAnnotationClick(annotation)
        }
        return process
    }


    private fun checkDoubleClick(): Boolean {
        if (abs(System.currentTimeMillis() - lastClickEvent) < DOUBLE_CLICK_GAP) {
            Log.i("mapbox", "mapbox double event happen, ignore return")
            return true
        }
        lastClickEvent = System.currentTimeMillis()
        return false
    }

    /**
     * 地图点击监听
     */
    inner class MapClickListener : MapboxMap.OnMapClickListener {
        override fun onMapClick(latlng: LatLng): Boolean {
            val coor =
                pixelForCoordinate(WiskyLatLng(latlng.latitude, latlng.longitude, latlng.altitude))
            val clickAnnList = queryMapForFeatures(ScreenCoordinate(coor.x, coor.y))
            if (clickAnnList.isNotEmpty()) {
                clickListeners.forEach { listener ->
                    val annotation = clickAnnList[0]
                    if (annotation.options.isTouchable) {
                        selectAnnotation(annotation)
                        listener.onAnnotationClick(annotation)
                    }
                }
                return true
            }

            if (checkDoubleClick()) {
                return false
            }
            mapClickListeners.forEach {
                it.onMapClick(
                    pointToWiskyLatLng(
                        Point.fromLngLat(
                            latlng.longitude,
                            latlng.latitude,
                            latlng.altitude
                        )
                    )
                )
            }
            return false
        }

    }

    private fun pointToWiskyLatLng(point: Point): WiskyLatLng {
        var alt = point.altitude()
        if (alt.isNaN()) {
            alt = 0.0
        }
        return WiskyLatLng(point.latitude(), point.longitude(), alt)
    }

    /**
     * 地图长按监听
     */
    inner class MapLongClickListener : MapboxMap.OnMapLongClickListener {
        override fun onMapLongClick(point: LatLng): Boolean {
            return false
        }

    }

    /**
     * 获取当前坐标对应的Annotation
     * @param screenCoordinate 坐标
     */
    private fun queryMapForFeatures(
        screenCoordinate: ScreenCoordinate,
    ): List<WiskyAnnotation<*>> {
        val latch = CountDownLatch(1)
        var clickAnnList = mutableListOf<WiskyAnnotation<*>>()
        mapboxMap?.apply {
            val list = mapboxMap?.queryRenderedFeatures(
                PointF(
                    screenCoordinate.x.toFloat(),
                    screenCoordinate.y.toFloat()
                )
            )
            if (list?.isEmpty() == true) {
                latch.countDown()
            } else {
                list?.forEach { feature ->
                    val type = feature.geometry()?.type()
                    annotationMap.forEach { (t, u) ->
                        WiskyLog.i(
                            TAG,
                            "mapliber --->${
                                feature.properties()?.get("id").toString()
                            }----->${u.id.toDouble().toString()}"
                        )
                        if ((t.options.isTouchable || t.options.isDraggable)
                            && feature.properties()?.get("id").toString() == u.id.toDouble()
                                .toString()
                        ) {
                            WiskyLog.i(
                                TAG,
                                "click Annotation ID = ${u.id}--->$type --->${System.currentTimeMillis()}"
                            )
                            when (type) {
                                "Point" -> {
                                    if (u is Symbol) clickAnnList.add(t)
                                }

                                "Polygon" -> {
                                    if (u is Fill) clickAnnList.add(t)
                                }

                                "LineString" -> {
                                    if (u is Line) clickAnnList.add(t)
                                }

                                "MultiPolygon" -> {
                                    if (u is Circle) clickAnnList.add(t)
                                }
                            }
                        }
                    }
                    latch.countDown()
                }
            }
        }
        latch.await(QUERY_WAIT_TIME, TimeUnit.SECONDS)
        return clickAnnList
    }

    /**
     * 地图缩放监听
     */
    inner class MapScaleListener : MapboxMap.OnScaleListener {
        override fun onScaleBegin(detector: StandardScaleGestureDetector) {
            scaleListeners.forEach {
                it.onScaleBegin(WiskyScaleGesture(detector.scaleFactor))
            }
        }

        override fun onScale(detector: StandardScaleGestureDetector) {
            scaleListeners.forEach {
                it.onScale(WiskyScaleGesture(detector.scaleFactor))
            }
        }

        override fun onScaleEnd(detector: StandardScaleGestureDetector) {
            scaleListeners.forEach {
                it.onScaleEnd(WiskyScaleGesture(detector.scaleFactor))
            }
        }

    }


    inner class SymbolDragListener : OnSymbolDragListener {
        override fun onAnnotationDragStarted(annotation: Symbol?) {
            annotation?.let {
                getWiskyAnnotation(it)?.let {
                    selectAnnotation(it)
                    startDragging(it)
                }
            }
        }

        override fun onAnnotationDrag(annotation: Symbol?) {
            val point = annotation?.latLng
            draggingAnnotation?.let { annotation ->
                if (!annotation.options.isDraggable) {
                    stopDragging()
                    return
                }
                if (annotation is WiskyPointAnnotation) {
                    annotation.options.withLatLng(
                        point?.longitude ?: 0.0,
                        point?.latitude ?: 0.0,
                        point?.altitude ?: 0.0
                    )
                }
                dragListeners.forEach {
                    it.onAnnotationDrag(annotation)
                }
            }
        }

        override fun onAnnotationDragFinished(annotation: Symbol?) {
            draggingAnnotation?.let {
                stopDragging()
            }
        }

    }

    private fun getWiskyAnnotation(annotation: com.mapbox.mapboxsdk.plugins.annotation.Annotation<*>): WiskyAnnotation<*>? {
        return annotationMap.filterValues { it == annotation }.keys.firstOrNull()
    }


    private fun getPointManager(priority: LayerPriority): SymbolManager? {
        return when (priority) {
            LayerPriority.LOW -> lowPointManager
            LayerPriority.MIDDLE -> midPointManager
            LayerPriority.HIGH -> highPointManager
        }
    }

    private fun getPolylineManager(priority: LayerPriority): LineManager? {
        return when (priority) {
            LayerPriority.LOW -> lowPolylineManager
            LayerPriority.MIDDLE -> midPolylineManager
            LayerPriority.HIGH -> highPolylineManager
        }
        return null
    }

    private fun getPolygonManager(priority: LayerPriority): FillManager? {
        return when (priority) {
            LayerPriority.LOW -> lowPolygonManager
            LayerPriority.MIDDLE -> midPolygonManager
            LayerPriority.HIGH -> highPolygonManager
        }
    }

    private fun getCircleManager(priority: LayerPriority): CircleManager? {
        return when (priority) {
            LayerPriority.LOW -> lowCircleManager
            LayerPriority.MIDDLE -> midCircleManager
            LayerPriority.HIGH -> highCircleManager
        }
    }

    private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024) // buffer size
                while (true) {
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
        }
    }

    private fun InputStream.readToString(): String {
        val r = BufferedReader(InputStreamReader(this))
        val total = StringBuilder("")
        var line: String?
        while (r.readLine().also { line = it } != null) {
            total.append(line).append('\n')
        }
        return total.toString()
    }

    fun Any.getPrivateFieldValue(fieldName: String): Any? {
        val field: Field? = javaClass.getDeclaredField(fieldName)
        field?.isAccessible = true
        return field?.get(this)
    }

}