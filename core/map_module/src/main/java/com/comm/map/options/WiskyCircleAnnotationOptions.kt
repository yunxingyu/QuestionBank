package com.comm.map.options

import androidx.annotation.ColorInt
import com.google.gson.JsonElement
import com.mapbox.maps.extension.style.utils.ColorUtils
import com.comm.map.bean.WiskyLatLng

/**
 * @date 2022/7/15.
 * @author xieyutuo
 * @description 地图上标记视图的属性
 */
class WiskyCircleAnnotationOptions : WiskyBaseOptions() {

    private var data: JsonElement? = null

    companion object

    fun withLayerId(layerId: String): WiskyCircleAnnotationOptions {
        mLayerId = layerId
        return this
    }

    fun withSourceId(sourceId: String): WiskyCircleAnnotationOptions {
        this.mSourceId = sourceId
        return this
    }

    fun withBelowLayerId(belowLayerId: String): WiskyCircleAnnotationOptions {
        this.belowLayerId = belowLayerId
        return this
    }

    fun withAboveLayerId(aboveLayerId: String): WiskyCircleAnnotationOptions {
        this.aboveLayerId = aboveLayerId
        return this
    }


    /**
     * Sorts features in ascending order based on this value. Features with a higher sort key will appear above features with a lower sort key.
     */
    var circleSortKey: Double? = null

    /**
     * Set circle-sort-key to initialise the circleAnnotation with.
     *
     * Sorts features in ascending order based on this value. Features with a higher sort key will appear above features with a lower sort key.
     *
     * @param circleSortKey the circle-sort-key value
     * @return this
     */
    fun withCircleSortKey(circleSortKey: Double): WiskyCircleAnnotationOptions {
        this.circleSortKey = circleSortKey
        return this
    }

    /**
     * Amount to blur the circle. 1 blurs the circle such that only the centerpoint is full opacity.
     */
    var circleBlur: Double? = null

    /**
     * Set circle-blur to initialise the circleAnnotation with.
     *
     * Amount to blur the circle. 1 blurs the circle such that only the centerpoint is full opacity.
     *
     * @param circleBlur the circle-blur value
     * @return this
     */
    fun withCircleBlur(circleBlur: Double): WiskyCircleAnnotationOptions {
        this.circleBlur = circleBlur
        return this
    }

    /**
     * The fill color of the circle.
     */
    var circleColor: String? = null

    /**
     * Set circle-color to initialise the circleAnnotation with.
     *
     * The fill color of the circle.
     *
     * @param circleColor the circle-color value
     * @return this
     */
    fun withCircleColor(circleColor: String): WiskyCircleAnnotationOptions {
        this.circleColor = circleColor
        return this
    }

    /**
     * Set circle-color to initialise the circleAnnotation with.
     *
     * The fill color of the circle.
     *
     * @param circleColor the circle-color value with ColorInt format
     * @return this
     */
    fun withCircleColor(@ColorInt circleColor: Int): WiskyCircleAnnotationOptions {
        this.circleColor = ColorUtils.colorToRgbaString(circleColor)
        return this
    }

    /**
     * The opacity at which the circle will be drawn.
     */
    var circleOpacity: Double? = null

    /**
     * Set circle-opacity to initialise the circleAnnotation with.
     *
     * The opacity at which the circle will be drawn.
     *
     * @param circleOpacity the circle-opacity value
     * @return this
     */
    fun withCircleOpacity(circleOpacity: Double): WiskyCircleAnnotationOptions {
        this.circleOpacity = circleOpacity
        return this
    }

    /**
     * Circle radius.
     */
    var circleRadius: Double? = null

    /**
     * Set circle-radius to initialise the circleAnnotation with.
     *
     * Circle radius.
     *
     * @param circleRadius the circle-radius value
     * @return this
     */
    fun withCircleRadius(circleRadius: Double): WiskyCircleAnnotationOptions {
        this.circleRadius = circleRadius
        return this
    }

    /**
     * The stroke color of the circle.
     */
    var circleStrokeColor: String? = null

    /**
     * Set circle-stroke-color to initialise the circleAnnotation with.
     *
     * The stroke color of the circle.
     *
     * @param circleStrokeColor the circle-stroke-color value
     * @return this
     */
    fun withCircleStrokeColor(circleStrokeColor: String): WiskyCircleAnnotationOptions {
        this.circleStrokeColor = circleStrokeColor
        return this
    }

    /**
     * Set circle-stroke-color to initialise the circleAnnotation with.
     *
     * The stroke color of the circle.
     *
     * @param circleStrokeColor the circle-stroke-color value with ColorInt format
     * @return this
     */
    fun withCircleStrokeColor(@ColorInt circleStrokeColor: Int): WiskyCircleAnnotationOptions {
        this.circleStrokeColor = ColorUtils.colorToRgbaString(circleStrokeColor)
        return this
    }

    /**
     * The opacity of the circle's stroke.
     */
    var circleStrokeOpacity: Double? = null

    /**
     * Set circle-stroke-opacity to initialise the circleAnnotation with.
     *
     * The opacity of the circle's stroke.
     *
     * @param circleStrokeOpacity the circle-stroke-opacity value
     * @return this
     */
    fun withCircleStrokeOpacity(circleStrokeOpacity: Double): WiskyCircleAnnotationOptions {
        this.circleStrokeOpacity = circleStrokeOpacity
        return this
    }

    /**
     * The width of the circle's stroke. Strokes are placed outside of the `circle-radius`.
     */
    var circleStrokeWidth: Double? = null

    /**
     * Set circle-stroke-width to initialise the circleAnnotation with.
     *
     * The width of the circle's stroke. Strokes are placed outside of the {@link PropertyFactory#circleRadius}.
     *
     * @param circleStrokeWidth the circle-stroke-width value
     * @return this
     */
    fun withCircleStrokeWidth(circleStrokeWidth: Double): WiskyCircleAnnotationOptions {
        this.circleStrokeWidth = circleStrokeWidth
        return this
    }

    /**
     * Set the LatLng of the circleAnnotation, which represents the location of the circleAnnotation on the map
     *
     * @return this
     */
    fun withLatLng(lng: Double, lat: Double, altitude: Double = 0.0): WiskyCircleAnnotationOptions {
        latLng.longitude = lng
        latLng.latitude = lat
        latLng.altitude = altitude
        return this
    }

    fun getPoint(): WiskyLatLng {
        return latLng
    }

    /**
     * Returns whether this circleAnnotation is draggable, meaning it can be dragged across the screen when touched and moved.
     *
     * @return draggable when touched
     */
    fun getDraggable(): Boolean {
        return isDraggable
    }

    /**
     * Set whether this circleAnnotation should be draggable,
     * meaning it can be dragged across the screen when touched and moved.
     *
     * @param draggable should be draggable
     */
    fun withDraggable(draggable: Boolean): WiskyCircleAnnotationOptions {
        isDraggable = draggable
        return this
    }

    /**
     * Set the arbitrary json data of the annotation.
     *
     * @param jsonElement the arbitrary json element data
     */
    fun withData(jsonElement: JsonElement): WiskyCircleAnnotationOptions {
        this.data = jsonElement
        return this
    }

    /**
     * Get the arbitrary json data of the annotation.
     *
     * @return the arbitrary json object data if set, else null
     */
    fun getData(): JsonElement? {
        return data
    }

}