package sts.phucchau.twittersimpleclient.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.twitter.sdk.android.core.models.Tweet
import sts.phucchau.twittersimpleclient.R
import sts.phucchau.twittersimpleclient.adapter.AdapterTweet
import sts.phucchau.twittersimpleclient.fragment.FragmentContainer
import sts.phucchau.twittersimpleclient.receiver.ConnectionReceiver
import sts.phucchau.twittersimpleclient.view.OnLoadMoreListener

open class BaseFragment : Fragment() {
    var connectionReceiver: ConnectionReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //initConnectionReceiver()
    }

    fun popBackStack(): Boolean {
        if (childFragmentManager.backStackEntryCount > 1) {
            return true
        }
        return false
    }

    fun addWithChildFragmentManager(id: Int, fragment: BaseFragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.animator.slide_up_in, R.animator.slide_down_in, R.animator.slide_up_out, R.animator.slide_down_out)
                .add(id, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
    }

    fun addWithFragmentManager(id: Int, fragment: BaseFragment) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.animator.slide_up_in, R.animator.slide_down_in, R.animator.slide_up_out, R.animator.slide_down_out)
                .add(id, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
    }

    fun createFragmentContainer(type: Int): FragmentContainer {
        val bundle = Bundle()
        val container = FragmentContainer()
        bundle.putInt("type", type)
        container.arguments = bundle
        return container
    }

    fun initRecyclerView(rv: RecyclerView, tweetList: MutableList<Tweet>, context: Context) {
        val showTweetList: MutableList<Tweet?> = arrayListOf()
        try {
            for (i in 0..AdapterTweet.MAX_COUNT_PER_PAGE) {
                showTweetList.add(tweetList[i])
            }
        } catch (ex: IndexOutOfBoundsException) {

        }

        rv.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterTweet(showTweetList, rv)
        rv.adapter = adapter

        adapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {
                showTweetList.add(null)
                adapter.notifyItemInserted(showTweetList.size - 1)

                Handler().postDelayed({
                    showTweetList.removeAt(showTweetList.size - 1)
                    adapter.notifyItemInserted(showTweetList.size)

                    val index = showTweetList.size
                    var end = index + AdapterTweet.MAX_COUNT_PER_PAGE
                    if (end >= tweetList.size) {
                        end = tweetList.size
                    }

                    try {
                        for (i in index..end) {
                            showTweetList.add(tweetList[i])
                        }
                    } catch (ex: IndexOutOfBoundsException) {

                    }
                    adapter.notifyDataSetChanged()
                    adapter.setLoaded()
                }, 2000)
            }
        })
    }

    fun checkConnection(): Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    /*private fun initConnectionReceiver() {
        connectionReceiver = ConnectionReceiver(object : ConnectionReceiver.ObserverConnection {
            override fun onChangeConnection(isConnected: Boolean) {
                this@BaseFragment.isConnected = isConnected
            }
        })

        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context?.registerReceiver(connectionReceiver, intentFilter)
    }*/

    override fun onDestroy() {
        super.onDestroy()
        //context!!.unregisterReceiver(connectionReceiver)
    }
}