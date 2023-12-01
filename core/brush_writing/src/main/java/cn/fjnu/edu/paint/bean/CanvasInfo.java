package cn.fjnu.edu.paint.bean;

import java.io.Serializable;

/* loaded from: classes.dex */
public class CanvasInfo implements Serializable {
    private int backgroundColor;
    private byte[] backgroundData;
    private int backgroundHeight;
    private int backgroundType;
    private int backgroundWith;
    private int canvasHeight;
    private int canvasWidth;

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public byte[] getBackgroundData() {
        return this.backgroundData;
    }

    public int getBackgroundHeight() {
        return this.backgroundHeight;
    }

    public int getBackgroundType() {
        return this.backgroundType;
    }

    public int getBackgroundWith() {
        return this.backgroundWith;
    }

    public int getCanvasHeight() {
        return this.canvasHeight;
    }

    public int getCanvasWidth() {
        return this.canvasWidth;
    }

    public void setBackgroundColor(int i2) {
        this.backgroundColor = i2;
    }

    public void setBackgroundData(byte[] bArr) {
        this.backgroundData = bArr;
    }

    public void setBackgroundHeight(int i2) {
        this.backgroundHeight = i2;
    }

    public void setBackgroundType(int i2) {
        this.backgroundType = i2;
    }

    public void setBackgroundWith(int i2) {
        this.backgroundWith = i2;
    }

    public void setCanvasHeight(int i2) {
        this.canvasHeight = i2;
    }

    public void setCanvasWidth(int i2) {
        this.canvasWidth = i2;
    }
}
