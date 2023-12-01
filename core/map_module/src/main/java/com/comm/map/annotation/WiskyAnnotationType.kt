package com.comm.map.annotation

/**
 * @date 2022/7/20.
 * @author xieyutuo
 * @description TODO
 */
enum class WiskyAnnotationType(var value: Int) {

    /** PolygonAnnotation type */
    PolygonAnnotation(1),

    /** PolylineAnnotation type */
    PolylineAnnotation(2),

    /** PointAnnotation type */
    PointAnnotation(3),

    /** CircleAnnotation type */
    CircleAnnotation(4),

    /** ViewAnnotation type */
    ViewAnnotation(5),
}