package jp.co.psoft.zenbrushfree.library;

import java.util.concurrent.FutureTask;

import jp.co.psoft.ZenBrushLib.ZenBrushGLSurfaceView;

public abstract class ImageBuffBackTask {
    protected final ZenBrushGLSurfaceView glSurfaceView;

    protected ImageBuffBackTask(ZenBrushGLSurfaceView paramZenBrushGLSurfaceView) {
        this.glSurfaceView = paramZenBrushGLSurfaceView;
    }

    protected abstract FutureTask onFutureTask();

    public   void onImageCallBack(ImageBuffDataCallBack callBack) {
       // callBack.onImageBuffCallBack( new ImageBuffData(glSurfaceView.getDrawableState(),0,0));
    }

}
