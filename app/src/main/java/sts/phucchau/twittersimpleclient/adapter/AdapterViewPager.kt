package sts.phucchau.twittersimpleclient.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import sts.phucchau.twittersimpleclient.base.BaseFragment
import sts.phucchau.twittersimpleclient.fragment.FragmentContainer

class AdapterViewPager(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragmentList: MutableList<FragmentContainer?> = arrayListOf()
    private var titleList: MutableList<String> = arrayListOf()

    override fun getItem(position: Int): BaseFragment = fragmentList[position] as BaseFragment

    override fun getCount(): Int = fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = titleList[position]

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

    }

    fun addFragment(fragment: FragmentContainer?, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}