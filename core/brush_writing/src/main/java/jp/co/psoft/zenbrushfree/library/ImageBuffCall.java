package jp.co.psoft.zenbrushfree.library;

import java.util.concurrent.Callable;

public final class ImageBuffCall implements Callable {
    ImageBuffBackTaskImp imageBuffBackTaskImp;
    protected ImageBuffCall(ImageBuffBackTaskImp imageBuffBackTaskImp) {
        this.imageBuffBackTaskImp = imageBuffBackTaskImp;
    }

    @Override
    public Object call() {
        // TODO Auto-generated method stub
        return imageBuffBackTaskImp.onFutureTask();
    }
}
