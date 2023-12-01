package com.comm.map.options

import androidx.annotation.ColorInt
import com.google.gson.JsonElement
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import com.mapbox.maps.extension.style.utils.ColorUtils
import com.comm.map.bean.WiskyLatLng

/**
 * @date 2022/7/15.
 * @author xieyutuo
 * @description 地图上标记视图的属性
 */
class WiskyPolylineAnnotationOptions : WiskyBaseOptions() {

//    private val id = ID_GENERATOR.incrementAndGet()

    companion object {

        /** The generator for id */
//        var ID_GENERATOR = AtomicLong(0)

//        const val DEFAULT_LAYER_ID = "Wisky-polylineAnnotation-layer-"
//        const val DEFAULT_SOURCE_ID = "Wisky-polylineAnnotation-source-"
    }

    fun withLayerId(layerId: String): WiskyPolylineAnnotationOptions {
        mLayerId = layerId
        return this
    }

    fun withSourceId(sourceId: String): WiskyPolylineAnnotationOptions {
        this.mSourceId = sourceId
        return this
    }

    fun withBelowLayerId(belowLayerId: String): WiskyPolylineAnnotationOptions {
        this.belowLayerId = belowLayerId
        return this
    }

    fun withAboveLayerId(aboveLayerId: String): WiskyPolylineAnnotationOptions {
        this.aboveLayerId = aboveLayerId
        return this
    }

    fun withVisibility(visibility: Boolean): WiskyPolylineAnnotationOptions {
        this.visibility = visibility
        return this
    }

    /** A list of LatLng for the line, which represents the locations of the line on the map*/
    private var points = mutableListOf<WiskyLatLng>()


    /**
     * The display of lines when joining.
     */
    var lineJoin: LineJoin? = null

    /**
     * Set line-join to initialise the polylineAnnotation with.
     *
     * The display of lines when joining.
     *
     * @param lineJoin the line-join value
     * @return this
     */
    fun withLineJoin(lineJoin: LineJoin): WiskyPolylineAnnotationOptions {
        this.lineJoin = lineJoin
        return this
    }

    /**
     * Sorts features in ascending order based on this value. Features with a higher sort key will appear above features with a lower sort key.
     */
    var lineSortKey: Double = 0.0

    /**
     * Set line-sort-key to initialise the polylineAnnotation with.
     *
     * Sorts features in ascending order based on this value. Features with a higher sort key will appear above features with a lower sort key.
     *
     * @param lineSortKey the line-sort-key value
     * @return this
     */
    fun withLineSortKey(lineSortKey: Double): WiskyPolylineAnnotationOptions {
        this.lineSortKey = lineSortKey
        return this
    }

    /**
     * Blur applied to the line, in pixels.
     */
    var lineBlur: Double? = null

    /**
     * Set line-blur to initialise the polylineAnnotation with.
     *
     * Blur applied to the line, in density-independent pixels.
     *
     * @param lineBlur the line-blur value
     * @return this
     */
    fun withLineBlur(lineBlur: Double): WiskyPolylineAnnotationOptions {
        this.lineBlur = lineBlur
        return this
    }

    /**
     * The color with which the line will be drawn.
     */
    var lineColor: String? = null

    /**
     * Set line-color to initialise the polylineAnnotation with.
     *
     * The color with which the line will be drawn.
     *
     * @param lineColor the line-color value
     * @return this
     */
    fun withLineColor(lineColor: String): WiskyPolylineAnnotationOptions {
        this.lineColor = lineColor
        return this
    }

    /**
     * Set line-color to initialise the polylineAnnotation with.
     *
     * The color with which the line will be drawn.
     *
     * @param lineColor the line-color value with ColorInt format
     * @return this
     */
    fun withLineColor(@ColorInt lineColor: Int): WiskyPolylineAnnotationOptions {
        this.lineColor = ColorUtils.colorToRgbaString(lineColor)
        return this
    }

    /**
     * Draws a line casing outside of a line's actual path. Value indicates the width of the inner gap.
     */
    var lineGapWidth: Double? = null

    /**
     * Set line-gap-width to initialise the polylineAnnotation with.
     *
     * Draws a line casing outside of a line's actual path. Value indicates the width of the inner gap.
     *
     * @param lineGapWidth the line-gap-width value
     * @return this
     */
    fun withLineGapWidth(lineGapWidth: Double): WiskyPolylineAnnotationOptions {
        this.lineGapWidth = lineGapWidth
        return this
    }

