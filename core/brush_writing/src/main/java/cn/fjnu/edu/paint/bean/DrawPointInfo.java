package cn.fjnu.edu.paint.bean;

import java.io.Serializable;

/* loaded from: classes.dex */
public class DrawPointInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private String extraInfo;
    private float pointX;
    private float pointY;
    private float pressure;

    public DrawPointInfo() {
    }

    public String getExtraInfo() {
        return this.extraInfo;
    }

    public float getPointX() {
        return this.pointX;
    }

    public float getPointY() {
        return this.pointY;
    }

    public float getPressure() {
        return this.pressure;
    }

    public void setExtraInfo(String str) {
        this.extraInfo = str;
    }

    public void setPointX(float f2) {
        this.pointX = f2;
    }

    public void setPointY(float f2) {
        this.pointY = f2;
    }

    public void setPressure(float f2) {
        this.pressure = f2;
    }

    public DrawPointInfo(float f2, float f3, float f4) {
        this.pointX = f2;
        this.pointY = f3;
        this.pressure = f4;
    }
}
