package com.comm.map.options

import androidx.annotation.LayoutRes

/**
 * @date 2022/7/15.
 * @author xieyutuo
 * @description 地图上自定义视图的属性
 */
class WiskyViewAnnotationOptions : WiskyBaseOptions() {

    companion object {
        /** the default name for icon */
        const val ICON_DEFAULT_NAME_PREFIX = "view_default_name_"
    }

    private var layoutId: Int = 0

    var imageId: String? = null


    fun withLayoutId(@LayoutRes layoutId: Int): WiskyViewAnnotationOptions {
        this.layoutId = layoutId
        if (imageId == null || imageId!!.startsWith(ICON_DEFAULT_NAME_PREFIX)) {
            imageId = ICON_DEFAULT_NAME_PREFIX + layoutId.hashCode()
        }
        return this
    }

    fun getLayout(): Int {
        return layoutId
    }

    fun withLatLng(lng: Double, lat: Double, altitude: Double = 0.0): WiskyViewAnnotationOptions {
        latLng.longitude = lng
        latLng.latitude = lat
        latLng.altitude = altitude
        return this
    }

    fun withDraggable(draggable: Boolean): WiskyViewAnnotationOptions {
        isDraggable = draggable
        return this
    }

}