package com.comm.map.listener

/**
 * @date 2022/7/30.
 * @author xieyutuo
 * @description Definition for listener invoked whenever the Map has entered the idle state.
 */
fun interface WiskyCameraIdleListener {
    /**
     *地图处于空闲状态，没有绘制任务（1s内），默认会执行一次
     */
    fun onCameraIdle()
}