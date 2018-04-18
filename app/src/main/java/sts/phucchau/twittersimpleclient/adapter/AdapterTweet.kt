package sts.phucchau.twittersimpleclient.adapter

import android.databinding.BindingAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.twitter.sdk.android.core.models.Tweet
import sts.phucchau.twittersimpleclient.databinding.ItemTweetBinding
import sts.phucchau.twittersimpleclient.databinding.ItemTweetLoadingBinding
import sts.phucchau.twittersimpleclient.view.OnLoadMoreListener
import sts.phucchau.twittersimpleclient.viewholder.ViewHolderLoading
import sts.phucchau.twittersimpleclient.viewholder.ViewHolderTweet

class AdapterTweet(private var tweetList: MutableList<Tweet?>, rv: RecyclerView) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val MAX_COUNT_PER_PAGE = 20

        fun formatTimeStamp(tv: TextView, createdAt: String?): String {;
            return if (createdAt != null && TweetDateUtils.isValidTimestamp(createdAt)) {
                val createdAtTimestamp = TweetDateUtils.apiTimeToLong(createdAt)
                val timestamp = TweetDateUtils.getRelativeTimeString(tv.context.resources,
                        System.currentTimeMillis(),
                        createdAtTimestamp)
                TweetDateUtils.dotPrefix(timestamp)
            } else {
                ""
            }
        }
    }

    private val TYPE_LOADING = 1
    private val TYPE_ITEM = 0

    private var lastVisibileItem: Int = 0
    private var totalItemCount: Int = 0
    private var isLoading: Boolean = false
    private val visibleThreshold = 5

    private var onLoadMoreListener: OnLoadMoreListener? = null

    init {
        val linearLayoutManager = rv.layoutManager as LinearLayoutManager

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = linearLayoutManager.itemCount
                lastVisibileItem = linearLayoutManager.findLastVisibleItemPosition()

                if (lastVisibileItem <= 1) {
                    return
                }

                if (!isLoading && totalItemCount <= (lastVisibileItem + visibleThreshold)) {
                    onLoadMoreListener?.let {
                        onLoadMoreListener!!.onLoadMore()
                    }
                    isLoading = true
                }
            }
        })
    }

    override fun getItemViewType(position: Int): Int = if (tweetList[position] == null) TYPE_LOADING else TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_LOADING) {
            val binding = ItemTweetLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderLoading(binding)
        } else {
            val binding = ItemTweetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolderTweet(binding)
        }
    }

    override fun getItemCount(): Int = tweetList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == TYPE_ITEM) {
            (holder as ViewHolderTweet).bindData(tweetList[holder.adapterPosition]!!)
        } else {
            (holder as ViewHolderLoading).setProgressBar()
        }
    }

    fun setLoaded() {
        isLoading = false
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }


}

@BindingAdapter("android:src")
fun ImageView.setImageSrc(src: String) {
    Picasso.get()
            .load(src)
            .fit()
            .centerCrop()
            .into(this)
}

@BindingAdapter("app:myText")
fun TextView.setTimeStamp(time: String) {
    this.text = AdapterTweet.formatTimeStamp(this, time)
}

@BindingAdapter("app:visibility")
fun ImageView.setVisibility(isVerified: Boolean) {
    this.visibility = if (isVerified) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("app:screenName")
fun TextView.setScreenText(screenName: String) {
    this.text = "@$screenName"
}

