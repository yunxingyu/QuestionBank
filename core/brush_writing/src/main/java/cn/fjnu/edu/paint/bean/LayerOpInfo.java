package cn.fjnu.edu.paint.bean;

/* loaded from: classes.dex */
public class LayerOpInfo {
    private int iconRes;
    private int opDes;
    private int opType;

    public LayerOpInfo(int i2, int i3, int i4) {
        this.opType = i2;
        this.iconRes = i3;
        this.opDes = i4;
    }

    public int getIconRes() {
        return this.iconRes;
    }

    public int getOpDes() {
        return this.opDes;
    }

    public int getOpType() {
        return this.opType;
    }

    public void setIconRes(int i2) {
        this.iconRes = i2;
    }

    public void setOpDes(int i2) {
        this.opDes = i2;
    }

    public void setOpType(int i2) {
        this.opType = i2;
    }
}
