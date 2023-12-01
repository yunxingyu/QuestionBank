package com.comm.map.options

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.comm.map.generated.WiskyTextAnchor
import com.comm.map.generated.WiskyTextJustify
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.ConvertUtils
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions

/**
 * @date 2022/7/15.
 * @author xieyutuo
 * @description 地图上标记视图的属性
 */
class WiskyPointAnnotationOptions : WiskyBaseOptions() {

    private var data: JsonElement? = null

    private var iconImageBitmap: Bitmap? = null


    companion object {

        /** the default name for icon */
        const val ICON_DEFAULT_NAME_PREFIX = "icon_default_name_"


        /** The property for icon-anchor */
        const val PROPERTY_ICON_ANCHOR = "icon-anchor"

        /** The property for icon-image */
        const val PROPERTY_ICON_IMAGE = "icon-image"

        /** The property for icon-offset */
        const val PROPERTY_ICON_OFFSET = "icon-offset"

        /** The property for icon-rotate */
        const val PROPERTY_ICON_ROTATE = "icon-rotate"

        /** The property for icon-size */
        const val PROPERTY_ICON_SIZE = "icon-size"

        /** The property for symbol-sort-key */
        const val PROPERTY_SYMBOL_SORT_KEY = "symbol-sort-key"

        /** The property for is-draggable */
        private const val PROPERTY_IS_DRAGGABLE = "is-draggable"
    }

    fun build(): JsonObject {
        val jsonObject = JsonObject()
        iconAnchor?.let {
            jsonObject.addProperty(PointAnnotationOptions.PROPERTY_ICON_ANCHOR, it.value)
        }
        imageId?.let {
            jsonObject.addProperty(PointAnnotationOptions.PROPERTY_ICON_IMAGE, it)
        }
        iconOffset?.let {
            jsonObject.add(
                PointAnnotationOptions.PROPERTY_ICON_OFFSET,
                ConvertUtils.convertDoubleArray(it)
            )
        }
        iconRotate?.let {
            jsonObject.addProperty(PointAnnotationOptions.PROPERTY_ICON_ROTATE, it)
        }
        iconSize?.let {
            jsonObject.addProperty(PointAnnotationOptions.PROPERTY_ICON_SIZE, it)
        }
        symbolSortKey?.let {
            jsonObject.addProperty(PointAnnotationOptions.PROPERTY_SYMBOL_SORT_KEY, it)
        }
        return jsonObject
    }

    fun withLatLng(lng: Double, lat: Double, altitude: Double = 0.0): WiskyPointAnnotationOptions {
        latLng.longitude = lng
        latLng.latitude = lat
        latLng.altitude = altitude
        return this
    }

    /**
     * Set whether this pointAnnotation should be draggable,
     * meaning it can be dragged across the screen when touched and moved.
     *
     * @param draggable should be draggable
     */
    fun withDraggable(draggable: Boolean): WiskyPointAnnotationOptions {
        isDraggable = draggable
        return this
    }

    /**
     * Returns whether this pointAnnotation is draggable, meaning it can be dragged across the screen when touched and moved.
     *
     * @return draggable when touched
     */
    fun getDraggable(): Boolean {
        return isDraggable
    }

    fun withSelected(selected: Boolean): WiskyPointAnnotationOptions {
        isSelected = selected
        return this
    }

    fun withLayerId(layerId: String): WiskyPointAnnotationOptions {
        mLayerId = layerId
        return this
    }

    fun withSourceId(sourceId: String): WiskyPointAnnotationOptions {
        this.mSourceId = sourceId
        return this
    }

    fun withBelowLayerId(belowLayerId: String): WiskyPointAnnotationOptions {
        this.belowLayerId = belowLayerId
        return this
    }

    fun withAboveLayerId(aboveLayerId: String): WiskyPointAnnotationOptions {
        this.aboveLayerId = aboveLayerId
        return this
    }

    /**
     * ID of the image
     */
    var imageId: String? = null

