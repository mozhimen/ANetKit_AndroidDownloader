package com.mozhimen.netk.file.downloader

import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import com.mozhimen.kotlin.elemk.android.app.cons.CDownloadManager
import com.mozhimen.netk.file.downloader.DownloadUtils.getPercent
import com.mozhimen.netk.file.downloader.bases.BaseDownloader
import com.mozhimen.netk.file.downloader.cons.CErrorCode
import com.mozhimen.netk.file.downloader.mos.DownloadException

/**
 *
 * @author by chiclaim@google.com
 */
internal class DownloadObserver(
    context: Context,
    private val downloadId: Long,
    private var downloader: BaseDownloader
) : ContentObserver(null) {


    private val context: Context = context.applicationContext

    private val handler by lazy {
        Handler(Looper.getMainLooper())
    }


    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        DownloadExecutor.execute {
            val info = SystemDownloadManager(context).getDownloadInfo(downloadId) ?: return@execute
            handler.post {
                downloader.request.onProgressUpdate(getPercent(info.totalSize, info.downloadedSize))
                when (info.status) {
                    CDownloadManager.STATUS_SUCCESSFUL -> {
                        context.contentResolver.unregisterContentObserver(this)
                        val uri = info.uri
                        if (uri == null) {
                            downloader.request.onFailed(
                                DownloadException(
                                    CErrorCode.MISSING_URI,
                                    context.getString(R.string.netk_file_notifier_failed_missing_uri)
                                )
                            )
                        } else {
                            downloader.request.onComplete(uri)
                        }
                    }
                    CDownloadManager.STATUS_FAILED -> {
                        context.contentResolver.unregisterContentObserver(this)
                        downloader.request.onFailed(
                            DownloadException(
                                CErrorCode.DM_FAILED,
                                context.getString(
                                    R.string.netk_file_notifier_content_err_placeholder,
                                    info.reason ?: "-"
                                )
                            )
                        )
                    }
                }
            }
        }

    }
}