package com.comm.map.listener

import com.comm.map.bean.WiskyLatLng

/**
 * @date 2022/8/17.
 * @author xieyutuo
 * @description 地图点击事件，非Annotation
 */
interface OnWiskyMapClickLister {

    fun onMapClick(latLng: WiskyLatLng): Boolean
}