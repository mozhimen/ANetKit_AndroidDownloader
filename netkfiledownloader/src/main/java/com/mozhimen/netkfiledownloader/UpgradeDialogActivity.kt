package com.mozhimen.netkfiledownloader

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mozhimen.netkfiledownloader.util.DownloadEngine
import com.mozhimen.netkfiledownloader.util.Utils.getTipFromException
import com.mozhimen.netkfiledownloader.util.e
import java.io.File

/**
 * 内置的更新弹窗
 * @author by chiclaim@google.com
 */
class UpgradeDialogActivity : AppCompatActivity(), DownloadListener {

    private var progressBar: ProgressBar? = null
    private var request: DownloadRequest? = null
    private var activityVisible = false

    private var tvNegative: TextView? = null
    private var tvPositive: TextView? = null

    private lateinit var dialogInfo: UpgradeDialogInfo

    companion object {

        private const val EXTRA_DIALOG_INFO = "EXTRA_DIALOG_INFO"
        private const val EXTRA_DOWNLOAD_MODE = "EXTRA_DOWNLOAD_MODE"


        @JvmStatic
        fun launch(
            context: Context,
            info: UpgradeDialogInfo,
            @DownloadEngine
            engine: Int = DOWNLOAD_ENGINE_EMBED
        ) {
            val intent = Intent(context, UpgradeDialogActivity::class.java)
            intent.putExtra(EXTRA_DIALOG_INFO, info)
            intent.putExtra(EXTRA_DOWNLOAD_MODE, engine)
            if (context is Application) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upgrade_dialog_layout)

        dialogInfo = intent.getParcelableExtra(EXTRA_DIALOG_INFO)
            ?: error("need $EXTRA_DIALOG_INFO parameter")

        val mode = intent.getIntExtra(EXTRA_DOWNLOAD_MODE, 0)


        progressBar = findViewById(R.id.pb_updater)

        findViewById<TextView>(R.id.tv_updater_title).text =
            dialogInfo.title ?: getString(R.string.downloader_title)

        findViewById<TextView>(R.id.tv_updater_desc).text =
            dialogInfo.description ?: getString(R.string.downloader_desc_default)

        // findViewById<MaxHeightScrollView>(R.id.scrollView).setMaxHeight(150)

        tvNegative = findViewById<TextView>(R.id.tv_updater_cancel).apply {
            this.text = dialogInfo.negativeText ?: getString(R.string.downloader_cancel)
        }

        tvPositive = findViewById<TextView>(R.id.tv_updater_confirm).apply {
            text = dialogInfo.positiveText ?: getString(R.string.downloader_ok)
        }
        findViewById<View>(R.id.tv_updater_cancel).setOnClickListener {
            if (dialogInfo.forceUpdate) {
                exitApp()
                return@setOnClickListener
            }
            finish()
        }

        val appName = applicationInfo.loadLabel(packageManager)

        findViewById<View>(R.id.tv_updater_confirm).setOnClickListener {
            val url = dialogInfo.url ?: return@setOnClickListener
            if (request == null) {
                request = DownloadRequest(applicationContext, url, mode)
                    .setNotificationSmallIcon(dialogInfo.notifierSmallIcon)
                    .setIgnoreLocal(dialogInfo.ignoreLocal)
                    .setNotificationTitle(appName)
                    .setNotificationContent(getString(R.string.downloader_notifier_description))
                    .setNeedInstall(dialogInfo.forceUpdate || dialogInfo.needInstall)
                dialogInfo.destinationPath?.let {
                    request?.setDestinationUri(Uri.fromFile(File(it)))
                }
            }
            request?.registerListener(this)
            val newState = request?.startDownload() ?: false

            // 非强制更新
            if (!dialogInfo.forceUpdate) {
                // 已经在下载
                if (!newState || dialogInfo.backgroundDownload) {
                    Toast.makeText(
                        applicationContext, R.string.downloader_notifier_background_downloading,
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                } else {
                    tvPositive?.setText(R.string.downloader_notifier_background_download)
                }
            }
        }
    }

    override fun onDownloadStart() {
    }

    override fun onProgressUpdate(
        percent: Int
    ) {
        progressBar?.isIndeterminate = percent == 0
        progressBar?.progress = percent
        if (BuildConfig.DEBUG) e("下载$percent%...")
    }

    override fun onDownloadComplete(uri: Uri) {
        if (!dialogInfo.forceUpdate) {
            finish()
        }
        // 强制更新情况下，用户从安装页面返回（取消安装），再次点击立即更新，不需要重新下载
        dialogInfo.ignoreLocal = false
        if (BuildConfig.DEBUG) e("下载完成...")

    }

    override fun onDownloadFailed(e: Throwable) {
        if (activityVisible) {
            val msg = getTipFromException(applicationContext, e)
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        activityVisible = hasFocus
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.unregisterListener(this)
    }

    private fun exitApp() {
        val activityManager: ActivityManager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.runningAppProcesses.forEach {
            if (it.pid != android.os.Process.myPid()) android.os.Process.killProcess(it.pid)
        }
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    override fun onBackPressed() {
        if (!dialogInfo.forceUpdate)
            super.onBackPressed()
    }
}