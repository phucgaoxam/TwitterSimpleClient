package sts.phucchau.twittersimpleclient.helper

import android.content.Context
import com.twitter.sdk.android.core.models.Tweet
import sts.phucchau.twittersimpleclient.sqlite.TableTimeLineTweet
import sts.phucchau.twittersimpleclient.sqlite.TableUserLikeTweet
import sts.phucchau.twittersimpleclient.sqlite.TableUserTweet

class TableTweetHelper(context: Context) {
    companion object {
        private var tableTweetHelper: TableTweetHelper? = null
        private var tableTimeLineTweet: TableTimeLineTweet? = null
        private var tableUserLikeTweet: TableUserLikeTweet? = null
        private var tableUserTweet: TableUserTweet? = null

        fun getInstance(context: Context): TableTweetHelper {
            if (tableTimeLineTweet == null || tableUserLikeTweet == null || tableUserTweet == null) {
                tableTweetHelper = TableTweetHelper(context)
            }
            return tableTweetHelper!!
        }
    }

    init {
        tableTimeLineTweet = TableTimeLineTweet(context)
        tableUserLikeTweet = TableUserLikeTweet(context)
        tableUserTweet = TableUserTweet(context)
    }

    fun insertTimelineTweet(tweet: Tweet) {
        tableTimeLineTweet!!.insertTweet(tweet)
    }

    fun deleteAllTimeLine() {
        tableTimeLineTweet!!.deleteAll()
    }

    fun getTimelineTweetList(): List<Tweet> {
        return tableTimeLineTweet!!.loadListTweet()
    }

    fun insertUserTweet(tweet: Tweet) {
        tableUserTweet!!.insertTweet(tweet)
    }

    fun deleteAllUserTimeline() {
        tableUserTweet!!.deleteAll()
    }

    fun getUserTweetList(): List<Tweet> {
        return tableUserTweet!!.loadListTweet()
    }

    fun insertUserLikeTweet(tweet: Tweet) {
        tableUserLikeTweet!!.insertTweet(tweet)
    }

    fun deleteAllUserLike() {
        tableUserLikeTweet!!.deleteAll()
    }

    fun getUserLikeList(): List<Tweet> {
        return tableUserLikeTweet!!.loadListTweet()
    }
}