package sts.phucchau.twittersimpleclient.viewholder

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.View
import com.twitter.sdk.android.core.internal.VineCardUtils
import com.twitter.sdk.android.core.models.Tweet
import com.twitter.sdk.android.tweetui.internal.TweetMediaUtils
import sts.phucchau.twittersimpleclient.MainActivity
import sts.phucchau.twittersimpleclient.R
import sts.phucchau.twittersimpleclient.databinding.ItemTweetBinding

class ViewHolderTweet(private val binding: ItemTweetBinding) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.tvContent.movementMethod = LinkMovementMethod.getInstance()

        resizeDrawableTextView()
    }

    fun resizeDrawableTextView() {
        (binding!!.root!!.context as MainActivity).scaleDrawable(binding.tvHeart, R.drawable.heart, 14f, 0.3)
        (binding!!.root!!.context as MainActivity).scaleDrawable(binding.tvRetweet, R.drawable.retweet, 14f, 0.3)
    }

    fun bindData(tweet: Tweet) {
        binding.myTweet = tweet

        binding.tweetMediaView.setTweetMediaEntities(null, null)
        binding.tweetMediaView.visibility = View.GONE

        if (tweet.card != null && VineCardUtils.isVine(tweet.card)) {
            val card = tweet.card
            val imageValue = VineCardUtils.getImageValue(card)
            val playerStreamUrl = VineCardUtils.getStreamUrl(card)
            if (imageValue != null && !TextUtils.isEmpty(playerStreamUrl)) {
                binding.tweetMediaView.setVineCard(tweet)
            }
        } else if (TweetMediaUtils.hasSupportedVideo(tweet)) {
            binding.tweetMediaView.visibility = View.VISIBLE
            val mediaEntity = TweetMediaUtils.getVideoEntity(tweet)
            binding.tweetMediaView.setTweetMediaEntities(tweet, listOf(mediaEntity))
        } else if (TweetMediaUtils.hasPhoto(tweet)) {
            binding.tweetMediaView.visibility = View.VISIBLE
            val mediaEntities = TweetMediaUtils.getPhotoEntities(tweet)
            binding.tweetMediaView.setTweetMediaEntities(tweet, mediaEntities)
        }
    }
}