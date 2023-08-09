package com.mozhimen.netk_file

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
    private lateinit var dbHelper: com.mozhimen.netk_file.DBManager.DBHelper

    internal class DBHelper(context: Context) :
        SQLiteOpenHelper(
            context,
            com.mozhimen.netk_file.DBManager.DOWNLOAD_DB_NAME, null,
            com.mozhimen.netk_file.DBManager.DB_VERSION
        ) {

        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL(
                """
            CREATE TABLE ${com.mozhimen.netk_file.DownloadRecord.Companion.TABLE_NAME}(
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, 
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_URL} TEXT, 
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_FILENAME} TEXT, 
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_DESTINATION_URI} TEXT, 
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_IGNORE_LOCAL} NUMERIC, 
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_NEED_INSTALL} NUMERIC, 
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_NOTIFICATION_VISIBILITY} INTEGER, 
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_NOTIFICATION_TITLE} TEXT, 
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_NOTIFICATION_CONTENT} TEXT, 
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_TOTAL_BYTES} INTEGER, 
                ${com.mozhimen.netk_file.DownloadRecord.Companion.COLUMN_STATUS} INTEGER)
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
    fun getDB(context: Context): com.mozhimen.netk_file.DBManager.DBHelper {
        if (!this::dbHelper.isInitialized) {
            com.mozhimen.netk_file.DBManager.dbHelper =
                com.mozhimen.netk_file.DBManager.DBHelper(context)
        }
        return com.mozhimen.netk_file.DBManager.dbHelper
    }


    /**
     * close database when you no longer need the database
     */
    fun close() {
        if (this::dbHelper.isInitialized) {
            com.mozhimen.netk_file.DBManager.dbHelper.close()
        }
    }


}