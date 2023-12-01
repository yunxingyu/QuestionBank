package cn.fjnu.edu.paint.bean;

import java.io.Serializable;

/* loaded from: classes.dex */
public class CustomGradientInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private int angleType;
    private ColorSelectInfo endColorInfo;
    private ColorSelectInfo startColorInfo;

    public int getAngleType() {
        return this.angleType;
    }

    public ColorSelectInfo getEndColorInfo() {
        return this.endColorInfo;
    }

    public ColorSelectInfo getStartColorInfo() {
        return this.startColorInfo;
    }

    public void setAngleType(int i2) {
        this.angleType = i2;
    }

    public void setEndColorInfo(ColorSelectInfo colorSelectInfo) {
        this.endColorInfo = colorSelectInfo;
    }

    public void setStartColorInfo(ColorSelectInfo colorSelectInfo) {
        this.startColorInfo = colorSelectInfo;
    }
}
