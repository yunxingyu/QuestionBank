package com.comm.map.util;

import java.util.ArrayDeque;

/**
 * Created by A15387 on 2016/12/7.
 */
@SuppressWarnings("DefaultFileTemplate")
public class AngleLowpassFilter {

    private final int LENGTH = 10;
    private float sumSin , sumCos;
    private final ArrayDeque<Float> queue = new ArrayDeque<>();
    public void add(float radians){
        sumSin += (float)Math.sin(radians);
        sumCos += (float)Math.cos(radians);
        queue.add(radians);
        if(queue.size() > LENGTH){
            float old = queue.poll();
            sumSin -= Math.sin(old);
            sumCos -= Math.cos(old);
        }
    }

    public float average(){
        int size = queue.size();
        return (float)Math.atan2(sumSin/size,sumCos/size);
    }
}