    /**
     *  Specifies the lengths of the alternating dashes and gaps that form the dash pattern
     */
    var lineDashArray: List<Double>? = null

    /**
     * Specifies the lengths of the alternating dashes and gaps that form the dash pattern. The lengths are later scaled by the line width. To convert a dash length to pixels, multiply the length by the current line width.
     *
     * @param lineDashArray value of lineDashArray
     * @return this
     */
    fun withLineDashArray(lineDashArray: List<Double>): WiskyPolylineAnnotationOptions {
        this.lineDashArray = lineDashArray
        return this
    }

    /**
     * The line's offset. For linear features, a positive value offsets the line to the right, relative to the direction of the line, and a negative value to the left. For polygon features, a positive value results in an inset, and a negative value results in an outset.
     */
    var lineOffset: Double? = null

    /**
     * Set line-offset to initialise the polylineAnnotation with.
     *
     * The line's offset. For linear features, a positive value offsets the line to the right, relative to the direction of the line, and a negative value to the left. For polygon features, a positive value results in an inset, and a negative value results in an outset.
     *
     * @param lineOffset the line-offset value
     * @return this
     */
    fun withLineOffset(lineOffset: Double): WiskyPolylineAnnotationOptions {
        this.lineOffset = lineOffset
        return this
    }

    /**
     * The opacity at which the line will be drawn.
     */
    var lineOpacity: Double? = null

    /**
     * Set line-opacity to initialise the polylineAnnotation with.
     *
     * The opacity at which the line will be drawn.
     *
     * @param lineOpacity the line-opacity value
     * @return this
     */
    fun withLineOpacity(lineOpacity: Double): WiskyPolylineAnnotationOptions {
        this.lineOpacity = lineOpacity
        return this
    }

    /**
     * Name of image in sprite to use for drawing image lines. For seamless patterns, image width must be a factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will be evaluated only at integer zoom levels.
     */
    var linePattern: String? = null

    /**
     * Set line-pattern to initialise the polylineAnnotation with.
     *
     * Name of image in sprite to use for drawing image lines. For seamless patterns, image width must be a factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will be evaluated only at integer zoom levels.
     *
     * @param linePattern the line-pattern value
     * @return this
     */
    fun withLinePattern(linePattern: String): WiskyPolylineAnnotationOptions {
        this.linePattern = linePattern
        return this
    }

    /**
     * Stroke thickness.
     */
    var lineWidth: Double? = null

    /**
     * Set line-width to initialise the polylineAnnotation with.
     *
     * Stroke thickness.
     *
     * @param lineWidth the line-width value
     * @return this
     */
    fun withLineWidth(lineWidth: Double): WiskyPolylineAnnotationOptions {
        this.lineWidth = lineWidth
        return this
    }

    /**
     * Set a list of LatLng for the line, which represents the locations of the line on the map
     *
     * @param points a list of the locations of the line in a longitude and latitude pairs
     * @return this
     */
    fun withPoints(points: MutableList<WiskyLatLng>): WiskyPolylineAnnotationOptions {
        this.points = points
        return this
    }

    /**
     * Get a list of LatLng for the line, which represents the locations of the line on the map
     *
     * @return a list of the locations of the line in a longitude and latitude pairs
     */
    fun getPoints(): MutableList<WiskyLatLng> {
        return points
    }


    /**
     * Returns whether this polylineAnnotation is draggable, meaning it can be dragged across the screen when touched and moved.
     *
     * @return draggable when touched
     */
    fun getDraggable(): Boolean {
        return isDraggable
    }

    /**
     * Set whether this polylineAnnotation should be draggable,
     * meaning it can be dragged across the screen when touched and moved.
     *
     * @param draggable should be draggable
     */
    fun withDraggable(draggable: Boolean): WiskyPolylineAnnotationOptions {
        isDraggable = draggable
        return this
    }

    private var data: JsonElement? = null

    /**
     * Set the arbitrary json data of the annotation.
     *
     * @param jsonElement the arbitrary json element data
     */
    fun withData(jsonElement: JsonElement): WiskyPolylineAnnotationOptions {
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