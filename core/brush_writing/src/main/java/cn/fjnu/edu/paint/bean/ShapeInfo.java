package cn.fjnu.edu.paint.bean;

/* loaded from: classes.dex */
public class ShapeInfo {
    private int imgRes;
    private boolean isNeedVIP;
    private boolean isSelect;
    private int shapeType;

    public ShapeInfo(int i2, int i3) {
        this(i2, i3, false);
    }

    public int getImgRes() {
        return this.imgRes;
    }

    public int getShapeType() {
        return this.shapeType;
    }

    public boolean isNeedVIP() {
        return this.isNeedVIP;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public void setImgRes(int i2) {
        this.imgRes = i2;
    }

    public void setNeedVIP(boolean z) {
        this.isNeedVIP = z;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }

    public void setShapeType(int i2) {
        this.shapeType = i2;
    }

    public ShapeInfo(int i2, int i3, boolean z) {
        this(i2, i3, z, false);
    }

    public ShapeInfo(int i2, int i3, boolean z, boolean z2) {
        this.shapeType = i3;
        this.imgRes = i2;
        this.isSelect = z;
        this.isNeedVIP = z2;
    }
}
