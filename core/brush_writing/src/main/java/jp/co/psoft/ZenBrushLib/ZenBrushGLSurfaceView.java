package jp.co.psoft.ZenBrushLib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class ZenBrushGLSurfaceView extends GLSurfaceView {
    public final ZenBrushRenderer brushRenderer;
    private final String TAG = ZenBrushGLSurfaceView.class.getName();


    public ZenBrushGLSurfaceView(Context context) {
        this(context, null);
    }

    public ZenBrushGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        getHolder().setFormat(1);
        setEGLConfigChooser(8, 8, 8, 8, 0, 0);
        this.brushRenderer = new ZenBrushRenderer();
        setRenderer(this.brushRenderer);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        new Handler().postDelayed(()->{
            this.brushRenderer.setBrushSize(5);
        },5000);


    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.brushRenderer.onTouchEvent(motionEvent);
        return true;
    }


    @Override
    public void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow()");
        super.onAttachedToWindow();

    }

    @Override
    public void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow()");
        super.onDetachedFromWindow();
    }




}
