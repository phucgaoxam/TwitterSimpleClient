package sts.phucchau.twittersimpleclient

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User
import sts.phucchau.twittersimpleclient.base.BaseActivity
import sts.phucchau.twittersimpleclient.helper.SharedPreferencesHelper
import sts.phucchau.twittersimpleclient.helper.TwitterHelper
import sts.phucchau.twittersimpleclient.model.TwitterUser
import sts.phucchau.twittersimpleclient.observer.NotifyTwitterLogin
import sts.phucchau.twittersimpleclient.receiver.ConnectionReceiver

class LoginActivity : BaseActivity() {

    private var connectionReceiver: ConnectionReceiver? = null
    private var tvLogin: TextView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val config = TwitterConfig.Builder(this)
                .logger(DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(TwitterAuthConfig(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build()

        Twitter.initialize(config)

        initUI()
        //initReceiver()
        checkIfLoginBefore()
    }

    override fun onStart() {
        super.onStart()
        progressBar?.visibility = View.GONE
        tvLogin?.visibility = View.VISIBLE
    }

    private fun initUI() {
        tvLogin = findViewById(R.id.tv_login)
        progressBar = findViewById(R.id.pb_loading)

        scaleDrawable(tvLogin!!, R.drawable.twitter, 14f, 0.3)
    }

    private fun checkIfLoginBefore() {
        SharedPreferencesHelper.getInstance(this).getUserInformation()?.let {
            loadMainScreen()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectionReceiver)
    }

    private var twitterCallBack: Callback<TwitterSession>? = null
    private var twitterAuthClient: TwitterAuthClient? = null

    fun login(view: View) {
        if (checkConnection()) {
            progressBar?.visibility = View.VISIBLE
            twitterCallBack = TwitterHelper.getInstance(object : NotifyTwitterLogin {
                override fun notifyTwitterLogin(twitterSession: TwitterSession?, twitterAuthToken: TwitterAuthToken?, twitterAuthClient: TwitterAuthClient?) {
                    tvLogin?.visibility = View.GONE

                    requestEmail(twitterSession!!, twitterAuthClient!!, twitterAuthToken!!.token, twitterAuthToken.secret)
                }
            }).getTwitterCallBack()

            twitterAuthClient = TwitterAuthClient()
            twitterAuthClient!!.authorize(this@LoginActivity, twitterCallBack)
        } else {
            Toast.makeText(this, "Please connect to the internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestEmail(twitterSession: TwitterSession, client: TwitterAuthClient, token: String, secret: String) {
        client.requestEmail(twitterSession, object : Callback<String>() {
            override fun success(result: Result<String>?) {
                val user = TwitterCore.getInstance()
                        .apiClient
                        .accountService
                        .verifyCredentials(false, false, true)

                user.enqueue(object : Callback<User>() {
                    override fun success(result: Result<User>?) {
                        val userT = result!!.data
                        SharedPreferencesHelper.getInstance(this@LoginActivity).setUserInformation(TwitterUser(userT.id, token, secret,
                                userT.name, userT.screenName, userT.email, userT.profileImageUrl.replace("_normal", ""), userT.location, userT.friendsCount, userT.followersCount))

                        loadMainScreen()
                        progressBar?.visibility = View.GONE

                    }

                    override fun failure(exception: TwitterException?) {
                        Toast.makeText(this@LoginActivity, exception.toString(), Toast.LENGTH_SHORT).show()
                        progressBar?.visibility = View.GONE
                    }

                })
            }

            override fun failure(exception: TwitterException?) {
                Toast.makeText(this@LoginActivity, exception.toString(), Toast.LENGTH_SHORT).show()
                progressBar?.visibility = View.GONE
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        twitterAuthClient?.let {
            twitterAuthClient!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun loadMainScreen() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}