package com.comm.map.annotation

import com.comm.map.options.WiskyPolylineAnnotationOptions

/**
 * @date 2022/7/21.
 * @author xieyutuo
 * @description TODO
 */
class WiskyPolyLineAnnotation(
    id: Long,
    options: WiskyPolylineAnnotationOptions,
) : WiskyAnnotation<WiskyPolylineAnnotationOptions>(id, options) {

    override fun getType(): WiskyAnnotationType {
        return WiskyAnnotationType.PolylineAnnotation
    }

}