    /**
     * Set icon-image to initialise the pointAnnotation with.
     *
     * ID of the image
     *
     * @param imageId the icon-image value
     * @return this
     */
    fun withIconImage(imageId: String): WiskyPointAnnotationOptions {
        this.imageId = imageId
        return this
    }

    fun withIconImage(@DrawableRes idRes: Int, context: Context): WiskyPointAnnotationOptions {
        withIconImage(BitmapFactory.decodeResource(context.resources, idRes))
        return this
    }


    /**
     * Set bitmap icon-image to initialise the symbol.
     *
     * Will not take effect if a String iconImage name has been set.
     *
     * @param iconImageBitmap the bitmap image
     * @return this
     */
    fun withIconImage(iconImageBitmap: Bitmap): WiskyPointAnnotationOptions {
        this.iconImageBitmap = iconImageBitmap
        if (imageId == null || imageId!!.startsWith(ICON_DEFAULT_NAME_PREFIX)) {
            imageId = ICON_DEFAULT_NAME_PREFIX + iconImageBitmap.hashCode()
        }
        return this
    }

    fun withVisibility(visibility: Boolean): WiskyPointAnnotationOptions {
        this.visibility = visibility
        return this
    }

    fun getImageBitmap(): Bitmap? {
        return iconImageBitmap
    }

    /**
     * Part of the icon placed closest to the anchor.
     */
    var iconAnchor: IconAnchor? = null

    /**
     * Set icon-anchor to initialise the pointAnnotation with.
     *
     * Part of the icon placed closest to the anchor.
     *
     * @param iconAnchor the icon-anchor value
     * @return this
     */
    fun withIconAnchor(iconAnchor: IconAnchor): WiskyPointAnnotationOptions {
        this.iconAnchor = iconAnchor
        return this
    }

    /**
     * Offset distance of icon from its anchor. Positive values indicate right and down, while negative values indicate left and up. Each component is multiplied by the value of `icon-size` to obtain the final offset in pixels. When combined with `icon-rotate` the offset will be as if the rotated direction was up.
     */
    var iconOffset: List<Double>? = null

    /**
     * Set icon-offset to initialise the pointAnnotation with.
     *
     * Offset distance of icon from its anchor. Positive values indicate right and down, while negative values indicate left and up. Each component is multiplied by the value of {@link PropertyFactory#iconSize} to obtain the final offset in density-independent pixels. When combined with {@link PropertyFactory#iconRotate} the offset will be as if the rotated direction was up.
     *
     * @param iconOffset the icon-offset value
     * @return this
     */
    fun withIconOffset(iconOffset: List<Double>): WiskyPointAnnotationOptions {
        this.iconOffset = iconOffset
        return this
    }

    /**
     * Rotates the icon clockwise.
     */
    var iconRotate: Double? = null

    /**
     * Set icon-rotate to initialise the pointAnnotation with.
     *
     * Rotates the icon clockwise.
     *
     * @param iconRotate the icon-rotate value
     * @return this
     */
    fun withIconRotate(iconRotate: Double): WiskyPointAnnotationOptions {
        this.iconRotate = iconRotate
        return this
    }


    /**
     * Scales the original size of the icon by the provided factor. The new pixel size of the image will be the original pixel size multiplied by `icon-size`. 1 is the original size; 3 triples the size of the image.
     */
    var iconSize: Double? = null

    /**
     * Set icon-size to initialise the pointAnnotation with.
     *
     * Scales the original size of the icon by the provided factor. The new pixel size of the image will be the original pixel size multiplied by {@link PropertyFactory#iconSize}. 1 is the original size; 3 triples the size of the image.
     *
     * @param iconSize the icon-size value
     * @return this
     */
    fun withIconSize(iconSize: Double): WiskyPointAnnotationOptions {
        this.iconSize = iconSize
        return this
    }

    fun withTag(tag: String): WiskyPointAnnotationOptions {
        this.tag = tag
        return this
    }

