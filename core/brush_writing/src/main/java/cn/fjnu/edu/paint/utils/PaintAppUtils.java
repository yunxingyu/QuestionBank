package cn.fjnu.edu.paint.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;

import org.json.JSONObject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.fjnu.edu.paint.R;
import cn.fjnu.edu.paint.bean.DrawInfo;
import cn.fjnu.edu.paint.bean.HeadOpInfo;
import cn.fjnu.edu.paint.bean.PermissionRequestInfo;
import cn.flynormal.baselib.bean.CloudSyncInfo;
import cn.flynormal.baselib.bean.UserInfo;
import cn.flynormal.baselib.bean.VIPInfo;
import cn.flynormal.baselib.service.HuaweiStorageManagerService;
import cn.flynormal.baselib.service.ServerManager;
import cn.flynormal.baselib.service.SharedPreferenceService;
import cn.flynormal.baselib.utils.BaseAppUtils;
import cn.flynormal.baselib.utils.DeviceUtils;
import cn.flynormal.baselib.utils.Md5Utils;
import cn.flynormal.baselib.utils.PixeUtils;

/* loaded from: classes.dex */
public class PaintAppUtils {
    private PaintAppUtils() {
    }

    public static List<DrawInfo> a(List<DrawInfo> list) {
        if (list instanceof ArrayList) {
            return (ArrayList) ((ArrayList) list).clone();
        }
        ArrayList arrayList = new ArrayList();
        try {
            for (DrawInfo drawInfo : list) {
                arrayList.add(drawInfo.clone());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return arrayList;
    }

    public static String b() {
        UserInfo z;
        if (SharedPreferenceService.I() && (z = SharedPreferenceService.z()) != null) {
            return z.getAccountId();
        }
        return "";
    }

    public static List<HeadOpInfo> c() {
        ArrayList arrayList = new ArrayList(20);
        arrayList.add(new HeadOpInfo(R.drawable.undo, 1, R.string.undo));
        arrayList.add(new HeadOpInfo(R.drawable.reply, 2, R.string.reply));
        arrayList.add(new HeadOpInfo(R.drawable.save, 3, R.string.save));
        arrayList.add(new HeadOpInfo(R.drawable.icon_paint2, 17, R.string.paint_setting));
        arrayList.add(new HeadOpInfo(R.drawable.ic_paint_color2, 21, R.string.paint_color));
        arrayList.add(new HeadOpInfo(R.drawable.ic_paint_size, 22, R.string.paint_size));
        arrayList.add(new HeadOpInfo(R.drawable.icon_head_shape2, 5, R.string.head_shape));
        arrayList.add(new HeadOpInfo(R.drawable.icon_fill_color, 8, R.string.fill_color));
        arrayList.add(new HeadOpInfo(R.drawable.text_main3, 16, R.string.text));
        arrayList.add(new HeadOpInfo(R.drawable.op_paset_photo2, 9, R.string.pastephoto));
        arrayList.add(new HeadOpInfo(R.drawable.background_main3, 7, R.string.head_background));
        arrayList.add(new HeadOpInfo(R.drawable.erase_main, 6, R.string.erase_setting));
        arrayList.add(new HeadOpInfo(R.drawable.empty_main, 12, R.string.empty_canvas));
        return arrayList;
    }

    public static List<HeadOpInfo> d(Context context) {
        List<HeadOpInfo> c2 = c();
        int g = (DeviceUtils.g(context) - PixeUtils.a(context, 10.0f)) / PixeUtils.a(context, 35.0f);
        if (g < c2.size() + 1) {
            c2 = c2.subList(0, g - 1);
        }
        c2.add(new HeadOpInfo(R.drawable.icon_head_more, 18, R.string.more));
        return c2;
    }

    public static List<PermissionRequestInfo> e() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PermissionRequestInfo(R.string.storage_permission, R.string.storage_permission_description));
        arrayList.add(new PermissionRequestInfo(R.string.imei_imsi_permission, R.string.imei_imsi_perimission_description));
        return arrayList;
    }

    public static String f() {
        String e2 = Md5Utils.e(UUID.randomUUID().toString());
        String substring = e2.substring(e2.length() - 16);
        return "com_" + substring;
    }

    public static String g(Activity activity, String str, String str2, String str3) {
        int i2;
        if (str.equals("vip_one_year_2")) {
            i2 = 2;
        } else if (str.equals("vip_one_month_2")) {
            i2 = 3;
        } else {
            i2 = str.equals("vip_perament3") ? 1 : -1;
        }
        if (i2 == -1) {
            return BaseAppUtils.a();
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("accountId", BaseAppUtils.a());
            jSONObject.put("vipType", i2);
            jSONObject.put("pkgName", activity.getPackageName());
            jSONObject.put("channelName", "tencent");
            jSONObject.put("xiaomiOrderId", str3);
            jSONObject.put("xiaomiUid", str2);
        } catch (Exception unused) {
        }
        return jSONObject.toString();
    }

    public static String h(String str) {
        File file = new File(x.a().getFilesDir(), "fonts");
        if (!file.exists()) {
            file.mkdirs();
        }
        return new File(file, Md5Utils.e(str) + ".ttf").getAbsolutePath();
    }

    public static boolean i(Context context) {
        boolean l2 = DeviceUtils.l(context);
        if (DeviceUtils.k()) {
            return false;
        }
        return l2;
    }

    public static boolean j(Context context) {
        List<PackageInfo> installedPackages = context.getPackageManager().getInstalledPackages(1);
        if (!(installedPackages == null || installedPackages.size() == 0)) {
            for (PackageInfo packageInfo : installedPackages) {
                if ("com.tencent.mm".equals(packageInfo.packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean k() {
        return !BaseAppUtils.s();
    }

    public static boolean l(Context context) {
        return DeviceUtils.e(context) > DeviceUtils.d(context);
    }

    public static boolean m(int i2) {
        return i2 == 22 || i2 == 23 || i2 == 24 || i2 == 25 || i2 == 26;
    }

    public static void n(Context context) throws Exception {
        if (SharedPreferenceService.z() != null) {
            p();
            HuaweiStorageManagerService.e().d(context);
        }
    }

    public static void o() {
        UserInfo z;
        if (SharedPreferenceService.I() && (z = SharedPreferenceService.z()) != null) {
            try {
                CloudSyncInfo result = ServerManager.b().o(z.getAccountId()).e().a().getResult();
                if (result != null) {
                    UserInfo z2 = SharedPreferenceService.z();
                    z2.setSyncOpen(result.isSyncOpen());
                    SharedPreferenceService.O(z2);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void p() {
        q();
        o();
    }

    public static void q() {
        UserInfo z;
        if (SharedPreferenceService.I() && (z = SharedPreferenceService.z()) != null) {
            try {
                VIPInfo result = ServerManager.b().i(z.getAccountId()).e().a().getResult();
                UserInfo z2 = SharedPreferenceService.z();
                z2.setVIP(result.isVIP());
                z2.setVipOverTime(result.getVipOverTime());
                SharedPreferenceService.O(z2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
