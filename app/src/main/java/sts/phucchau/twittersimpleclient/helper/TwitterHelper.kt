package sts.phucchau.twittersimpleclient.helper

import android.util.Log
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.TwitterSession
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import sts.phucchau.twittersimpleclient.observer.NotifyTwitterLogin

class TwitterHelper(notifyTwitterLogin: NotifyTwitterLogin) {

    companion object {
        private var twitterHelper: TwitterHelper? = null

        private var twitterCallback: Callback<TwitterSession>? = null

        fun getInstance(notifyTwitterLogin: NotifyTwitterLogin): TwitterHelper {
            if (twitterCallback == null) {
                twitterHelper = TwitterHelper(notifyTwitterLogin)
            }
            return twitterHelper!!
        }
    }

    init {
        initTwitterCallbac(notifyTwitterLogin)
    }

    private fun initTwitterCallbac(notifyTwitterLogin: NotifyTwitterLogin) {
        twitterCallback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>?) {
                val twitterSession = result?.data

                val twitterAuthToken = twitterSession?.authToken

                val twitterAuthClient = TwitterAuthClient()

                notifyTwitterLogin.notifyTwitterLogin(twitterSession, twitterAuthToken, twitterAuthClient)
            }

            override fun failure(exception: TwitterException?) {
                Log.e("errorLogin", exception?.message)
            }
        }
    }
}