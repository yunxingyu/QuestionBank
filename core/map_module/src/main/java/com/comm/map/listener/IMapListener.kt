package com.comm.map.listener

import com.comm.map.annotation.WiskyAnnotation

/**
 * @date 2022/7/18.
 * @author xieyutuo
 * @description 地图交互的事件接口
 */
interface IMapListener {

    /** 添加地图的点击事件 */
    fun addMapClickLister(listener: OnWiskyMapClickLister)

    /** 添加地图的点击事件 */
    fun removeMapClickLister(listener: OnWiskyMapClickLister)

    /** 添加标记点的点击事件 */
    fun addAnnotationClickLister(listener: OnWiskyAnnotationClickListener<WiskyAnnotation<*>>)

    /** 移除标记点的点击事件 */
    fun removeAnnotationClickLister(listener: OnWiskyAnnotationClickListener<WiskyAnnotation<*>>)

    /** 添加标记点的选中事件 */
    fun addAnnotationInteractionLister(listener: OnWiskyAnnotationInteractionListener<WiskyAnnotation<*>>)

    /**移除标记点的点击事件 */
    fun removeAnnotationInteractionLister(listener: OnWiskyAnnotationInteractionListener<WiskyAnnotation<*>>)

    /**添加标记点的长按事件 */
    fun addAnnotationLongClickListener(listener: OnWiskyAnnotationLongClickListener<WiskyAnnotation<*>>)

    /**移除标记点的点击事件 */
    fun removeAnnotationLongClickListener(listener: OnWiskyAnnotationLongClickListener<WiskyAnnotation<*>>)

    /**添加标记点的拖动事件 */
    fun addAnnotationDragListener(listener: OnWiskyAnnotationDragLister<WiskyAnnotation<*>>)

    /** 移除标记点的点击事件 */
    fun removeAnnotationDragListener(listener: OnWiskyAnnotationDragLister<WiskyAnnotation<*>>)

    /** 添加缩放监听 */
    fun addScaleListener(listener: OnWiskyScaleListener)

    /** 移除缩放监听 */
    fun removeScaleListener(listener: OnWiskyScaleListener)

    /** 添加用户位置变化监听 */
    fun addPositionChangeListener(listener: OnWiskyPositionChangedListener)

    /** 移除用户位置变化监听 */
    fun removePositionChangeListener(listener: OnWiskyPositionChangedListener)

    fun addOnCameraMoveListener(listener: WiskyCameraMoveListener)

    fun removeCameraMoveListener(listener: WiskyCameraMoveListener)

    fun addOnCameraChangeListener(listener: OnWiskyCameraChangeListener)

    fun removeCameraChangeListener(listener: OnWiskyCameraChangeListener)
}