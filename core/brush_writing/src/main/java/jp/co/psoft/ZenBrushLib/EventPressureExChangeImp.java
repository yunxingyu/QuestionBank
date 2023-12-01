package jp.co.psoft.ZenBrushLib;

import android.view.MotionEvent;

/* loaded from: classes.dex */
public final class EventPressureExChangeImp extends EventPressureExChange {
    private EventPressureExChangeImp() {
        super((byte) 0);
    }

    public /* synthetic */ EventPressureExChangeImp(byte b) {
        this();
    }

    @Override // jp.co.psoft.ZenBrushLib.g
    public final float onEventPressures(MotionEvent motionEvent) {
        if (motionEvent.getToolType(0) == 2) {
            return motionEvent.getPressure();
        }
        return 1.0f;
    }
}
