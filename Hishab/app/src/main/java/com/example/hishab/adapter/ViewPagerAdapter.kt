package com.example.hishab.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hishab.MainActivity
import com.example.hishab.interfaces.IViewPagerSwipeListener
import com.example.hishab.utils.BottomNavigationTitleTabLayoutFragmentsMapping

class ViewPagerAdapter(
    val fragmentActivity: FragmentActivity,
    val selectedBottomNavigationId: Int,
    val bottomNavigationTitleTabLayoutFragmentsMapping: BottomNavigationTitleTabLayoutFragmentsMapping
): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        var fragmentList= bottomNavigationTitleTabLayoutFragmentsMapping.getFragmentListFromBottomnavigationId(selectedBottomNavigationId)
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return bottomNavigationTitleTabLayoutFragmentsMapping.getViewPagerFragmentAtPosition(selectedBottomNavigationId,position).fragment

    }

}



