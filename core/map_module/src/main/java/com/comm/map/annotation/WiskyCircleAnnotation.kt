package com.comm.map.annotation

import com.comm.map.options.WiskyCircleAnnotationOptions

/**
 * @date 2022/7/21.
 * @author xieyutuo
 * @description TODO
 */
class WiskyCircleAnnotation(
    id: Long,
    options: WiskyCircleAnnotationOptions,
) : WiskyAnnotation<WiskyCircleAnnotationOptions>(id, options) {

    override fun getType(): WiskyAnnotationType {
        return WiskyAnnotationType.CircleAnnotation
    }

}