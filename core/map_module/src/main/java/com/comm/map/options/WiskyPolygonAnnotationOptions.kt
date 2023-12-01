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
class WiskyPolygonAnnotationOptions : WiskyBaseOptions() {

    private var data: JsonElement? = null

    fun withLayerId(layerId: String): WiskyPolygonAnnotationOptions {
        mLayerId = layerId
        return this
    }

    fun withIsTouchable(touchable: Boolean): WiskyPolygonAnnotationOptions {
        isTouchable = touchable
        return this
    }

    fun withSourceId(sourceId: String): WiskyPolygonAnnotationOptions {
        this.mSourceId = sourceId
        return this
    }

    fun withBelowLayerId(belowLayerId: String): WiskyPolygonAnnotationOptions {
        this.belowLayerId = belowLayerId
        return this
    }

    fun withAboveLayerId(aboveLayerId: String): WiskyPolygonAnnotationOptions {
        this.aboveLayerId = aboveLayerId
        return this
    }


    /**
     * Sorts features in ascending order based on this value. Features with a higher sort key will appear above features with a lower sort key.
     */
    var fillSortKey: Double? = null

    /**
     * Set fill-sort-key to initialise the polygonAnnotation with.
     *
     * Sorts features in ascending order based on this value. Features with a higher sort key will appear above features with a lower sort key.
     *
     * @param fillSortKey the fill-sort-key value
     * @return this
     */
    fun withFillSortKey(fillSortKey: Double): WiskyPolygonAnnotationOptions {
        this.fillSortKey = fillSortKey
        return this
    }

    /**
     * The color of the filled part of this layer. This color can be specified as `rgba` with an alpha component and the color's opacity will not affect the opacity of the 1px stroke, if it is used.
     */
    var fillColor: String? = null

    /**
     * Set fill-color to initialise the polygonAnnotation with.
     *
     * The color of the filled part of this layer. This color can be specified as `rgba` with an alpha component and the color's opacity will not affect the opacity of the 1px stroke, if it is used.
     *
     * @param fillColor the fill-color value
     * @return this
     */
    fun withFillColor(fillColor: String): WiskyPolygonAnnotationOptions {
        this.fillColor = fillColor
        return this
    }

    /**
     * Set fill-color to initialise the polygonAnnotation with.
     *
     * The color of the filled part of this layer. This color can be specified as `rgba` with an alpha component and the color's opacity will not affect the opacity of the 1px stroke, if it is used.
     *
     * @param fillColor the fill-color value with ColorInt format
     * @return this
     */
    fun withFillColor(@ColorInt fillColor: Int): WiskyPolygonAnnotationOptions {
        this.fillColor = ColorUtils.colorToRgbaString(fillColor)
        return this
    }

    /**
     * The opacity of the entire fill layer. In contrast to the `fill-color`, this value will also affect the 1px stroke around the fill, if the stroke is used.
     */
    var fillOpacity: Double? = null

    /**
     * Set fill-opacity to initialise the polygonAnnotation with.
     *
     * The opacity of the entire fill layer. In contrast to the {@link PropertyFactory#fillColor}, this value will also affect the 1px stroke around the fill, if the stroke is used.
     *
     * @param fillOpacity the fill-opacity value
     * @return this
     */
    fun withFillOpacity(fillOpacity: Double): WiskyPolygonAnnotationOptions {
        this.fillOpacity = fillOpacity
        return this
    }

    /**
     * The outline color of the fill. Matches the value of `fill-color` if unspecified.
     */
    var fillOutlineColor: String? = null

    /**
     * Set fill-outline-color to initialise the polygonAnnotation with.
     *
     * The outline color of the fill. Matches the value of {@link PropertyFactory#fillColor} if unspecified.
     *
     * @param fillOutlineColor the fill-outline-color value
     * @return this
     */
    fun withFillOutlineColor(fillOutlineColor: String): WiskyPolygonAnnotationOptions {
        this.fillOutlineColor = fillOutlineColor
        return this
    }

    /**
     * Set fill-outline-color to initialise the polygonAnnotation with.
     *
     * The outline color of the fill. Matches the value of {@link PropertyFactory#fillColor} if unspecified.
     *
     * @param fillOutlineColor the fill-outline-color value with ColorInt format
     * @return this
     */
    fun withFillOutlineColor(@ColorInt fillOutlineColor: Int): WiskyPolygonAnnotationOptions {
        this.fillOutlineColor = ColorUtils.colorToRgbaString(fillOutlineColor)
        return this
    }

    /**
     * Name of image in sprite to use for drawing image fills. For seamless patterns, image width and height must be a factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will be evaluated only at integer zoom levels.
     */
    var fillPattern: String? = null

    /**
     * Set fill-pattern to initialise the polygonAnnotation with.
     *
     * Name of image in sprite to use for drawing image fills. For seamless patterns, image width and height must be a factor of two (2, 4, 8, ..., 512). Note that zoom-dependent expressions will be evaluated only at integer zoom levels.
     *
     * @param fillPattern the fill-pattern value
     * @return this
     */
    fun withFillPattern(fillPattern: String): WiskyPolygonAnnotationOptions {
        this.fillPattern = fillPattern
        return this
    }

    private var points: List<List<WiskyLatLng>>? = null

    /**
     * Set a list of lists of Point for the fill, which represents the locations of the fill on the map
     *
     * @param points a list of a lists of the locations of the line in a longitude and latitude pairs
     * @return this
     */
    fun withPoints(points: List<List<WiskyLatLng>>): WiskyPolygonAnnotationOptions {
        this.points = points
        return this
    }

    /**
     * Get a list of lists of Point for the fill, which represents the locations of the fill on the map
     *
     * @return a list of a lists of the locations of the line in a longitude and latitude pairs
     */
    fun getPoints(): List<List<WiskyLatLng>> {
        return points ?: ArrayList()
    }


    /**
     * Returns whether this polygonAnnotation is draggable, meaning it can be dragged across the screen when touched and moved.
     *
     * @return draggable when touched
     */
    fun getDraggable(): Boolean {
        return isDraggable
    }

    /**
     * Set whether this polygonAnnotation should be draggable,
     * meaning it can be dragged across the screen when touched and moved.
     *
     * @param draggable should be draggable
     */
    fun withDraggable(draggable: Boolean): WiskyPolygonAnnotationOptions {
        isDraggable = draggable
        return this
    }

    /**
     * Set the arbitrary json data of the annotation.
     *
     * @param jsonElement the arbitrary json element data
     */
    fun withData(jsonElement: JsonElement): WiskyPolygonAnnotationOptions {
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