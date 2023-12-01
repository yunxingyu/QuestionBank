package jp.co.psoft.ZenBrushLib;

import android.view.MotionEvent;

/* loaded from: classes.dex */
public abstract class EventPressureExChange {
    private EventPressureExChange() {
    }

    public /* synthetic */ EventPressureExChange(byte b) {
        this();
    }

    public abstract float onEventPressures(MotionEvent motionEvent);
}
