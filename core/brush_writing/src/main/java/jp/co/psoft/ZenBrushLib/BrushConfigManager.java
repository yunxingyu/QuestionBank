package jp.co.psoft.ZenBrushLib;

import android.view.MotionEvent;

/* loaded from: classes.dex */
public abstract class BrushConfigManager {
    final /* synthetic */ ZenBrushRenderer brushRenderer;

    private BrushConfigManager(ZenBrushRenderer zenBrushRenderer) {
        this.brushRenderer = zenBrushRenderer;
    }

    public /* synthetic */ BrushConfigManager(ZenBrushRenderer zenBrushRenderer, byte b) {
        this(zenBrushRenderer);
    }

    public void onChangeBrushSize(float f) {
        this.brushRenderer.setBrushSize(f);
    }

    public void onBrushTypeChanged(int i) {
        this.brushRenderer.setBrushType(i - 1);
    }

    /* access modifiers changed from: package-private */
    public abstract void onMotionEvent(MotionEvent motionEvent);

    public void onBrushAlphaChange(float f) {
        this.brushRenderer.setBrushAlpha(f);
    }
}
