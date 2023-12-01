package com.comm.map.listener

import com.comm.map.annotation.WiskyAnnotation

/**
 * @date 2022/7/18.
 * @author xieyutuo
 * @description TODO
 */
interface OnWiskyAnnotationClickListener<T : WiskyAnnotation<*>> {

    /**
     * Called when an annotation has been clicked
     *
     * @param annotation the annotation clicked.
     * @return True if this click should be consumed and not passed further to other listeners
     * registered afterwards, false otherwise.
     */
    fun onAnnotationClick(annotation: T): Boolean
}