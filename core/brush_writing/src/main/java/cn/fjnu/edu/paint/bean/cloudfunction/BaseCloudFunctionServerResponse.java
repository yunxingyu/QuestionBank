package cn.fjnu.edu.paint.bean.cloudfunction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BaseCloudFunctionServerResponse.kt */
@Metadata
/* loaded from: classes.dex */
public final class BaseCloudFunctionServerResponse<T> {
    @Nullable
    private T data;
    private int code = -1;
    @NotNull
    private String msg = "Unknow error";

    public final int getCode() {
        return this.code;
    }

    @Nullable
    public final T getData() {
        return this.data;
    }

    @NotNull
    public final String getMsg() {
        return this.msg;
    }

    public final void setCode(int i2) {
        this.code = i2;
    }

    public final void setData(@Nullable T t) {
        this.data = t;
    }

    public final void setMsg(@NotNull String str) {
        Intrinsics.e(str, "<set-?>");
        this.msg = str;
    }
}
