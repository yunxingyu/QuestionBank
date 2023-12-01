package jp.co.psoft.zenbrushfree.library;

import android.graphics.Bitmap.CompressFormat;

import java.io.BufferedOutputStream;

public final class ImageBuffData {
    public final int[] buffBytes;
    public final int mKey;
    public final int mValues;

    public ImageBuffData(int[] buffBytes, int mKey, int mValues) {
        this.buffBytes = buffBytes;
        this.mKey = mKey;
        this.mValues = mValues;
    }

    public final boolean a(BufferedOutputStream paramBufferedOutputStream,
            CompressFormat paramCompressFormat, BitMapRect[] bitMapRects) {
        return true;
    }
}