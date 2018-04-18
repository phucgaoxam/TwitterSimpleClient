package sts.phucchau.twittersimpleclient.viewholder

import android.support.v7.widget.RecyclerView
import sts.phucchau.twittersimpleclient.databinding.ItemTweetLoadingBinding

class ViewHolderLoading(private val binding: ItemTweetLoadingBinding): RecyclerView.ViewHolder(binding.root) {

    init {

    }

    fun setProgressBar() {
        binding.pbLoading.isIndeterminate = true
    }
}