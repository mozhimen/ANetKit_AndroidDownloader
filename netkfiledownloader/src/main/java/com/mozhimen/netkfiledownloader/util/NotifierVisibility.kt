package com.mozhimen.netkfiledownloader.util

import androidx.annotation.IntDef
import com.mozhimen.netkfiledownloader.NOTIFIER_HIDDEN
import com.mozhimen.netkfiledownloader.NOTIFIER_VISIBLE
import com.mozhimen.netkfiledownloader.NOTIFIER_VISIBLE_NOTIFY_COMPLETED
import com.mozhimen.netkfiledownloader.NOTIFIER_VISIBLE_NOTIFY_ONLY_COMPLETION

/**
 *
 * @author by chiclaim@google.com
 */
@IntDef(
    NOTIFIER_VISIBLE,
    NOTIFIER_HIDDEN,
    NOTIFIER_VISIBLE_NOTIFY_COMPLETED,
    NOTIFIER_VISIBLE_NOTIFY_ONLY_COMPLETION
)
@Retention(AnnotationRetention.SOURCE)
annotation class NotifierVisibility
