package sts.phucchau.twittersimpleclient.fragment.profile

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import sts.phucchau.twittersimpleclient.R
import sts.phucchau.twittersimpleclient.base.BaseFragment
import sts.phucchau.twittersimpleclient.helper.SharedPreferencesHelper
import sts.phucchau.twittersimpleclient.helper.TableTweetHelper

class FragmentUserLikeTweet : BaseFragment() {

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
        val twitterUser = SharedPreferencesHelper.getInstance(context!!).getUserInformation()
        Toast.makeText(context, "Load online", Toast.LENGTH_SHORT).show()
        val favoriteService = TwitterCore.getInstance().apiClient.favoriteService
        val favoriteTimeline = favoriteService.list(twitterUser!!.userId, twitterUser.screenName, 200, null, null, null)
        favoriteTimeline.enqueue(object : Callback<MutableList<Tweet>>() {
            override fun success(result: Result<MutableList<Tweet>>?) {
                TableTweetHelper.getInstance(context!!).deleteAllUserLike()
                tweetList = arrayListOf()
                tweetList.addAll(result!!.data)

                Thread(Runnable {
                    for (t in tweetList) {
                        TableTweetHelper.getInstance(context!!).insertUserLikeTweet(t)
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
        tweetList = TableTweetHelper.getInstance(context!!).getUserLikeList().toMutableList()
        initRecyclerView(rvTweet!!, tweetList, context!!)
    }
}