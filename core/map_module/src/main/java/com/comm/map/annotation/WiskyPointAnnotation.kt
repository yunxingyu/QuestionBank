package com.comm.map.annotation

import com.comm.map.options.WiskyPointAnnotationOptions

/**
 * @date 2022/7/21.
 * @author xieyutuo
 * @description TODO
 */
class WiskyPointAnnotation(
    id: Long,
    options: WiskyPointAnnotationOptions,
) : WiskyAnnotation<WiskyPointAnnotationOptions>(id, options) {

    override fun getType(): WiskyAnnotationType {
        return WiskyAnnotationType.PointAnnotation
    }

}