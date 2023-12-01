package cn.fjnu.edu.paint.bean;

/* loaded from: classes.dex */
public class ExtendDrawInfo {
    private String imgPath;
    private boolean isFullImg;
    private int lineType = 1;
    private int penType = 2;

    public String getImgPath() {
        return this.imgPath;
    }

    public int getLineType() {
        return this.lineType;
    }

    public int getPenType() {
        return this.penType;
    }

    public boolean isFullImg() {
        return this.isFullImg;
    }

    public void setFullImg(boolean z) {
        this.isFullImg = z;
    }

    public void setImgPath(String str) {
        this.imgPath = str;
    }

    public void setLineType(int i2) {
        this.lineType = i2;
    }

    public void setPenType(int i2) {
        this.penType = i2;
    }
}
