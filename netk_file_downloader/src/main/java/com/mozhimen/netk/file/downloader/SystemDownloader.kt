package com.mozhimen.netk.file.downloader

import android.app.DownloadManager
import android.net.Uri
import android.widget.Toast
import com.mozhimen.kotlin.elemk.android.app.cons.CDownloadManager
import com.mozhimen.kotlin.utilk.android.app.UtilKActivityStart
import com.mozhimen.kotlin.utilk.android.content.UtilKPackage
import com.mozhimen.kotlin.utilk.android.net.uri2strFilePathName
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.commons.IUtilK
import com.mozhimen.netk.file.downloader.bases.BaseDownloader
import java.io.File

internal class SystemDownloader(request: DownloadRequest) : BaseDownloader(request), IUtilK {

    private var observer: DownloadObserver? = null

    private val downloader: SystemDownloadManager by lazy {
        SystemDownloadManager(request.context)
    }

    override fun startDownload() {
        if (!UtilKPackage.isDownloadComponentEnabled(request.context)) {
            Toast.makeText(
                request.context,
                R.string.netk_file_component_disable,
                Toast.LENGTH_SHORT
            ).show()
            UtilKActivityStart.startSettingApplicationDetailsSettings(request.context)
            //InstallUtils.showDownloadComponentSetting(request.context)
            return
        }

        if (request.ignoreLocal) {
            download()
            return
        }

        val downloadId = DownloadUtils.getLocalDownloadId(request.url)
        if (downloadId != -1L) {
            val downloadInfo = downloader.getDownloadInfo(downloadId)
            if (downloadInfo == null) {
                download()
                return
            }
            //获取下载状态
            when (downloadInfo.status) {
                CDownloadManager.STATUS_SUCCESSFUL -> {
                    val uri = downloader.getDownloadedFileUri(downloadId)
                    if (uri != null) {
                        val strFilePath = uri.uri2strFilePathName()//getRealPathFromURI(request.context, )
                        strFilePath?.let {
                            val file = File(it)
                            if (file.exists() && file.length() == downloadInfo.totalSize) {
                                request.onComplete(uri)
                                if (request.needInstall) //startInstall(request.context, file)
                                    return
                            }
                        }
                    }
                    //重新下载
                    download()
                }

                CDownloadManager.STATUS_RUNNING -> {
                    registerListener(downloadId)
                }

                CDownloadManager.STATUS_FAILED, CDownloadManager.STATUS_UNKNOWN -> {
                    download()
                }

                else -> printDownloadStatus(downloadId, downloadInfo.status)
            }
        } else {
            download()
        }
    }

    private fun registerListener(downloadId: Long) {
        if (observer != null) {
            return
        }
        val ob = DownloadObserver(request.context.applicationContext, downloadId, this)
        observer = ob
        request.context.contentResolver.registerContentObserver(
            Uri.parse("content://downloads/my_downloads/$downloadId"),
            true,
            ob
        )
    }

    private fun printDownloadStatus(downloadId: Long, status: Int) {
        when (status) {
            CDownloadManager.STATUS_PENDING -> UtilKLogWrapper.d(TAG, "downloadId=$downloadId, status=STATUS_PENDING")
            CDownloadManager.STATUS_PAUSED -> UtilKLogWrapper.d(TAG, "downloadId=$downloadId, status=STATUS_PAUSED")
            CDownloadManager.STATUS_RUNNING -> UtilKLogWrapper.d(TAG, "downloadId=$downloadId, status=STATUS_RUNNING")
            CDownloadManager.STATUS_SUCCESSFUL -> UtilKLogWrapper.d(TAG, "downloadId=$downloadId, status=STATUS_SUCCESSFUL")
            CDownloadManager.STATUS_FAILED -> UtilKLogWrapper.d(TAG, "downloadId=$downloadId, status=STATUS_FAILED")
            CDownloadManager.STATUS_UNKNOWN -> UtilKLogWrapper.d(TAG, "downloadId=$downloadId, status=STATUS_UNKNOWN")
        }
    }

    override fun download() {
        super.download()
        val dr = DownloadManager.Request(Uri.parse(request.url))
            .setTitle(request.notificationTitle)
            .setDescription(request.notificationContent)
            .setNotificationVisibility(request.notificationVisibility)
        //.setAllowedNetworkTypes(request.allowedNetworkTypes)
        val downloadId = downloader.download(dr)
        DownloadUtils.saveDownloadId(request.context, request.url, downloadId)
        registerListener(downloadId)
    }

}