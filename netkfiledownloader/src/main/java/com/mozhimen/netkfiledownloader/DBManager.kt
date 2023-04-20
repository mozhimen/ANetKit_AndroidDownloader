package com.mozhimen.netkfiledownloader

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 *
 * @author by chiclaim@google.com
 */
internal object DBManager {

    const val DOWNLOAD_DB_NAME = "download_db"
    const val DB_VERSION = 1

    // share single database object
    private lateinit var dbHelper: com.mozhimen.netkfiledownloader.DBManager.DBHelper

    internal class DBHelper(context: Context) :
        SQLiteOpenHelper(
            context,
            com.mozhimen.netkfiledownloader.DBManager.DOWNLOAD_DB_NAME, null,
            com.mozhimen.netkfiledownloader.DBManager.DB_VERSION
        ) {

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(
                """
            CREATE TABLE ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.TABLE_NAME}(
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, 
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_URL} TEXT, 
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_FILENAME} TEXT, 
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_DESTINATION_URI} TEXT, 
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_IGNORE_LOCAL} NUMERIC, 
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_NEED_INSTALL} NUMERIC, 
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_NOTIFICATION_VISIBILITY} INTEGER, 
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_NOTIFICATION_TITLE} TEXT, 
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_NOTIFICATION_CONTENT} TEXT, 
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_TOTAL_BYTES} INTEGER, 
                ${com.mozhimen.netkfiledownloader.DownloadRecord.Companion.COLUMN_STATUS} INTEGER)
        """.trimIndent()
            )
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }

    }


    /**
     * return single database object
     */
    @Synchronized
    fun getDB(context: Context): com.mozhimen.netkfiledownloader.DBManager.DBHelper {
        if (!this::dbHelper.isInitialized) {
            com.mozhimen.netkfiledownloader.DBManager.dbHelper =
                com.mozhimen.netkfiledownloader.DBManager.DBHelper(context)
        }
        return com.mozhimen.netkfiledownloader.DBManager.dbHelper
    }


    /**
     * close database when you no longer need the database
     */
    fun close() {
        if (this::dbHelper.isInitialized) {
            com.mozhimen.netkfiledownloader.DBManager.dbHelper.close()
        }
    }


}