package com.comm.map.listener

import com.comm.map.annotation.WiskyAnnotation

/**
 * @date 2022/7/22.
 * @author xieyutuo
 * @description TODO
 */
interface OnWiskyAnnotationDragLister<out T : WiskyAnnotation<*>> {


    /**
     * Called when an annotation dragging has started.
     *
     * @param annotation the annotation
     */
    fun onAnnotationDragStarted(annotation: WiskyAnnotation<*>)

    /**
     * Called when an annotation dragging is in progress.
     *
     * @param annotation the annotation
     */
    fun onAnnotationDrag(annotation: WiskyAnnotation<*>)

    /**
     * Called when an annotation dragging has finished.
     *
     * @param annotation the annotation
     */
    fun onAnnotationDragFinished(annotation: WiskyAnnotation<*>)
}