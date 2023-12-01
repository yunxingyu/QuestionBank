package com.comm.map.annotation

import com.comm.map.options.WiskyViewAnnotationOptions

/**
 * @date 2022/7/21.
 * @author xieyutuo
 * @description TODO
 */
class WiskyViewAnnotation(
    id: Long,
    options: WiskyViewAnnotationOptions,
) : WiskyAnnotation<WiskyViewAnnotationOptions>(id, options) {

    override fun getType(): WiskyAnnotationType {
        return WiskyAnnotationType.ViewAnnotation
    }

}