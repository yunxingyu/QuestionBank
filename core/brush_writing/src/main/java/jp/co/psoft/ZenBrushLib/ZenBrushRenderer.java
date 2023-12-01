package jp.co.psoft.ZenBrushLib;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes.dex */
public class ZenBrushRenderer implements GLSurfaceView.Renderer {
    public int mWidth;
    public int mHeight;
    public final BrushConfigManager brushConfigManager;
    private long mCurrentTime;
    private boolean isStart;
    private int mCurrentPointIndex;
    private float mCurrentPositionX;
    private float mCurrentPositionY;
    private float mCurrentPressures;
    private boolean mDSarted;
    private int mDPointIndex;
    private float mDPositionX;
    private float mDPositionY;
    private float mDPressures;
    private Boolean isIniBg = false;
    private final EventPressureExChange mPressExChange;

    static {
        System.loadLibrary("ZenBrushRenderer_Android");
    }

    public ZenBrushRenderer() {
        this.brushConfigManager = new BrushConfigManagerImp(this, (byte) 0);
        this.mPressExChange =   new EventPressureExChangeImp((byte) 0) ;
    }


    private native void onNdkDrawFrame();

    private native void onNdkSurfaceChanged(int mWidth, int mHeight);

    private native boolean setBackgroundImageARGB8888(int[] iArr, int i, int i2);

    /* access modifiers changed from: private */
    public native void setBrushAlpha(float brushAlpha);

    /* access modifiers changed from: private */
    public native void setBrushSize(float brushSize);

    /* access modifiers changed from: private */
    public native void setBrushType(int brushType);

    public final boolean isIniBg() {
        boolean booleanValue;
        synchronized (this.isIniBg) {
            booleanValue = this.isIniBg.booleanValue();
        }
        return booleanValue;
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int pointIndex = 0;
        switch (motionEvent.getAction()) {
            case ACTION_DOWN:
                synchronized (this) {
                    this.brushConfigManager.onMotionEvent(motionEvent);
                    if (this.mCurrentPointIndex != Integer.MAX_VALUE) {
                        pointIndex = this.mCurrentPointIndex + 1;
                    }
                    this.mCurrentPointIndex = pointIndex;
                    this.isStart = true;
                    this.mCurrentPositionX = motionEvent.getX();
                    this.mCurrentPositionY = motionEvent.getY();
                    this.mCurrentPressures = this.mPressExChange.onEventPressures(motionEvent);
                }
                break;
            case ACTION_UP:
            case ACTION_CANCEL:
                synchronized (this) {
                    this.isStart = false;
                }
                break;
            case ACTION_MOVE:
                synchronized (this) {
                    this.brushConfigManager.onMotionEvent(motionEvent);
                    this.mCurrentPositionX = motionEvent.getX();
                    this.mCurrentPositionY = motionEvent.getY();
                    this.mCurrentPressures = this.mPressExChange.onEventPressures(motionEvent);
                }
                break;
        }
        return true;
    }

    public final boolean onBackGroundImage(int[] iArr, int i, int i2) {
        boolean backgroundImageARGB8888;
        synchronized (this.isIniBg) {
            backgroundImageARGB8888 = setBackgroundImageARGB8888(iArr, i, i2);
            this.isIniBg = Boolean.valueOf(backgroundImageARGB8888);
        }
        return backgroundImageARGB8888;
    }

    public native boolean canClearInk();

    public native boolean canRedo();

    public native boolean canUndo();

    public native boolean canvasInitialize(int width, int height);

    public native boolean canvasUpdate(int i, float positonX, float positionY, float pressures, double factor);

    public native void clearInk();

    public native boolean getImageARGB8888(int[] iArr);

    public native boolean getInkImageARGB8888(int[] iArr);

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        double mFactor = 0.06666666666666667d;
        synchronized (this) {
            this.mDSarted = this.isStart;
            this.mDPointIndex = this.mCurrentPointIndex;
            this.mDPositionX = this.mCurrentPositionX;
            this.mDPositionY = this.mCurrentPositionY;
            this.mDPressures = this.mCurrentPressures;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mCurrentTime != Long.MIN_VALUE) {
            double timeFactor = 0.001d * ((double) (currentTimeMillis - this.mCurrentTime));
            if (timeFactor <= 0.06666666666666667d) {
                mFactor = timeFactor;
            }
            canvasUpdate(this.mDSarted ? this.mDPointIndex : -1, this.mDPositionX, this.mDPositionY, this.mDPressures, mFactor);
        }
        this.mCurrentTime = currentTimeMillis;
        onNdkDrawFrame();


    }

    @Override // android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        synchronized (this.isIniBg) {
            this.isIniBg = false;
        }
        onNdkSurfaceChanged(width, height);
        this.mWidth = width;
        this.mHeight = height;
        if (this.mWidth >= this.mHeight && this.mWidth > 1024) {
            this.mHeight = (this.mHeight * 1024) / this.mWidth;
            this.mWidth = 1024;
        } else if (this.mHeight >= this.mWidth && this.mHeight > 1024) {
            this.mWidth = (this.mWidth * 1024) / this.mHeight;
            this.mHeight = 1024;
        }
        canvasInitialize(this.mWidth, this.mHeight);

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        this.mCurrentTime = Long.MIN_VALUE;
        this.isStart = false;
        this.mCurrentPointIndex = 0;
    }

    public native void redo();

    public native void setBackgroundColor(float red, float green, float blue);

    public native void setBrushTintColor(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    public native int setInkImageARGB8888(int[] iArr);

    public native void undo();
}
