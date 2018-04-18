package sts.phucchau.twittersimpleclient.base

import android.graphics.drawable.ScaleDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import sts.phucchau.twittersimpleclient.fragment.FragmentContainer

open class BaseActivity : AppCompatActivity() {

    fun scaleDrawable(tv: TextView, drawable: Int, size: Float, percent: Double) {
        if (Build.VERSION.SDK_INT < 23) {
            val dw = ContextCompat.getDrawable(this, drawable)
            dw!!.setBounds(0, 0, (dw.intrinsicWidth * percent).toInt(), (dw.intrinsicHeight * percent).toInt())
            val sd = ScaleDrawable(dw, 0, size, size)
            tv.setCompoundDrawables(sd.drawable, null, null, null)
        }
    }

    fun createContainerFragment(type: Int): FragmentContainer {
        val bundle = Bundle()
        val containerFragment = FragmentContainer()
        bundle.putInt("type", type)
        containerFragment.arguments = bundle
        return containerFragment
    }
}