package com.example.dragonhelpertext1.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class MainTabLayoutAdapter(fragmentManager: FragmentManager, private val fragmentList: ArrayList<Fragment>, private val listTitle: List<String>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {

        return listTitle[position]
    }
}