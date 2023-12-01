package com.comm.map.annotation

import com.comm.map.options.WiskyBaseOptions

/**
 * @date 2022/7/15.
 * @author xieyutuo
 * @description Base class for annotations
 */
abstract class WiskyAnnotation<T : WiskyBaseOptions>(
    /** The id for annotation */
    val id: Long,
    /** The options of annotation, saves all the property values of this annotation */
    val options: T,
) {

    /**
     * Get the type of this annotation
     */
    abstract fun getType(): WiskyAnnotationType

    /**
     * Unique feature identifier. May be useful to connect an annotation with view annotation.
     */
    val featureIdentifier = FEATURE_IDENTIFIER_PREFIX + id.toString()


    /**
     * Static variables and methods.
     */
    companion object {

        private const val FEATURE_IDENTIFIER_PREFIX = "Wisky_annotation_"
    }
}