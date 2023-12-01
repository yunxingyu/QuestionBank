package jp.co.psoft.zenbrushfree.library;

import java.util.concurrent.FutureTask;

import jp.co.psoft.ZenBrushLib.ZenBrushGLSurfaceView;

public final class ImageBuffBackTaskImp extends ImageBuffBackTask {

    public ImageBuffBackTaskImp(ZenBrushGLSurfaceView paramZenBrushGLSurfaceView) {
        super(paramZenBrushGLSurfaceView);
    }

    protected FutureTask onFutureTask() {
        return new FutureTask(new ImageBuffCall(this));
    }

}
