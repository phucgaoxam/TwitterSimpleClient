package sts.phucchau.twittersimpleclient.fragment.profile

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import sts.phucchau.twittersimpleclient.MainActivity.Companion.USER_LIKE_TWEET
import sts.phucchau.twittersimpleclient.MainActivity.Companion.USER_TIMELINE
import sts.phucchau.twittersimpleclient.R
import sts.phucchau.twittersimpleclient.adapter.AdapterViewPager
import sts.phucchau.twittersimpleclient.base.BaseFragment
import sts.phucchau.twittersimpleclient.databinding.FragmentProfileBinding
import sts.phucchau.twittersimpleclient.helper.SharedPreferencesHelper
import sts.phucchau.twittersimpleclient.model.TwitterUser

class FragmentProfile : BaseFragment() {

    private var binding: FragmentProfileBinding? = null
    private var twitterUser: TwitterUser? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        twitterUser = SharedPreferencesHelper.getInstance(context!!).getUserInformation()
        binding?.user = twitterUser
        setUpViewPager()

        return binding?.root
    }

    private fun setUpViewPager() {
        val adapter = AdapterViewPager(childFragmentManager)
        adapter.addFragment(createFragmentContainer(USER_TIMELINE), "User timeline")
        adapter.addFragment(createFragmentContainer(USER_LIKE_TWEET), "User like tweet")

        binding!!.viewPagerProfile.adapter = adapter
        binding!!.tabLayoutProfile.setupWithViewPager(binding!!.viewPagerProfile)
    }
}

@BindingAdapter("app:profileSrc")
fun ImageView.setProfileImage(src: String) {
    Picasso.with(this.context).load(src)
            .centerCrop()
            .fit()
            .into(this)
}
