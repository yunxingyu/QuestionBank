package cn.fjnu.edu.paint.bean;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import cn.flynormal.baselib.utils.BitmapUtils;

/* loaded from: classes.dex */
public class DrawInfo implements Serializable, Cloneable {
    private static final long serialVersionUID = 5910993302876501878L;
    private String customBrushImgPath;
    private List<DrawPointInfo> drawPointInfos;
    private float elementBottom;
    private float elementLeft;
    private float elementRight;
    private float elementTop;
    private int fillColor;
    private byte[] imgData;
    private int imgHeight;
    private int imgType;
    private int imgWidth;
    private float imgX;
    private float imgY;
    private boolean isFillMode;
    private boolean isLightEffect;
    private float[] martixValues;
    private int mode;
    private int originImgType;
    private int paintColor;
    private float paintSize;
    private List<PointInfo> pointInfos;
    private float radious;
    private int shapeType;
    private String text;
    private int textColor;
    private float textSkewX;
    private float textX;
    private float textY;

    public void changeToMemoryObject() {
        byte[] imgData = getImgData();
        if (imgData != null) {
            ExtendDrawInfo extendDrawInfo = getExtendDrawInfo();
            boolean z = true;
            if (extendDrawInfo == null) {
                if (getMode() != 4) {
                    z = false;
                }
                saveImgDataToFile(imgData, z);
            } else {
                String imgPath = extendDrawInfo.getImgPath();
                if (TextUtils.isEmpty(imgPath)) {
                    if (!extendDrawInfo.isFullImg() && getMode() != 4) {
                        z = false;
                    }
                    saveImgDataToFile(imgData, z);
                } else {
                    File file = new File(imgPath);
                    if (!file.exists()) {
                        if (!extendDrawInfo.isFullImg() && getMode() != 4) {
                            z = false;
                        }
                        saveImgDataToFile(imgData, z);
                    } else if (file.length() != ((long) imgData.length)) {
                        saveImgDataToFile(imgData, extendDrawInfo.isFullImg());
                    } else {
                        Log.e("DrawInfo", "changeToMemoryObject->本地文件存在");
                    }
                }
            }
        }
        setImgData(null);
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DrawInfo drawInfo = (DrawInfo) obj;
        if (this.paintColor == drawInfo.paintColor && Float.compare(drawInfo.paintSize, this.paintSize) == 0 && this.shapeType == drawInfo.shapeType && this.isLightEffect == drawInfo.isLightEffect && this.isFillMode == drawInfo.isFillMode && Float.compare(drawInfo.radious, this.radious) == 0 && this.mode == drawInfo.mode && this.fillColor == drawInfo.fillColor && this.textColor == drawInfo.textColor && Float.compare(drawInfo.textSkewX, this.textSkewX) == 0 && Float.compare(drawInfo.imgX, this.imgX) == 0 && Float.compare(drawInfo.imgY, this.imgY) == 0 && this.imgType == drawInfo.imgType && this.originImgType == drawInfo.originImgType && Float.compare(drawInfo.textX, this.textX) == 0 && Float.compare(drawInfo.textY, this.textY) == 0 && this.imgWidth == drawInfo.imgWidth && this.imgHeight == drawInfo.imgHeight && Float.compare(drawInfo.elementLeft, this.elementLeft) == 0 && Float.compare(drawInfo.elementTop, this.elementTop) == 0 && Float.compare(drawInfo.elementRight, this.elementRight) == 0 && Float.compare(drawInfo.elementBottom, this.elementBottom) == 0 && Objects.equals(this.pointInfos, drawInfo.pointInfos) && Objects.equals(this.drawPointInfos, drawInfo.drawPointInfos) && Arrays.equals(this.imgData, drawInfo.imgData) && Arrays.equals(this.martixValues, drawInfo.martixValues) && Objects.equals(this.text, drawInfo.text) && Objects.equals(this.customBrushImgPath, drawInfo.customBrushImgPath)) {
            return true;
        }
        return false;
    }

