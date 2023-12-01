package com.comm.map.options

import android.text.TextUtils
import com.comm.map.bean.WiskyLatLng
import com.comm.map.bean.LayerPriority
import java.util.concurrent.atomic.AtomicLong

/**
 * @date 2022/7/16.
 * @author xieyutuo
 * @description Options class for building annotations
 */
abstract class WiskyBaseOptions {

    private val id = ID_GENERATOR.incrementAndGet()

    /*经纬度*/
    val latLng: WiskyLatLng = WiskyLatLng()

    /*所处层级 默认在中间层显示*/
    var layerPriority = LayerPriority.MIDDLE

    /*是否可以拖动*/
    var isDraggable = false

    /**
     * 是否可以触摸
     */
    var isTouchable = true

    /*是否可以选中*/
    var isSelected = false

    /*对应地图上层级id*/
    var mLayerId: String? = null

    /*对应地图上的源id*/
    var mSourceId: String? = null

    var belowLayerId: String? = null

    var aboveLayerId: String? = null

    //透明度
    var iconOpacity: Double = 1.0

    /**
     * 是否显示
     */
    var visibility: Boolean = true

    /*用户自定义标记*/
    var tag: String? = null

    fun getLayerId(): String {
        if (mLayerId == null) {
            mLayerId = DEFAULT_LAYER_ID + id
        }
        return mLayerId!!
    }

    fun getSourceId(): String {
        if (mSourceId == null) {
            mSourceId = DEFAULT_SOURCE_ID + id
        }
        return mSourceId!!
    }

    companion object {
        /**The generator for id*/
        var ID_GENERATOR = AtomicLong(0)

        /**The default layer id*/
        const val DEFAULT_LAYER_ID = "Wisky-annotation-layer-"

        /**The default source id*/
        const val DEFAULT_SOURCE_ID = "Wisky-annotation-source-"
    }

    override fun equals(other: Any?): Boolean {
        return if (other is WiskyBaseOptions) {
            (mSourceId != null && other.mSourceId != null && TextUtils.equals(
                other.mSourceId,
                mSourceId
            )) || (id == other.id)
        } else {
            false
        }

    }

    override fun toString(): String {
        if (mSourceId != null) {
            return mSourceId.toString()

        } else
            return "WiskyBaseOptions{id:$id,mLayerId:$mLayerId,latLng:$latLng}"
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + latLng.hashCode()
        result = 31 * result + isDraggable.hashCode()
        result = 31 * result + isSelected.hashCode()
        result = 31 * result + (mLayerId?.hashCode() ?: 0)
        result = 31 * result + (mSourceId?.hashCode() ?: 0)
        result = 31 * result + (belowLayerId?.hashCode() ?: 0)
        result = 31 * result + (aboveLayerId?.hashCode() ?: 0)
        result = 31 * result + (tag?.hashCode() ?: 0)
        return result
    }


}