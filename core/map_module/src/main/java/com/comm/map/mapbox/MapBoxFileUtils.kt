package com.comm.map.mapbox

import android.content.Context
import android.os.Build
import android.os.storage.StorageManager
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.comm.map.util.UDiskCheckUtils
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * @Author create by LJ
 * @Date 2023/2/17 09
 */
object MapBoxFileUtils {
    /**
     * 文件读写
     */
    fun writeFile(path: String?, content: String?, append: Boolean): Boolean {
        if (path == null) {
            return false
        }
        var ret = false
        val createFileSuccess: Boolean = if (!isExist(path)) {
            createFileByPath(path)
        } else {
            true
        }
        if (createFileSuccess && content != null && content.length > 0) {
            var bw: BufferedWriter? = null
            try {
                bw = BufferedWriter(FileWriter(path, append), 1024)
                bw.write(content)
                bw.flush()
                ret = true
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (null != bw) {
                    try {
                        bw.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return ret
    }

    fun isExist(path: String?): Boolean {
        return File(path).exists()
    }

    fun createFileByPath(path: String?): Boolean {
        if (!TextUtils.isEmpty(path)) {
            val file = File(path)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                    return false
                }
                return true
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun uDiskName(context: Context, number: Int): String? {
        val storageManager = context.getSystemService(StorageManager::class.java)
        val volumeList = storageManager.storageVolumes
        if (number == 2) {
            if (volumeList.size == 3) {
                return UDiskCheckUtils.getSDCardPath(volumeList[2])
            } else if (volumeList.size == 2) {
                return UDiskCheckUtils.getSDCardPath(volumeList[1])
            }
        } else if (number == 1) {
            if (volumeList.size == 2) {
                return UDiskCheckUtils.getSDCardPath(volumeList[1])
            }
        }
        return null
    }

}