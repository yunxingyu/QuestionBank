package com.comm.map.annotation

import com.comm.map.options.WiskyPolygonAnnotationOptions

/**
 * @date 2022/7/21.
 * @author xieyutuo
 * @description TODO
 */
class WiskyPolygonAnnotation(
    id: Long,
    options: WiskyPolygonAnnotationOptions,
) : WiskyAnnotation<WiskyPolygonAnnotationOptions>(id, options) {

    override fun getType(): WiskyAnnotationType {
        return WiskyAnnotationType.PolygonAnnotation
    }

}