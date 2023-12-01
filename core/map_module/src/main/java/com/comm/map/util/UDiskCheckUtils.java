package com.comm.map.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import androidx.annotation.RequiresApi;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * u盘检测
 * 检测原理，插入U之后会将U盘的路径写到mounts文件中，判断文件中是否有U盘路径即可
 */
public class UDiskCheckUtils {

    //    private static final String MOUNTS_FILE = "/proc/mounts";
    private static final String MOUNTS_FILE = "/proc/partitions";

    public static boolean isMounted() {
        boolean blnRet = false;
        String strLine = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(MOUNTS_FILE));

            while ((strLine = reader.readLine()) != null) {
                if (strLine.contains("sda")) {
                    blnRet = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reader = null;
            }
        }
        return blnRet;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String uDiskName(Context context, int number) {
        StorageManager storageManager = context.getSystemService(StorageManager.class);
        List<StorageVolume> volumeList = storageManager.getStorageVolumes();

        if (number == 2) {
            if (volumeList.size() == 3) {
                return getSDCardPath(volumeList.get(2));
            } else if (volumeList.size() == 2) {
                return getSDCardPath(volumeList.get(1));
            }
        } else if (number == 1) {
            if (volumeList.size() == 2) {
                return getSDCardPath(volumeList.get(1));
            }
        }
        return null;
    }

     static String getSDCardPath(StorageVolume volume) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (null != volume && volume.isRemovable()) {
                String mPath = "";

                try {
                    Class myclass = Class.forName(volume.getClass().getName());
                    Method getPath = myclass.getDeclaredMethod("getPath");
                    getPath.setAccessible(true);
                    mPath = (String) getPath.invoke(volume);
                    return mPath;

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

/**
     * getAppDir:获取应用存放文件的主目录
     *
     * @return String DOM对象
     * @throws
     */
    public static String getAppDir(Context context) {
        String appId = context.getPackageName();
        int index = appId.lastIndexOf(".") + 1;
        String appDirPath = getSpValueForString(context, "AppDirName", Environment.getExternalStorageDirectory() + "/" + appId.substring(index));
        File appDirFile = new File(appDirPath);
        if (!appDirFile.exists()) {
            appDirFile.mkdirs();
        }

        return appDirPath;
    }

    public static String getSpValueForString(Context context, String spKey, String defValue) {
        SharedPreferences preference = context.getSharedPreferences(spKey, 0);
        return preference.getString(spKey, defValue);
    }

}
