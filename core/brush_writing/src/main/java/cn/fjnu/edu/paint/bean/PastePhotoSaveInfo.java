package cn.fjnu.edu.paint.bean;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/* loaded from: classes.dex */
public class PastePhotoSaveInfo {
    private Bitmap bitmap;
    private Matrix matrix;

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }
}
