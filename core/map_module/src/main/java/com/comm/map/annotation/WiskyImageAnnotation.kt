package com.comm.map.annotation

import com.comm.map.options.WiskyImageAnnotationOptions

/**
 * @date 2022/7/21.
 * @author xieyutuo
 * @description TODO
 */
class WiskyImageAnnotation(
    id: Long,
    options: WiskyImageAnnotationOptions,
) : WiskyAnnotation<WiskyImageAnnotationOptions>(id, options) {

    override fun getType(): WiskyAnnotationType {
        return WiskyAnnotationType.PointAnnotation
    }

}