    /**
     * Sorts features in ascending order based on this value. Features with lower sort keys are drawn and placed first.  When `icon-allow-overlap` or `text-allow-overlap` is `false`, features with a lower sort key will have priority during placement. When `icon-allow-overlap` or `text-allow-overlap` is set to `true`, features with a higher sort key will overlap over features with a lower sort key.
     */
    var symbolSortKey: Double? = null

    /**
     * Set symbol-sort-key to initialise the pointAnnotation with.
     *
     * Sorts features in ascending order based on this value. Features with lower sort keys are drawn and placed first.  When {@link PropertyFactory#iconAllowOverlap} or {@link PropertyFactory#textAllowOverlap} is `false`, features with a lower sort key will have priority during placement. When {@link PropertyFactory#iconAllowOverlap} or {@link PropertyFactory#textAllowOverlap} is set to `true`, features with a higher sort key will overlap over features with a lower sort key.
     *
     * @param symbolSortKey the symbol-sort-key value
     * @return this
     */
    fun withSymbolSortKey(symbolSortKey: Double): WiskyPointAnnotationOptions {
        this.symbolSortKey = symbolSortKey
        return this
    }

    fun withIconOpacity(iconOpacity: Double): WiskyPointAnnotationOptions {
        this.iconOpacity = iconOpacity
        return this
    }


    var textAnchor: WiskyTextAnchor? = null

    fun withTextAnchor(textAnchor: WiskyTextAnchor): WiskyPointAnnotationOptions {
        this.textAnchor = textAnchor
        return this
    }

    var textJustify: WiskyTextJustify? = null

    fun withTextJustify(textJustify: WiskyTextJustify): WiskyPointAnnotationOptions {
        this.textJustify = textJustify
        return this
    }

    var textField: String? = null

    fun withTextField(textField: String): WiskyPointAnnotationOptions {
        this.textField = textField
        return this
    }

    var textLineHeight: Double? = null

    fun withTextLineHeight(textLineHeight: Double): WiskyPointAnnotationOptions {
        this.textLineHeight = textLineHeight
        return this
    }

    var textMaxWidth: Double? = null

    fun withTextMaxWidth(textLineHeight: Double): WiskyPointAnnotationOptions {
        this.textMaxWidth = textMaxWidth
        return this
    }

    var textOffset: List<Double>? = null

    fun withTextOffset(textOffset: List<Double>): WiskyPointAnnotationOptions {
        this.textOffset = textOffset
        return this
    }

    var textRadialOffset: Double? = null

    fun withTextRadialOffset(textRadialOffset: Double): WiskyPointAnnotationOptions {
        this.textRadialOffset = textRadialOffset
        return this
    }

    var textRotate: Double? = null

    fun withTextRotate(textRotate: Double): WiskyPointAnnotationOptions {
        this.textRotate = textRotate
        return this
    }

    var textSize: Double? = null

    fun withTextSize(textSize: Double): WiskyPointAnnotationOptions {
        this.textSize = textSize
        return this
    }

    var textColorInt: Int? = null

    fun withTextColorInt(@ColorInt textColorInt: Int): WiskyPointAnnotationOptions {
        this.textColorInt = textColorInt
        return this
    }

    var textColorString: String? = null

    fun withTextColorString(textColorString: String): WiskyPointAnnotationOptions {
        this.textColorString = textColorString
        return this
    }

    var textHaloBlur: Double? = null

    fun withTextHaloBlur(textHaloBlur: Double): WiskyPointAnnotationOptions {
        this.textHaloBlur = textHaloBlur
        return this
    }

    var textHaloColorInt: Int? = null

    fun withTextHaloColorInt(@ColorInt textHaloColorInt: Int): WiskyPointAnnotationOptions {
        this.textHaloColorInt = textHaloColorInt
        return this
    }

    var textHaloColorString: String? = null

    fun withTextHaloColorString(textHaloColorString: String): WiskyPointAnnotationOptions {
        this.textHaloColorString = textHaloColorString
        return this
    }

    /**
     * Set the arbitrary json data of the annotation.
     *
     * @param jsonElement the arbitrary json element data
     */
    fun withData(jsonElement: JsonElement): WiskyPointAnnotationOptions {
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