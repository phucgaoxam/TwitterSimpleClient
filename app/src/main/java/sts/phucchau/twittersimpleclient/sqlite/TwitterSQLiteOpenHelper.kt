package sts.phucchau.twittersimpleclient.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TwitterSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, "twitter", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(TableUserTweet.CREATE_TABLE)
        db.execSQL(TableTimeLineTweet.CREATE_TABLE)
        db.execSQL(TableUserLikeTweet.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}