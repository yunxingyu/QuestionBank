package cn.fjnu.edu.paint.bean;

/* loaded from: classes.dex */
public class GradientAngleInfo {
    private String angleDes;
    private int angleType;
    private boolean isSelected;

    public GradientAngleInfo(String str, int i2, boolean z) {
        this.angleDes = str;
        this.angleType = i2;
        this.isSelected = z;
    }

    public String getAngleDes() {
        return this.angleDes;
    }

    public int getAngleType() {
        return this.angleType;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setAngleDes(String str) {
        this.angleDes = str;
    }

    public void setAngleType(int i2) {
        this.angleType = i2;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
