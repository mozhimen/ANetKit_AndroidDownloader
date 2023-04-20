package com.mozhimen.netkfiledownloader.util

import androidx.annotation.IntDef
import com.mozhimen.netkfiledownloader.*

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
