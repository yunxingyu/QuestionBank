package com.comm.map.listener

import com.comm.map.annotation.WiskyAnnotation

/**
 * @date 2022/7/25.
 * @author xieyutuo
 * @description TODO
 */
interface OnWiskyAnnotationLongClickListener<T : WiskyAnnotation<*>> {

    /**
     * Generic fun interface definition of a callback to be invoked when an annotation has been long clicked.
     *
     * @param <T> generic parameter extending from Annotation
     */
    fun onAnnotationLongClick(annotation: T): Boolean

}