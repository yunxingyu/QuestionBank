// This file is generated and will be overwritten automatically.
package com.comm.map.bean


import com.comm.map.util.RecordUtils
import java.io.Serializable
import java.util.*

/**
 * Various options for describing the viewpoint of a camera. All fields are
 * optional.
 *
 * Anchor and center points are mutually exclusive, with preference for the
 * center point when both are set.
 */
class WiskyCameraOptions private constructor(
    /** Coordinate at the center of the camera.  */
    val center: WiskyLatLng?,
    /**
     * Padding around the interior of the view that affects the frame of
     * reference for `center`.
     */
    val padding: WiskyEdgeInsets?,
    /**
     * Point of reference for `zoom` and `angle`, assuming an origin at the
     * top-left corner of the view.
     */
    val anchor: WiskyScreenCoordinate?,
    /**
     * Zero-based zoom level. Constrained to the minimum and maximum zoom
     * levels.
     */
    val zoom: Double?,
    /** Bearing, measured in degrees from true north. Wrapped to [0, 360).  */
    val bearing: Double?,
    /** Pitch toward the horizon measured in degrees.  */
    val pitch: Double?
) : Serializable {

    override fun equals(any: Any?): Boolean {
        if (this === any) {
            return true
        }
        if (any == null || javaClass != any.javaClass) {
            return false
        }
        val other = any as WiskyCameraOptions
        if (center != other.center) {
            return false
        }
        if (padding != other.padding) {
            return false
        }
        if (anchor != other.anchor) {
            return false
        }
        if (zoom != other.zoom) {
            return false
        }
        if (bearing != other.bearing) {
            return false
        }
        return pitch == other.pitch
    }

    override fun hashCode(): Int {
        return Objects.hash(
            center,
            padding,
            anchor,
            zoom,
            bearing,
            pitch
        )
    }

    class Builder {
        private var center: WiskyLatLng? = null
        private var padding: WiskyEdgeInsets? = null
        private var anchor: WiskyScreenCoordinate? = null
        private var zoom: Double? = null
        private var bearing: Double? = null
        private var pitch: Double? = null

        /** Coordinate at the center of the camera.  */
        fun center(center: WiskyLatLng?): Builder {
            this.center = center
            return this
        }

        /**
         * Padding around the interior of the view that affects the frame of
         * reference for `center`.
         */
        fun padding(padding: WiskyEdgeInsets?): Builder {
            this.padding = padding
            return this
        }

        /**
         * Point of reference for `zoom` and `angle`, assuming an origin at the
         * top-left corner of the view.
         */
        fun anchor(anchor: WiskyScreenCoordinate?): Builder {
            this.anchor = anchor
            return this
        }

        /**
         * Zero-based zoom level. Constrained to the minimum and maximum zoom
         * levels.
         */
        fun zoom(zoom: Double?): Builder {
            this.zoom = zoom
            return this
        }

        /** Bearing, measured in degrees from true north. Wrapped to [0, 360).  */
        fun bearing(bearing: Double?): Builder {
            this.bearing = bearing
            return this
        }

        /** Pitch toward the horizon measured in degrees.  */
        fun pitch(pitch: Double?): Builder {
            this.pitch = pitch
            return this
        }

        fun build(): WiskyCameraOptions {
            return WiskyCameraOptions(center, padding, anchor, zoom, bearing, pitch)
        }
    }

    fun toBuilder(): Builder {
        return Builder()
            .center(center)
            .padding(padding)
            .anchor(anchor)
            .zoom(zoom)
            .bearing(bearing)
            .pitch(pitch)
    }

    override fun toString(): String {
        return "[" + "center: " + RecordUtils.fieldToString(center) + ", " + "padding: " + RecordUtils.fieldToString(
            padding
        ) + ", " + "anchor: " + RecordUtils.fieldToString(anchor) + ", " + "zoom: " + RecordUtils.fieldToString(
            zoom
        ) + ", " + "bearing: " + RecordUtils.fieldToString(bearing) + ", " + "pitch: " + RecordUtils.fieldToString(
            pitch
        ) + "]"
    }
}