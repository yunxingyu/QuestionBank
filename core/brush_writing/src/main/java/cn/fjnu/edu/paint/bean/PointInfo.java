package cn.fjnu.edu.paint.bean;

import java.io.Serializable;
import java.util.Arrays;

/* loaded from: classes.dex */
public class PointInfo implements Serializable {
    private float x;
    private float y;

    public PointInfo() {
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PointInfo pointInfo = (PointInfo) obj;
        if (Float.compare(pointInfo.x, this.x) == 0 && Float.compare(pointInfo.y, this.y) == 0) {
            return true;
        }
        return false;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return Arrays.hashCode(new Object[]{Float.valueOf(this.x), Float.valueOf(this.y)});
    }

    public void setX(float f2) {
        this.x = f2;
    }

    public void setY(float f2) {
        this.y = f2;
    }

    @Override // java.lang.Object
    public String toString() {
        return "PointInfo{x=" + this.x + ", y=" + this.y + '}';
    }

    public PointInfo(float f2, float f3) {
        this.x = f2;
        this.y = f3;
    }
}
