package com.audio.view.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FragmentsAdapt : FragmentPagerAdapter {
    var fragmentList: MutableList<Fragment>
    val fragmentTableNames: MutableList<CharSequence>

    constructor(fManager: FragmentManager, fragments: MutableList<Fragment>,
                names: MutableList<CharSequence>) : super(fManager) {
        fragmentList = fragments
        fragmentTableNames = names
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

