package sts.phucchau.twittersimpleclient

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Window
import sts.phucchau.twittersimpleclient.adapter.AdapterViewPager
import sts.phucchau.twittersimpleclient.base.BaseActivity
import sts.phucchau.twittersimpleclient.fragment.FragmentContainer

class MainActivity : BaseActivity() {

    companion object {
        const val TIMELINE = 1
        const val PROFILE = 2
        const val USER_TIMELINE = 3
        const val USER_LIKE_TWEET = 4
    }

    private var fragmentProfile: FragmentContainer? = null
    private var fragmentTimeline: FragmentContainer? = null
    private var tabLayout: TabLayout? = null
    private var adapter: AdapterViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        initUI()
    }

    fun initUI() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        setUpViewPager(viewPager)

        tabLayout = findViewById(R.id.tab_layout)
        tabLayout?.setupWithViewPager(viewPager)
    }

    fun setUpViewPager(viewPager: ViewPager) {
        adapter = AdapterViewPager(supportFragmentManager)

        fragmentTimeline = createContainerFragment(TIMELINE)

        adapter?.addFragment(fragmentTimeline, "Timeline")
        adapter?.addFragment(createContainerFragment(PROFILE), "Profile")

        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        sts.phucchau.twittersimpleclient.helper.SharedPreferencesHelper.getInstance(this).setUserInformation(null)
        finish()
    }
}
