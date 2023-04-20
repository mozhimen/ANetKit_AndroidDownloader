package com.mozhimen.netkfiledownloader.util

import androidx.annotation.IntDef
import com.mozhimen.netkfiledownloader.DOWNLOAD_ENGINE_EMBED
import com.mozhimen.netkfiledownloader.DOWNLOAD_ENGINE_SYSTEM_DM

/**
 *
 * @author by chiclaim@google.com
 */
@IntDef(
    DOWNLOAD_ENGINE_EMBED,
    DOWNLOAD_ENGINE_SYSTEM_DM
)
@Retention(AnnotationRetention.SOURCE)
annotation class DownloadEngine
