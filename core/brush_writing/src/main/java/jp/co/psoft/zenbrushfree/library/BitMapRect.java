package jp.co.psoft.zenbrushfree.library;

import android.graphics.Bitmap;
import android.graphics.Rect;

public final class BitMapRect {
    Bitmap mBitMap;
    Rect mRect;

    public BitMapRect(Bitmap paramBitmap, Rect paramRect) {
        this.mBitMap = paramBitmap;
        this.mRect = paramRect;
    }

    public final void recycleData() {
        this.mBitMap.recycle();
        this.mBitMap = null;
    }
}
