package jp.co.psoft.ZenBrushLib;

import android.view.MotionEvent;

/* loaded from: classes.dex */
public final class BrushConfigManagerImp extends BrushConfigManager {

    protected int configStatue;
    protected int mBrushType;
    protected float mBrushSize;
    protected float mBrushAlpha;
    final  ZenBrushRenderer zenBrushRenderer;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    private BrushConfigManagerImp(ZenBrushRenderer zenBrushRenderer) {
        super(zenBrushRenderer, (byte) 0);
        this.zenBrushRenderer = zenBrushRenderer;
        this.configStatue = -1;
        this.mBrushType = 0;
        this.mBrushSize = 1.0f;
        this.mBrushAlpha = 1.0f;
    }

    public  BrushConfigManagerImp(ZenBrushRenderer zenBrushRenderer, byte b) {
        this(zenBrushRenderer);
    }

    @Override // jp.co.psoft.ZenBrushLib.i
    public void onChangeBrushSize(float brushSize) {
        super.onChangeBrushSize(brushSize);
        this.mBrushSize = brushSize;
        this.configStatue = 0;
    }

    @Override // jp.co.psoft.ZenBrushLib.i
    public void onBrushTypeChanged(int brushType) {
        super.onBrushTypeChanged(brushType);
        this.mBrushType = brushType - 1;
        this.configStatue = 0;
    }

    /* access modifiers changed from: package-private */
    @Override // jp.co.psoft.ZenBrushLib.i
    public void onMotionEvent(MotionEvent motionEvent) {
        if (motionEvent.getToolType(0) == 4) {
            if (this.configStatue != 1) {
                this.configStatue = 1;
                this.zenBrushRenderer.setBrushType((int) (mBrushAlpha - 1));
                this.zenBrushRenderer.setBrushSize(20.0f);
                this.zenBrushRenderer.setBrushAlpha(1.0f);
            }
        } else if (this.configStatue != 0) {
            this.configStatue = 0;
            this.zenBrushRenderer.setBrushType(this.mBrushType);
            this.zenBrushRenderer.setBrushSize(this.mBrushSize);
            this.zenBrushRenderer.setBrushAlpha(this.mBrushAlpha);
        }
    }

    @Override // jp.co.psoft.ZenBrushLib.i
    public void onBrushAlphaChange(float brushAlpha) {
        super.onBrushAlphaChange(brushAlpha);
        this.mBrushAlpha = brushAlpha;
        this.configStatue = 0;
    }
}
