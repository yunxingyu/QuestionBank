package cn.fjnu.edu.paint.bean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: LocalFontInfo.kt */
@Metadata
@Table(name = "LocalFontInfo")
/* loaded from: classes.dex */
public final class LocalFontInfo {
    @Nullable
    @Column(name = "fontName")
    private String fontName;
    @Nullable
    @Column(isId = true, name = "fontPath")
    private String fontPath;
    private boolean isSelect;

    public LocalFontInfo() {
    }

    @Nullable
    public final String getFontName() {
        return this.fontName;
    }

    @Nullable
    public final String getFontPath() {
        return this.fontPath;
    }

    public final boolean isSelect() {
        return this.isSelect;
    }

    public final void setFontName(@Nullable String str) {
        this.fontName = str;
    }

    public final void setFontPath(@Nullable String str) {
        this.fontPath = str;
    }

    public final void setSelect(boolean z) {
        this.isSelect = z;
    }

    public LocalFontInfo(@NotNull String str, @NotNull String str2, boolean z) {
        Intrinsics.e(str, "fontName");
        Intrinsics.e(str2, "fontPath");
        this.fontName = str;
        this.fontPath = str2;
        this.isSelect = z;
    }

    public /* synthetic */ LocalFontInfo(String str, String str2, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, str2, (i2 & 4) != 0 ? false : z);
    }
}
