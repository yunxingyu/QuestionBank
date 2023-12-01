package com.comm.map.bean

/**
 * Created by zrp 20230505
 * 地图层级定义：高中低三个层级；层级越高，优先在顶层显示
 */
enum class LayerPriority(i: Int) {

    HIGH(0),

    MIDDLE(1),

    LOW(2)
}