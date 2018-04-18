package sts.phucchau.twittersimpleclient.base

import android.os.Bundle
import android.support.v4.app.Fragment
import sts.phucchau.twittersimpleclient.R
import sts.phucchau.twittersimpleclient.fragment.FragmentContainer

open class BaseFragment : Fragment() {

    fun popBackStack(): Boolean {
        if (childFragmentManager.backStackEntryCount > 1) {
            return true
        }
        return false
    }

    fun addWithChildFragmentManager(id: Int, fragment: BaseFragment) {
        val fragmentTransaction = childFragmentManager!!.beginTransaction()
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
}