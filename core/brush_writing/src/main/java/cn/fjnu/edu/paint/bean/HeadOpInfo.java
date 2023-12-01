package cn.fjnu.edu.paint.bean;

/* loaded from: classes.dex */
public class HeadOpInfo {
    private int imgRes;
    private int title;
    private int type;
    private boolean isShow = true;
    private boolean isSelect = false;

    public HeadOpInfo(int i2, int i3, int i4) {
        this.imgRes = i2;
        this.type = i3;
        this.title = i4;
    }

    public int getImgRes() {
        return this.imgRes;
    }

    public int getTitle() {
        return this.title;
    }

    public int getType() {
        return this.type;
    }

    public boolean isSelect() {
        return this.isSelect;
    }

    public boolean isShow() {
        return this.isShow;
    }

    public void setImgRes(int i2) {
        this.imgRes = i2;
    }

    public void setSelect(boolean z) {
        this.isSelect = z;
    }

    public void setShow(boolean z) {
        this.isShow = z;
    }

    public void setTitle(int i2) {
        this.title = i2;
    }

    public void setType(int i2) {
        this.type = i2;
    }
}
