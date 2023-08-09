package com.mozhimen.netk_file.util

import androidx.annotation.IntDef
import com.mozhimen.netk_file.*

/**
 *
 * @author by chiclaim@google.com
 */
@IntDef(
    STATUS_PENDING,
    STATUS_PAUSED,
    STATUS_RUNNING,
    STATUS_SUCCESSFUL,
    STATUS_FAILED,
    STATUS_UNKNOWN
)
@Retention(AnnotationRetention.SOURCE)
annotation class DownloadStatus
