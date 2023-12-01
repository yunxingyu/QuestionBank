package cn.fjnu.edu.paint.bean;

import java.io.Serializable;

/* loaded from: classes.dex */
public class GuideInfo implements Serializable {
    private int imgRes;
    private boolean isShowEnterMainBtn;
    private int titleRes;

    public GuideInfo(int i2, int i3, boolean z) {
        this.titleRes = i2;
        this.imgRes = i3;
        this.isShowEnterMainBtn = z;
    }

    public int getImgRes() {
        return this.imgRes;
    }

    public int getTitleRes() {
        return this.titleRes;
    }

    public boolean isShowEnterMainBtn() {
        return this.isShowEnterMainBtn;
    }

    public void setImgRes(int i2) {
        this.imgRes = i2;
    }

    public void setShowEnterMainBtn(boolean z) {
        this.isShowEnterMainBtn = z;
    }

    public void setTitleRes(int i2) {
        this.titleRes = i2;
    }
}
