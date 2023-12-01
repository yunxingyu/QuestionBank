package com.comm.map.generated

import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.TextJustify


/**
 * @date 2022/10/28.
 * @author xieyutuo
 * @description TODO
 */
internal interface LayerProperty {
    val value: String
}

enum class WiskyTextAnchor(override val value: String) : LayerProperty {
    /**
     * The center of the text is placed closest to the anchor.
     */
    CENTER("center"),

    /**
     * The left side of the text is placed closest to the anchor.
     */
    LEFT("left"),

    /**
     * The right side of the text is placed closest to the anchor.
     */
    RIGHT("right"),

    /**
     * The top of the text is placed closest to the anchor.
     */
    TOP("top"),

    /**
     * The bottom of the text is placed closest to the anchor.
     */
    BOTTOM("bottom"),

    /**
     * The top left corner of the text is placed closest to the anchor.
     */
    TOP_LEFT("top-left"),

    /**
     * The top right corner of the text is placed closest to the anchor.
     */
    TOP_RIGHT("top-right"),

    /**
     * The bottom left corner of the text is placed closest to the anchor.
     */
    BOTTOM_LEFT("bottom-left"),

    /**
     * The bottom right corner of the text is placed closest to the anchor.
     */
    BOTTOM_RIGHT("bottom-right");

    companion object {
        fun findMapboxEnum(value: String): TextAnchor {
            val values = TextAnchor.values()
            values.forEach {
                if (it.value == value) {
                    return it
                }
            }
            return TextAnchor.CENTER
        }
    }
}

enum class WiskyTextJustify(override val value: String) : LayerProperty {
    /**
     * The text is aligned towards the anchor position.
     */
    AUTO("auto"),

    /**
     * The text is aligned to the left.
     */
    LEFT("left"),

    /**
     * The text is centered.
     */
    CENTER("center"),

    /**
     * The text is aligned to the right.
     */
    RIGHT("right");

    companion object {
        fun findMapboxEnum(value: String): TextJustify {
            val values = TextJustify.values()
            values.forEach {
                if (it.value == value) {
                    return it
                }
            }
            return TextJustify.CENTER
        }
    }
}