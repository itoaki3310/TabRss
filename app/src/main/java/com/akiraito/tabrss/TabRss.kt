package com.akiraito.tabrss

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.activity_tab_rss.*

class TabRss : AppCompatActivity(), ViewPager.OnPageChangeListener, PageFragment.OnFragmentInteractionListener {
    override fun onPageScrollStateChanged(p0: Int) {}
    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
    override fun onPageSelected(p0: Int) {}
    override fun onFragmentInteraction(uri: Uri) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_rss)

        pager.addOnPageChangeListener(this)
        setTabLayout()
    }

    fun setTabLayout(){
        val adapter = TabAdapter(supportFragmentManager, this)
        pager.adapter = adapter

        tabs.setupWithViewPager(pager)

        for(i in 0 until adapter.count) {
            val tab = tabs.getTabAt(i)!!
            tab.customView = adapter.getTabView(tabs, i)
        }
    }
}
