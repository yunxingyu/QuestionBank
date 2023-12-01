package cn.fjnu.edu.paint.bean;

import android.graphics.Bitmap;

import org.xutils.x;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.flynormal.baselib.utils.PackageUtils;

/* loaded from: classes.dex */
public class LayerInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private transient Bitmap bitmap;
    private transient List<DrawInfo> cancelDrawInfos;
    private transient List<DrawInfo> fullImageDrawInfos;
    private boolean isHide;
    private boolean isSelected;
    private int lastFullImgIndex;
    private String layerPath;
    private List<DrawInfo> saveDrawInfos;
    private int layerAlpha = 255;
    private int versionCode = PackageUtils.a(x.a());

    public LayerInfo(boolean z, Bitmap bitmap, List<DrawInfo> list, List<DrawInfo> list2, List<DrawInfo> list3, int i2, boolean z2) {
        this.isHide = z;
        this.bitmap = bitmap;
        this.saveDrawInfos = list;
        this.cancelDrawInfos = list2;
        this.fullImageDrawInfos = list3;
        this.lastFullImgIndex = i2;
        this.isSelected = z2;
    }

    public void addDrawInfo(DrawInfo drawInfo) {
        if (this.saveDrawInfos == null) {
            this.saveDrawInfos = new ArrayList();
        }
        this.saveDrawInfos.add(drawInfo);
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public List<DrawInfo> getCancelDrawInfos() {
        return this.cancelDrawInfos;
    }

    public List<DrawInfo> getFullImageDrawInfos() {
        return this.fullImageDrawInfos;
    }

    public int getLastFullImgIndex() {
        return this.lastFullImgIndex;
    }

    public int getLayerAlpha() {
        return this.layerAlpha;
    }

    public String getLayerPath() {
        return this.layerPath;
    }

    public List<DrawInfo> getSaveDrawInfos() {
        return this.saveDrawInfos;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    public boolean isHide() {
        return this.isHide;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setCancelDrawInfos(List<DrawInfo> list) {
        this.cancelDrawInfos = list;
    }

    public void setFullImageDrawInfos(List<DrawInfo> list) {
        this.fullImageDrawInfos = list;
    }

    public void setHide(boolean z) {
        this.isHide = z;
    }

    public void setLastFullImgIndex(int i2) {
        this.lastFullImgIndex = i2;
    }

    public void setLayerAlpha(int i2) {
        this.layerAlpha = i2;
    }

    public void setLayerPath(String str) {
        this.layerPath = str;
    }

    public void setSaveDrawInfos(List<DrawInfo> list) {
        this.saveDrawInfos = list;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public void setVersionCode(int i2) {
        this.versionCode = i2;
    }
}
