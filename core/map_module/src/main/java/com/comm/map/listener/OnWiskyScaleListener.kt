package com.comm.map.listener

import com.comm.map.bean.WiskyScaleGesture

/**
 * @date 2022/7/18.
 * @author xieyutuo
 * @description 地图缩放监听
 */
interface OnWiskyScaleListener {

    /**
     * Called when the scale gesture is starting.
     */
    fun onScaleBegin(detector: WiskyScaleGesture)

    /**
     * Called when the scale gesture is executing.
     */
    fun onScale(detector: WiskyScaleGesture)

    /**
     * called when the scale gesture has ended.
     */
    fun onScaleEnd(detector: WiskyScaleGesture)
}