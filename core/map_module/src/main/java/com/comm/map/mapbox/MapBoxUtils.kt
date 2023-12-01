package com.comm.map.mapbox

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.text.TextUtils
import com.Wisky.log.WiskyLog
import com.comm.map.bean.WiskyLatLng
import com.mapbox.geojson.Point
import com.mapbox.search.*
import com.mapbox.search.result.SearchResult
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.*

/**
 * @date 2022/7/27.
 * @author xieyutuo
 * @description MapBox工具类
 */
object MapBoxUtils {

    private val TAG: String = MapBoxUtils::class.java.simpleName

    /** 默认生成圆形的坐标点数*/
    private const val DEFAULT_NUMBER = 50

    /** 地球半径，单位m*/
    private const val EARTH_RADIUS = 6378137.0

    @JvmStatic
    fun generateCirclePolyline(
        centerPosition: WiskyLatLng,
        radiusInKilometers: Double,
        numberOfSides: Int = DEFAULT_NUMBER,
    ): List<WiskyLatLng> {
        val positions = mutableListOf<WiskyLatLng>()
        val distanceX: Double =
            radiusInKilometers / (111.319 * cos(centerPosition.latitude * Math.PI / 180))
        val distanceY: Double = radiusInKilometers / 110.574
        val slice = 2 * Math.PI / numberOfSides
        var theta: Double
        var x: Double
        var y: Double
        var position: WiskyLatLng?
        for (i in 0 until numberOfSides) {
            theta = i * slice
            x = distanceX * cos(theta)
            y = distanceY * sin(theta)
            position = WiskyLatLng(
                centerPosition.latitude + y,
                centerPosition.longitude + x
            )
            positions.add(position)
        }
        //闭合
        positions.add(positions[0])
        return positions
    }

    //获取两个点之间距离的中点
    fun midPoint(point1: WiskyLatLng, point2: WiskyLatLng): WiskyLatLng {
        val dLon = Math.toRadians(point2.longitude - point1.longitude)
        val latitude1 = Math.toRadians(point1.latitude)
        val latitude2 = Math.toRadians(point2.latitude)
        val longitude1 = Math.toRadians(point1.longitude)
        val bx = cos(latitude2) * cos(dLon)
        val by = cos(latitude2) * sin(dLon)
        val latitude3 = atan2(
            sin(latitude1) + sin(latitude2),
            sqrt((cos(latitude1) + bx) * (cos(latitude1) + bx) + by * by)
        )
        val longitude3 = longitude1 + atan2(by, cos(latitude1) + bx)
        return WiskyLatLng(Math.toDegrees(latitude3), Math.toDegrees(longitude3))
    }

    //获取两个点之间的距离
    fun getDistance(point1: WiskyLatLng, point2: WiskyLatLng): Double {
        val latitude1 = Math.toRadians(point1.latitude)
        val latitude2 = Math.toRadians(point2.latitude)
        val longitude1 = Math.toRadians(point1.longitude)
        val longitude2 = Math.toRadians(point2.longitude)
        val a = latitude1 - latitude2
        val b = longitude1 - longitude2
        //计算两点间距离的公式
        var s = 2 * asin(sqrt(sin(a / 2).pow(2.0) + cos(latitude1) * cos(latitude2) * sin(b / 2).pow(2.0)))
        s *= EARTH_RADIUS
        return s
    }

