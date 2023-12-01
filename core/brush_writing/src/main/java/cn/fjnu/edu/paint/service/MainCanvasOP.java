package cn.fjnu.edu.paint.service;

import android.view.View;

import androidx.annotation.IdRes;

import cn.fjnu.edu.paint.bean.HeadOpInfo;

/* loaded from: classes.dex */
public interface MainCanvasOP {
    int e();

    <T extends View> T findViewById(@IdRes int i2);

    int g();

    void i();

    void j(HeadOpInfo headOpInfo);
}
