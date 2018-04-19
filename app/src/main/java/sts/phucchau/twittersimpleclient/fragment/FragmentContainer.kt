package sts.phucchau.twittersimpleclient.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sts.phucchau.twittersimpleclient.MainActivity.Companion.PROFILE
import sts.phucchau.twittersimpleclient.MainActivity.Companion.TIMELINE
import sts.phucchau.twittersimpleclient.MainActivity.Companion.USER_LIKE_TWEET
import sts.phucchau.twittersimpleclient.MainActivity.Companion.USER_TIMELINE
import sts.phucchau.twittersimpleclient.R
import sts.phucchau.twittersimpleclient.base.BaseFragment
import sts.phucchau.twittersimpleclient.fragment.profile.FragmentProfile
import sts.phucchau.twittersimpleclient.fragment.profile.FragmentUserLikeTweet
import sts.phucchau.twittersimpleclient.fragment.profile.FragmentUserTimeline

class FragmentContainer : BaseFragment() {

    private var typeFragment = 0
    private var fragmentTimeline: FragmentTimeline? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        typeFragment = arguments!!.getInt("type", 0)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_container, container, false)

        when (typeFragment) {
            TIMELINE -> {
                fragmentTimeline = FragmentTimeline()
                addWithChildFragmentManager(R.id.fragment_layout, fragmentTimeline!!)
            }
            PROFILE -> addWithChildFragmentManager(R.id.fragment_layout, FragmentProfile())
            USER_TIMELINE -> addWithChildFragmentManager(R.id.fragment_layout, FragmentUserTimeline())
            USER_LIKE_TWEET -> addWithChildFragmentManager(R.id.fragment_layout, FragmentUserLikeTweet())
        }

        return root
    }

    fun getFragmentTimeLine(): FragmentTimeline = fragmentTimeline!!
}