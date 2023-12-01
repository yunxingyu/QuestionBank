package cn.fjnu.edu.paint.bean;

/* loaded from: classes.dex */
public class PermissionRequestInfo {
    private int permissionDescription;
    private int permissionTitle;

    public PermissionRequestInfo(int i2, int i3) {
        this.permissionTitle = i2;
        this.permissionDescription = i3;
    }

    public int getPermissionDescription() {
        return this.permissionDescription;
    }

    public int getPermissionTitle() {
        return this.permissionTitle;
    }

    public void setPermissionDescription(int i2) {
        this.permissionDescription = i2;
    }

    public void setPermissionTitle(int i2) {
        this.permissionTitle = i2;
    }
}
