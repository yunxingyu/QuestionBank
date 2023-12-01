package cn.fjnu.edu.paint.bean;

import java.io.Serializable;

/* loaded from: classes.dex */
public class ColorSelectInfo implements Serializable {
    private int commonColor;
    private int pickerColor;
    private int recentColor;
    private int resultColor;
    private int rgbColor;
    private int selectType;

    public ColorSelectInfo(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.selectType = i2;
        this.commonColor = i3;
        this.pickerColor = i4;
        this.rgbColor = i5;
        this.recentColor = i6;
        this.resultColor = i7;
    }

    public int getCommonColor() {
        return this.commonColor;
    }

    public int getPickerColor() {
        return this.pickerColor;
    }

    public int getRecentColor() {
        return this.recentColor;
    }

    public int getResultColor() {
        return this.resultColor;
    }

    public int getRgbColor() {
        return this.rgbColor;
    }

    public int getSelectType() {
        return this.selectType;
    }

    public void setCommonColor(int i2) {
        this.commonColor = i2;
    }

    public void setPickerColor(int i2) {
        this.pickerColor = i2;
    }

    public void setRecentColor(int i2) {
        this.recentColor = i2;
    }

    public void setResultColor(int i2) {
        this.resultColor = i2;
    }

    public void setRgbColor(int i2) {
        this.rgbColor = i2;
    }

    public void setSelectType(int i2) {
        this.selectType = i2;
    }
}
