package com.mozhimen.netk_file.util

import androidx.annotation.IntDef
import com.mozhimen.netk_file.DOWNLOAD_ENGINE_EMBED
import com.mozhimen.netk_file.DOWNLOAD_ENGINE_SYSTEM_DM

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
