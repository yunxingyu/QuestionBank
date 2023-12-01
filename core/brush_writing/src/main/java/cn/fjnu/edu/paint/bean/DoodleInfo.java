package cn.fjnu.edu.paint.bean;

import com.huawei.hms.framework.common.hianalytics.CrashHianalyticsData;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "DoodleInfo")
/* loaded from: classes.dex */
public class DoodleInfo {
    @Column(name = "count")
    private int count;
    @Column(name = "data")
    private byte[] data;
    @Column(name = "filePath")
    private String filePath;
    @Column(isId = true, name = "id")
    private int id;
    @Column(name = "isDelete")
    private boolean isDelete;
    @Column(name = CrashHianalyticsData.TIME)
    private long time;

    public int getCount() {
        return this.count;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public int getId() {
        return this.id;
    }

    public long getTime() {
        return this.time;
    }

    public boolean isDelete() {
        return this.isDelete;
    }

    public void setCount(int i2) {
        this.count = i2;
    }

    public void setData(byte[] bArr) {
        this.data = bArr;
    }

    public void setDelete(boolean z) {
        this.isDelete = z;
    }

    public void setFilePath(String str) {
        this.filePath = str;
    }

    public void setId(int i2) {
        this.id = i2;
    }

    public void setTime(long j) {
        this.time = j;
    }

    public String toString() {
        return "DoodleInfo{id=" + this.id + ", filePath='" + this.filePath + "', data='" + this.data + "', time=" + this.time + ", count=" + this.count + ", isDelete=" + this.isDelete + '}';
    }
}
