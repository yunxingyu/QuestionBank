package cn.fjnu.edu.paint.bean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: FontDecoratorInfo.kt */
@Metadata
/* loaded from: classes.dex */
public final class FontDecoratorInfo {
    @NotNull
    private final String des;
    private final int type;

    public FontDecoratorInfo(@NotNull String str, int i2) {
        Intrinsics.e(str, "des");
        this.des = str;
        this.type = i2;
    }

    public static /* synthetic */ FontDecoratorInfo copy$default(FontDecoratorInfo fontDecoratorInfo, String str, int i2, int i3, Object obj) {
        if ((i3 & 1) != 0) {
            str = fontDecoratorInfo.des;
        }
        if ((i3 & 2) != 0) {
            i2 = fontDecoratorInfo.type;
        }
        return fontDecoratorInfo.copy(str, i2);
    }

    @NotNull
    public final String component1() {
        return this.des;
    }

    public final int component2() {
        return this.type;
    }

    @NotNull
    public final FontDecoratorInfo copy(@NotNull String str, int i2) {
        Intrinsics.e(str, "des");
        return new FontDecoratorInfo(str, i2);
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FontDecoratorInfo)) {
            return false;
        }
        FontDecoratorInfo fontDecoratorInfo = (FontDecoratorInfo) obj;
        return Intrinsics.a(this.des, fontDecoratorInfo.des) && this.type == fontDecoratorInfo.type;
    }

    @NotNull
    public final String getDes() {
        return this.des;
    }

    public final int getType() {
        return this.type;
    }

    public int hashCode() {
        return (this.des.hashCode() * 31) + this.type;
    }

    @NotNull
    public String toString() {
        return "FontDecoratorInfo(des=" + this.des + ", type=" + this.type + ')';
    }
}
