package com.mozhimen.netk_file

import android.net.Uri

/**
 *
 * @author by chiclaim@google.com
 */
interface DownloadListener {

    fun onDownloadStart()

    fun onProgressUpdate(percent: Int)


    fun onDownloadComplete(uri: Uri)

    fun onDownloadFailed(e: Throwable)


}