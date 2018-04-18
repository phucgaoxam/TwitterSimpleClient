package sts.phucchau.twittersimpleclient.helper

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import sts.phucchau.twittersimpleclient.model.TwitterUser

class SharedPreferencesHelper(context: Context) {

    companion object {
        const val TWITTER = "twitter"
        const val FEED = "feed"
        const val USER = "user"

        private var sharedPreferences: SharedPreferences? = null
        private var gson: Gson? = null
        private var sharedPreferencesHelper: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            if (sharedPreferences == null)
                sharedPreferencesHelper = SharedPreferencesHelper(context)
            return sharedPreferencesHelper!!
        }
    }

    init {
        gson = GsonBuilder()
                .setLenient()
                .create()

        sharedPreferences = context.getSharedPreferences(TWITTER, Context.MODE_PRIVATE)
    }

    fun setUserInformation(twitterUser: TwitterUser?) {
        val jsonUser = if (twitterUser != null) gson?.toJson(twitterUser) else "null"

        var editor = sharedPreferences?.edit()
        editor?.putString(USER, jsonUser)
        editor?.apply()
    }

    fun getUserInformation(): TwitterUser? {
        val jsonUser = sharedPreferences?.getString(USER, "null")

        if (jsonUser.equals("null")) return null


        return gson?.fromJson(jsonUser, TwitterUser::class.java)
    }
}