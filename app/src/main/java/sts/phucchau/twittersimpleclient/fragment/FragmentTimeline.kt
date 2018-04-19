package sts.phucchau.twittersimpleclient.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetcomposer.ComposerActivity
import sts.phucchau.twittersimpleclient.R
import sts.phucchau.twittersimpleclient.base.BaseFragment
import sts.phucchau.twittersimpleclient.helper.SharedPreferencesHelper
import sts.phucchau.twittersimpleclient.helper.TableTweetHelper

class FragmentTimeline : BaseFragment() {

    companion object {
        const val REQUEST_CODE_POST_TWEET = 100
    }

    private var rvTweet: RecyclerView? = null
    private var tweetList: MutableList<Tweet> = arrayListOf()
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var swipeListener: SwipeRefreshLayout.OnRefreshListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_timeline, container, false)

        initUI(root)
        addControl()

        if (checkConnection()) loadTweetWhenConnected()
        else loadTweetWhenOffline()
        return root
    }

    private fun initUI(root: View) {
        rvTweet = root.findViewById(R.id.rv_tweet)
        swipeRefreshLayout = root.findViewById(R.id.refresh_layout)

        rvTweet!!.layoutManager = LinearLayoutManager(context!!)
        rvTweet!!.isNestedScrollingEnabled = true

        swipeListener = SwipeRefreshLayout.OnRefreshListener {
            if (checkConnection())
                loadTweetWhenConnected()
            else loadTweetWhenOffline()
        }
    }

    private fun addControl() {
        swipeRefreshLayout!!.setOnRefreshListener { swipeListener }
    }

    private fun loadTweetWhenConnected() {
        Toast.makeText(context, "Load online", Toast.LENGTH_SHORT).show()
        val statusesService = TwitterCore.getInstance().apiClient.statusesService
        val timelineService = statusesService.homeTimeline(200, null, null, null, null, null, null)
        timelineService.enqueue(object : Callback<MutableList<Tweet>>() {
            override fun success(result: Result<MutableList<Tweet>>?) {
                TableTweetHelper.getInstance(context!!).deleteAllTimeLine()
                tweetList = arrayListOf()
                tweetList.addAll(result!!.data)

                Thread(Runnable {
                    for (t in tweetList) {
                        TableTweetHelper.getInstance(context!!).insertTimelineTweet(t)
                    }
                }).start()

                initRecyclerView(rvTweet!!, tweetList, context!!)
            }

            override fun failure(exception: TwitterException?) {
                Toast.makeText(context!!, exception.toString(), Toast.LENGTH_SHORT).show()
                loadTweetWhenOffline()
            }
        })
    }

    private fun loadTweetWhenOffline() {
        Toast.makeText(context, "Load offline", Toast.LENGTH_SHORT).show()
        tweetList = arrayListOf()
        tweetList = TableTweetHelper.getInstance(context!!).getTimelineTweetList().toMutableList()
        initRecyclerView(rvTweet!!, tweetList, context!!)
    }

    fun postTweet() {
        val user = SharedPreferencesHelper.getInstance(context!!).getUserInformation()
        val twitterSession = TwitterSession(TwitterAuthToken(user!!.token, user.secret), user.userId, user.name)
        val intent = ComposerActivity.Builder(context!!)
                .session(twitterSession)
                .darkTheme()
                .createIntent()

        startActivityForResult(intent, REQUEST_CODE_POST_TWEET)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (REQUEST_CODE_POST_TWEET == requestCode) {
            swipeRefreshLayout?.postDelayed({
                Toast.makeText(context!!, "Wait a minute", Toast.LENGTH_SHORT).show()
                swipeRefreshLayout?.isRefreshing = true
                if (checkConnection()) loadTweetWhenConnected()
                else loadTweetWhenOffline()
                swipeRefreshLayout?.isRefreshing = false
            }, 2000)
        }
    }
}