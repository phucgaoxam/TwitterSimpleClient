package sts.phucchau.twittersimpleclient.fragment.profile

import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import sts.phucchau.twittersimpleclient.R
import sts.phucchau.twittersimpleclient.base.BaseFragment
import sts.phucchau.twittersimpleclient.databinding.FragmentProfileBinding

class FragmentProfile : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        return binding.root
    }
}

@BindingAdapter("app:profileSrc")
fun ImageView.setProfileImage(src: String) {
    Picasso.get().load(src)
            .centerCrop()
            .fit()
            .into(this)
}