    /**使用mapbox SDK获取当前经纬度所在的国家***/
    fun fetchCountryByLatLng(context: Context, accessToken: String, latitude: Double, longitude: Double): String {
        var country = ""

        val countryList = mutableListOf<Address>()
        val geocoder = Geocoder(context.applicationContext)
        try {
            countryList.addAll(geocoder.getFromLocation(latitude, longitude, 1))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (countryList.size > 0) {
            country = countryList[0].countryCode
            /***
             * !Character.isDigit(country.charAt(0))   适配部分机型使用国标码标记
             */
            if (!TextUtils.isEmpty(country) && !Character.isDigit(country[0])) {
                return country
            }
        }
        return country
    }

    /**
     * 通过坐标获取地址 -  mapbox方式
     *
     */
    fun geoSearch(context: Context, lat: Double, lng: Double): String {
        val geocoder = Geocoder(context)
        val addresss = geocoder.getFromLocation(lat, lng, 10)
        if (addresss.isNullOrEmpty()) return ""
        var country = ""
        var area = ""
        var subArea = ""
        var locality = ""
        var subLocality = ""
        addresss.forEach { address ->
            if (!TextUtils.isEmpty(address.countryName)) country = address.countryName
            if (!TextUtils.isEmpty(address.adminArea)) area = address.adminArea
            if (!TextUtils.isEmpty(address.subAdminArea)) subArea = address.subAdminArea
            if (!TextUtils.isEmpty(address.locality)) locality = address.locality
            if (!TextUtils.isEmpty(address.subLocality)) subLocality = address.subLocality
        }
        if (country.isNotEmpty()) return country
        if (area.isNotEmpty()) return area
        if (subArea.isNotEmpty()) return subArea
        if (locality.isNotEmpty()) return locality
        if (subLocality.isNotEmpty()) return subLocality
        return ""
    }

    /**
     * 查询对应坐标国家代码
     * 当调用系统查询失败时,会调用mapbox的方式查询
     * @return ISO 3166 地理代码
     */
    fun getCountryCodeByLatLng(context: Context, latitude: Double, longitude: Double, countryCodeListener: ICountryCodeByLatLng) {
        val geocoder = Geocoder(context)
        val address = geocoder.getFromLocation(latitude, longitude, 1)
        if (!address.isNullOrEmpty()) {
            val country = address[0].countryCode.uppercase()
            WiskyLog.d(TAG, "get country from system geocoder . $country")
            countryCodeListener.onSuccess(country)
        } else {
            getCountryCodeByMapbox(latitude, longitude, countryCodeListener)
        }
    }

    interface ICountryCodeByLatLng {
        fun onSuccess(countryCode: String)

        fun onFail()
    }

    /**
     * 通过调用mapbox方式查询坐标点的国家地理代码 ISO 3166
     * @return ISO 3166 地理代码
     */
    fun getCountryCodeByMapbox(latitude: Double, longitude: Double, countryCodeListener: ICountryCodeByLatLng) {
        val options = ReverseGeoOptions.Builder(Point.fromLngLat(longitude, latitude))
            .types(listOf(QueryType.POSTCODE)) // 国家代码要通过这个去查找
            .languages(Language.ENGLISH)
            .build()
        val searchEngine = MapboxSearch.getSearchEngine()
        WiskyLog.d(TAG, "get country from mapbox search engine.")
        searchEngine.search(options, object : SearchCallback {
            override fun onError(e: Exception) {
                countryCodeListener.onFail()
            }

            override fun onResults(results: List<SearchResult>, responseInfo: ResponseInfo) {
                try {
                    if (results.isNullOrEmpty()) {
                        countryCodeListener.onFail()
                    } else {
                        // 国家代码要通过这个去查找
                        val country = results[0].metadata?.extraData?.get("iso_3166_1") // 这里还有iso_3166_2
                        if (!country.isNullOrEmpty()) {
                            countryCodeListener.onSuccess(country.uppercase())
                        } else {
                            countryCodeListener.onFail()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }


    /**
     * 通过调用mapbox方式查询地址（将坐标转化为地址）
     */
    suspend fun getAddressByMapbox(latitude: Double, longitude: Double, isChina: Boolean) = suspendCoroutine {
        val options = ReverseGeoOptions.Builder(Point.fromLngLat(longitude, latitude))
            .types(listOf(QueryType.LOCALITY))
            .languages(if (isChina) Language.CHINESE else Language.ENGLISH)
            .build()
        val searchEngine = MapboxSearch.getSearchEngine()
        WiskyLog.d(TAG, "getAddressByMapbox from mapbox search engine.")
        try {
            searchEngine.search(options, object : SearchCallback {
                override fun onError(e: Exception) {
                    it.resumeWithException(e)
                    WiskyLog.d(TAG, "onError not search address.")
                }

                override fun onResults(results: List<SearchResult>, responseInfo: ResponseInfo) {
                    try {
                        if (results.isNullOrEmpty()) {
                            WiskyLog.d(TAG, "not search address.")
                            it.resume("")
                        } else {
                            val address = geoSearch(results[0])
                            if (!address.isNullOrEmpty()) {
                                it.resume(address)
                            } else {
                                WiskyLog.d(TAG, "get address error")
                                it.resume("")
                            }
                        }
                    } catch (e: Exception) {
                        WiskyLog.e(TAG, e.toString())
                        e.printStackTrace()
                    }
                }
            })
        } catch (e: Exception) {
            WiskyLog.e(TAG, e.toString())
            e.printStackTrace()
        }

    }


    /**
     * 查询地理位置
     */
    fun geoSearch(result: SearchResult): String {
        var address = ""
        var country = ""
        var region = ""
        var place = ""
        var street = ""
        var name = ""

        result.name.let {
            name = it
            address += "$name "
        }
        result.address?.street?.let {
            street = it
            address += "$street "
        }

        result.address?.place?.let {
            place = it
            address += "$place "
        }

        result.address?.region?.let {
            region = it
            address += "$region "
        }
        return address
    }

    fun containsLocation(point: WiskyLatLng, polygon: List<WiskyLatLng>, geodesic: Boolean): Boolean {
        return containsLocation(point.latitude, point.longitude, polygon, geodesic)
    }

    /**
     * 计算指定点是否在范围内
     */
    private fun containsLocation(latitude: Double, longitude: Double, polygon: List<WiskyLatLng>, geodesic: Boolean): Boolean {
        val size = polygon.size
        if (size == 0) {
            return false
        }
        val lat3 = Math.toRadians(latitude)
        val lng3 = Math.toRadians(longitude)
        val prev: WiskyLatLng = polygon[size - 1]
        var lat1 = Math.toRadians(prev.latitude)
        var lng1 = Math.toRadians(prev.longitude)
        var nIntersect = 0
        for (point2 in polygon) {
            val dLng3: Double = wrap(lng3 - lng1, -Math.PI, Math.PI)
            // Special case: point equal to vertex is inside.
            if (lat3 == lat1 && dLng3 == 0.0) {
                return true
            }
            val lat2 = Math.toRadians(point2.latitude)
            val lng2 = Math.toRadians(point2.longitude)
            // Offset longitudes by -lng1.
            if (intersects(lat1, lat2, wrap(lng2 - lng1, -Math.PI, Math.PI), lat3, dLng3, geodesic)) {
                ++nIntersect
            }
            lat1 = lat2
            lng1 = lng2
        }
        return nIntersect and 1 != 0
    }

    /**
     * Returns tan(latitude-at-lng3) on the great circle (lat1, lng1) to (lat2, lng2). lng1==0.
     * See http://williams.best.vwh.net/avform.htm .
     */
    private fun tanLatGC(lat1: Double, lat2: Double, lng2: Double, lng3: Double): Double {
        return (tan(lat1) * sin(lng2 - lng3) + tan(lat2) * sin(lng3)) / sin(lng2)
    }

    /**
     * Returns mercator(latitude-at-lng3) on the Rhumb line (lat1, lng1) to (lat2, lng2). lng1==0.
     */
    private fun mercatorLatRhumb(lat1: Double, lat2: Double, lng2: Double, lng3: Double): Double {
        return (mercator(lat1) * (lng2 - lng3) + mercator(lat2) * lng3) / lng2
    }

    /**
     * Computes whether the vertical segment (lat3, lng3) to South Pole intersects the segment
     * (lat1, lng1) to (lat2, lng2).
     * Longitudes are offset by -lng1; the implicit lng1 becomes 0.
     */
    private fun intersects(lat1: Double, lat2: Double, lng2: Double, lat3: Double, lng3: Double, geodesic: Boolean): Boolean {
        // Both ends on the same side of lng3.
        if (lng3 >= 0 && lng3 >= lng2 || lng3 < 0 && lng3 < lng2) {
            return false
        }
        // Point is South Pole.
        if (lat3 <= -Math.PI / 2) {
            return false
        }
        // Any segment end is a pole.
        if (lat1 <= -Math.PI / 2 || lat2 <= -Math.PI / 2 || lat1 >= Math.PI / 2 || lat2 >= Math.PI / 2) {
            return false
        }
        if (lng2 <= -Math.PI) {
            return false
        }
        val linearLat = (lat1 * (lng2 - lng3) + lat2 * lng3) / lng2
        // Northern hemisphere and point under lat-lng line.
        if (lat1 >= 0 && lat2 >= 0 && lat3 < linearLat) {
            return false
        }
        // Southern hemisphere and point above lat-lng line.
        if (lat1 <= 0 && lat2 <= 0 && lat3 >= linearLat) {
            return true
        }
        // North Pole.
        if (lat3 >= Math.PI / 2) {
            return true
        }
        // Compare lat3 with latitude on the GC/Rhumb segment corresponding to lng3.
        // Compare through a strictly-increasing function (tan() or mercator()) as convenient.
        return if (geodesic) tan(lat3) >= tanLatGC(
            lat1,
            lat2,
            lng2,
            lng3
        ) else mercator(lat3) >= mercatorLatRhumb(lat1, lat2, lng2, lng3)
    }

    /**
     * Returns mercator Y corresponding to latitude.
     * See http://en.wikipedia.org/wiki/Mercator_projection .
     */
    private fun mercator(lat: Double): Double {
        return ln(tan(lat * 0.5 + Math.PI / 4))
    }

    /**
     * Wraps the given value into the inclusive-exclusive interval between min and max.
     * @param n   The value to wrap.
     * @param min The minimum.
     * @param max The maximum.
     */
    private fun wrap(n: Double, min: Double, max: Double): Double {
        return if (n >= min && n < max) n else mod(n - min, max - min) + min
    }

    /**
     * Returns the non-negative remainder of x / m.
     * @param x The operand.
     * @param m The modulus.
     */
    private fun mod(x: Double, m: Double): Double {
        return (x % m + m) % m
    }
}