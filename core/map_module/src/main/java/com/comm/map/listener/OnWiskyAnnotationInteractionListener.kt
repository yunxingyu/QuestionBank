package com.comm.map.listener

import com.comm.map.annotation.WiskyAnnotation

/**
 * @date 2022/7/25.
 * @author xieyutuo
 * @description TODO
 */
interface OnWiskyAnnotationInteractionListener<T : WiskyAnnotation<*>> {

    /**
     * Called when an annotation has been selected
     *
     * @param annotation the annotation selected.
     */
    fun onSelectAnnotation(annotation: T)

    /**
     * Called when an annotation has been deselected
     *
     * @param annotation the annotation deselected.
     */
    fun onDeselectAnnotation(annotation: T)

}