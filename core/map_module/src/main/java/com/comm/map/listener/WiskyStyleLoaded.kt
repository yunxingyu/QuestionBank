package com.comm.map.listener

/**
 * @date 2022/7/30.
 * @author xieyutuo
 * @description
 */

/**
 * Callback to be invoked when a style has finished loading.
 */
fun interface WiskyStyleLoaded {

    /**
     * Invoked when a style has finished loading.
     */
    fun onStyleLoaded(isSuc: Boolean, message: String)

}

