package com.audio.view.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class LocalFragmentAdapt : FragmentPagerAdapter {
    var fragmentList : MutableList<Fragment>
    val fragmentTableNames : MutableList<CharSequence>
    constructor(fManager: FragmentManager) : super(fManager) {
        fragmentList = createFragmentItems()
        fragmentTableNames = createTableNames()
    }
    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return fragmentTableNames[position]
    }
}

