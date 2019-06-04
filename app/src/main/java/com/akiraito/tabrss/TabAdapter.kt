package com.akiraito.tabrss

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

class TabAdapter(fragmentManager: FragmentManager, private val context: Context): FragmentStatePagerAdapter(fragmentManager){
    private val pageTitle = arrayOf("page1","2","3")





    override fun getItem(psition: Int): Fragment {
        //新しいフラグメントを生成して返す
        return PageFragment.newInstance(psition + 1)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return super.getPageTitle(position)
    }

    override fun getCount(): Int {
        return pageTitle.size
    }

    fun getTabView(tabLayout: TabLayout, position: Int): View {
        //copy layout
        val view = LayoutInflater.from(this.context).inflate(R.layout.tab_item, tabLayout, false)
        //title
        val tab = view.findViewById<TextView>(R.id.tab_item_text)
        tab.text = pageTitle[position]
        //icon
        val tabIcon = view.findViewById<ImageView>(R.id.tab_item_image)
        tabIcon.setImageResource(R.mipmap.ic_launcher)

        return view
    }




}