package com.mozhimen.netk.file.downloader.mos

import android.net.Uri
import com.mozhimen.kotlin.elemk.android.app.cons.CDownloadManager

/**
 *
 * @author by chiclaim@google.com
 */
class DownloadInfo(var downloadId: Long = 0L) {
    var totalSize = 0L
    var downloadedSize = 0L
    var status = CDownloadManager.STATUS_UNKNOWN
    var reason: String? = null
    var uri: Uri? = null
}