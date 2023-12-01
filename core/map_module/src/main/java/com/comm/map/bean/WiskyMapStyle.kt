package com.comm.map.bean

import com.mapbox.maps.Style

/**
 * @date 2022/8/25.
 * @author xieyutuo
 * @description 地图样式
 */
enum class WiskyMapStyle(val value: Int) {

    /** 正常 */
    NORMAL(0),

    /** 混合 */
    MIX(1),

    /** 3D */
    THREE_DIMENSIONAL(2),

    /** 未知 */
    UNKNOWN(-1);

    companion object {
        fun findEnum(value: Int): WiskyMapStyle {
            val array = values()
            for (type in array) {
                if (type.value == value) {
                    return type
                }
            }
            return UNKNOWN
        }

        /**
         * 可以根据不同的要求，配置不同的Style路径，这里暂时先用mapbox的路径
         */
        fun getStyleUri(style: WiskyMapStyle): String {
            return when (style) {
                NORMAL -> {
                    Style.OUTDOORS
                }
                MIX -> {
                    Style.MAPBOX_STREETS
                }
                THREE_DIMENSIONAL -> {
                    Style.OUTDOORS
                }
                UNKNOWN -> {
                    Style.OUTDOORS
                }
            }
        }
    }

}