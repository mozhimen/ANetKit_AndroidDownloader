package com.mozhimen.netk.file.downloader.mos

/**
 *
 * @author by chiclaim@google.com
 */
internal class DownloadException(val errorType: Int, errMsg: String, val responseCode: Int = 0) : Exception(errMsg)