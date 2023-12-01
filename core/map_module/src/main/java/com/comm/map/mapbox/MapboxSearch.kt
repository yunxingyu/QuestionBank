package com.comm.map.mapbox

import com.mapbox.search.MapboxSearchSdk
import com.mapbox.search.SearchEngine
import com.mapbox.search.SearchEngineSettings


/**
 * Created by xulc 2022/9/27
 */
object MapboxSearch {
    private var searchEngine: SearchEngine? = null

    fun getSearchEngine(): SearchEngine {
        if (searchEngine == null) {
            searchEngine = MapboxSearchSdk.createSearchEngine(SearchEngineSettings("sk.eyJ1IjoiYXV0ZWxyb2JvdGljcyIsImEiOiJja2l6c2g4NmY0Zzk3MzJxajVnaWN5MTY2In0.E9CUnxD1MXiFZuhB77GK6A"))
        }
        return searchEngine!!
    }


}