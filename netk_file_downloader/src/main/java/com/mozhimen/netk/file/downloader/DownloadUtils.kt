package com.mozhimen.netk.file.downloader

import android.content.Context
import com.mozhimen.basick.utilk.kotlin.UtilKStrMd5
import com.mozhimen.netk.file.downloader.utils.SPUtil
import java.io.File

internal object DownloadUtils {

    fun getLocalDownloadId(url: String): Long {
        return SPUtil.getLong("${UtilKStrMd5.str2strMd5(url)}-id", -1L)
    }

    fun saveDownloadId(context: Context, url: String, id: Long) {
        SPUtil.putLong("${UtilKStrMd5.str2strMd5(url)}-id", id)
    }

    internal fun getPercent(totalSize: Long, downloadedSize: Long) = if (totalSize <= 0) 0 else
        (downloadedSize / totalSize.toDouble() * 100).toInt()

    fun getDownloadDir(context: Context): File {
        val dir = context.externalCacheDir
        // a file named cache
        if (dir?.isDirectory == true) {
            return dir
        }
        return context.filesDir
    }
}