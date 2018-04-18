package sts.phucchau.twittersimpleclient.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import com.twitter.sdk.android.core.models.Tweet

class TableUserLikeTweet(context: Context) {

    companion object {
        const val TABLE_NAME = "user_like_tweet"
        const val COLUMN_JSON_TWEET = "json"
        const val CREATE_TABLE = "create table $TABLE_NAME($COLUMN_JSON_TWEET text);"
    }

    private val gson = Gson()
    private var database: SQLiteDatabase? = null

    init {
        database = TwitterSQLiteOpenHelper(context).writableDatabase
    }

    fun insertTweet(tweet: Tweet): Long {
        var contentValues = ContentValues()
        contentValues.put(COLUMN_JSON_TWEET, gson.toJson(tweet))
        return database!!.insert(TABLE_NAME, null, contentValues)
    }

    fun deleteAll(): Int = database!!.delete(TABLE_NAME, null, null)

    fun loadListTweet(): List<Tweet> {
        val tweetList: MutableList<Tweet> = arrayListOf()

        val cursor = database!!.query(TABLE_NAME, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            tweetList.add(gson.fromJson(cursor.getString(cursor.getColumnIndex(COLUMN_JSON_TWEET)), Tweet::class.java))
        }
        return tweetList
    }
}