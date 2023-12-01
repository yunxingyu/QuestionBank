package com.comm.map.options;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author create by LJ
 * @Date 2022/11/18 13
 */


public class OfflineMapOptions {

    public String tilejson = "3.0.0";
    public String name = "OpenStreetMap";
    public String version = "3.0.0";
    public String scheme = "xyz";
    public List<String> tiles = new ArrayList<>();
    public Integer minzoom = 0;
    public Integer maxzoom = 22;
    public List<Integer> bounds;
    public Integer fillzoom = 0;
    public String glyphs = "asset://mapfonts/Open Sans Bold,Arial Unicode MS Bold/{range}.pbf";


    @Override
    public String toString() {
        return "{ \n" +
                "\"tilejson\":\"" + tilejson + '\"' +
                ", \"name\":\"" + name + '\"' +
                ",\" version\":\"" + version + '\"' +
                ", \"scheme\":\"" + scheme + '\"' +
                ", \"tiles\":" + tiles +
                ", \"minzoom\":" + minzoom +
                ", \"maxzoom\":" + maxzoom +
                ", \"bounds\":" + bounds +
                ", \"fillzoom\":" + fillzoom +
                ", \"glyphs\":" + glyphs +
                '}';
    }
}
