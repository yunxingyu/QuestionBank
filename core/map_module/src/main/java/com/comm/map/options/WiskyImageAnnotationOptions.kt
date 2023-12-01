package com.comm.map.options

import android.graphics.Bitmap

/**
 * @date 2022/7/29.
 * @author xieyutuo
 * @description TODO
 */
class WiskyImageAnnotationOptions : WiskyBaseOptions() {
    var width: Double? = null
    var height: Double? = null
    var rotate: Double? = null
    var points: List<List<Double>>? = null
    var resId: Int? = null
    var bitmap: Bitmap? = null

    /**
     * ID of the image
     */
    var imageId: String? = null
}