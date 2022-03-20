package com.example.hishab.utils

import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.hishab.R
import com.example.hishab.fragments.ViewPagerTabFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationViewWithViewPagerManager private constructor(val navHostFragment: NavHostFragment, private val bottomNavigationView: BottomNavigationView) {
    init {
        setSelectionItemChangeClickListener()

    }

    fun getCurrentlySelectedIndex():Int
    {
        for(i in 0..bottomNavigationView.menu.size())
        {
            if(bottomNavigationView.menu.getItem(i).itemId==bottomNavigationView.selectedItemId)
            {
                return i
            }
        }
        return -1
    }
    companion object
    {
        public var instance:BottomNavigationViewWithViewPagerManager?=null
        fun create(navHostFragment: NavHostFragment, bottomNavigationView: BottomNavigationView):BottomNavigationViewWithViewPagerManager
        {
            if(instance==null)
            {
                instance= BottomNavigationViewWithViewPagerManager(navHostFragment,bottomNavigationView)
            }
            return instance!!
        }

    }

    fun setSelectionItemChangeClickListener()
    {
        bottomNavigationView.setOnItemSelectedListener{
            navHostFragment.navController.navigate(R.id.viewPagerTabFragment)
            return@setOnItemSelectedListener true
        }
    }


}