    public String getCustomBrushImgPath() {
        return this.customBrushImgPath;
    }

    public List<DrawPointInfo> getDrawPointInfos() {
        return this.drawPointInfos;
    }

    public float getElementBottom() {
        return this.elementBottom;
    }

    public float getElementLeft() {
        return this.elementLeft;
    }

    public float getElementRight() {
        return this.elementRight;
    }

    public float getElementTop() {
        return this.elementTop;
    }

    public ExtendDrawInfo getExtendDrawInfo() {
        String text = getText();
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        try {
            return (ExtendDrawInfo) new Gson().fromJson(text, (Class<Object>) ExtendDrawInfo.class);
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public int getFillColor() {
        return this.fillColor;
    }

    public byte[] getImgData() {
        return this.imgData;
    }

    public byte[] getImgDataFromFile() {
        byte[] bArr = this.imgData;
        if (bArr != null) {
            return bArr;
        }
        ExtendDrawInfo extendDrawInfo = getExtendDrawInfo();
        if (extendDrawInfo == null) {
            return null;
        }
        String imgPath = extendDrawInfo.getImgPath();
        if (TextUtils.isEmpty(imgPath)) {
            return null;
        }
        int imgWidth = getImgWidth();
        int imgHeight = getImgHeight();
        if (imgWidth == 0 || imgHeight == 0) {
            return null;
        }
        return BitmapUtils.c(imgPath);
    }

    public Bitmap getImgFromFile() {
        byte[] bArr = this.imgData;
        if (bArr != null) {
            return BitmapUtils.a(bArr, getImgWidth(), getImgHeight());
        }
        ExtendDrawInfo extendDrawInfo = getExtendDrawInfo();
        if (extendDrawInfo == null) {
            return null;
        }
        String imgPath = extendDrawInfo.getImgPath();
        if (TextUtils.isEmpty(imgPath)) {
            return null;
        }
        int imgWidth = getImgWidth();
        int imgHeight = getImgHeight();
        if (imgWidth == 0 || imgHeight == 0) {
            return null;
        }
        return BitmapUtils.d(imgPath);
    }

    public int getImgHeight() {
        return this.imgHeight;
    }

    public int getImgType() {
        return this.imgType;
    }

    public int getImgWidth() {
        return this.imgWidth;
    }

    public float getImgX() {
        return this.imgX;
    }

    public float getImgY() {
        return this.imgY;
    }

    public float[] getMartixValues() {
        return this.martixValues;
    }

    public int getMode() {
        return this.mode;
    }

    public int getOriginImgType() {
        return this.originImgType;
    }

    public int getPaintColor() {
        return this.paintColor;
    }

    public float getPaintSize() {
        return this.paintSize;
    }

    public List<PointInfo> getPointInfos() {
        return this.pointInfos;
    }

    public float getRadious() {
        return this.radious;
    }

    public int getShapeType() {
        return this.shapeType;
    }

    public String getText() {
        return this.text;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public float getTextSkewX() {
        return this.textSkewX;
    }

    public float getTextX() {
        return this.textX;
    }

    public float getTextY() {
        return this.textY;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return (((Objects.hash(Integer.valueOf(this.paintColor), Float.valueOf(this.paintSize), Integer.valueOf(this.shapeType), Boolean.valueOf(this.isLightEffect), Boolean.valueOf(this.isFillMode), this.pointInfos, this.drawPointInfos, Float.valueOf(this.radious), Integer.valueOf(this.mode), Integer.valueOf(this.fillColor), this.text, Integer.valueOf(this.textColor), Float.valueOf(this.textSkewX), Float.valueOf(this.imgX), Float.valueOf(this.imgY), Integer.valueOf(this.imgType), Integer.valueOf(this.originImgType), Float.valueOf(this.textX), Float.valueOf(this.textY), Integer.valueOf(this.imgWidth), Integer.valueOf(this.imgHeight), Float.valueOf(this.elementLeft), Float.valueOf(this.elementTop), Float.valueOf(this.elementRight), Float.valueOf(this.elementBottom), this.customBrushImgPath) * 31) + Arrays.hashCode(this.imgData)) * 31) + Arrays.hashCode(this.martixValues);
    }

    public boolean isFillMode() {
        return this.isFillMode;
    }

    public boolean isLightEffect() {
        return this.isLightEffect;
    }

    public void saveImgDataToFile(Bitmap bitmap, boolean z) {
        ExtendDrawInfo extendDrawInfo = new ExtendDrawInfo();
        File file = new File(x.a().getFilesDir(), "CacheImg");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, UUID.randomUUID().toString() + ".png");
        try {
            System.currentTimeMillis();
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            System.currentTimeMillis();
            extendDrawInfo.setImgPath(file2.getAbsolutePath());
            extendDrawInfo.setFullImg(z);
            setText(new Gson().toJson(extendDrawInfo));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void setCustomBrushImgPath(String str) {
        this.customBrushImgPath = str;
    }

    public void setDrawPointInfos(List<DrawPointInfo> list) {
        this.drawPointInfos = list;
    }

    public void setElementBottom(float f2) {
        this.elementBottom = f2;
    }

    public void setElementLeft(float f2) {
        this.elementLeft = f2;
    }

    public void setElementRight(float f2) {
        this.elementRight = f2;
    }

    public void setElementTop(float f2) {
        this.elementTop = f2;
    }

    public void setFillColor(int i2) {
        this.fillColor = i2;
    }

    public void setFillMode(boolean z) {
        this.isFillMode = z;
    }

    public void setImgData(byte[] bArr) {
        this.imgData = bArr;
    }

    public void setImgHeight(int i2) {
        this.imgHeight = i2;
    }

    public void setImgType(int i2) {
        this.imgType = i2;
    }

    public void setImgWidth(int i2) {
        this.imgWidth = i2;
    }

    public void setImgX(float f2) {
        this.imgX = f2;
    }

    public void setImgY(float f2) {
        this.imgY = f2;
    }

    public void setLightEffect(boolean z) {
        this.isLightEffect = z;
    }

    public void setMartixValues(float[] fArr) {
        this.martixValues = fArr;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setOriginImgType(int i2) {
        this.originImgType = i2;
    }

    public void setPaintColor(int i2) {
        this.paintColor = i2;
    }

    public void setPaintSize(float f2) {
        this.paintSize = f2;
    }

    public void setPointInfos(List<PointInfo> list) {
        this.pointInfos = list;
    }

    public void setRadious(float f2) {
        this.radious = f2;
    }

    public void setShapeType(int i2) {
        this.shapeType = i2;
    }

    public void setText(String str) {
        this.text = str;
    }

    public void setTextColor(int i2) {
        this.textColor = i2;
    }

    public void setTextSkewX(float f2) {
        this.textSkewX = f2;
    }

    public void setTextX(float f2) {
        this.textX = f2;
    }

    public void setTextY(float f2) {
        this.textY = f2;
    }

    @Override // java.lang.Object
    @NonNull
    public DrawInfo clone() throws CloneNotSupportedException {
        return (DrawInfo) super.clone();
    }

    public void saveImgDataToFile(byte[] bArr, boolean z) {
        ExtendDrawInfo extendDrawInfo = new ExtendDrawInfo();
        File file = new File(x.a().getFilesDir(), "CacheImg");
        String absolutePath = new File(file, UUID.randomUUID().toString() + ".png").getAbsolutePath();
        if (BitmapUtils.i(bArr, absolutePath, this.imgWidth, this.imgHeight)) {
            extendDrawInfo.setImgPath(absolutePath);
            extendDrawInfo.setFullImg(z);
            setText(new Gson().toJson(extendDrawInfo));
        }
    }
}
