package com.example.hishab.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.hishab.R
import com.example.hishab.adapter.ViewPagerAdapter
import com.example.hishab.databinding.FragmentViewPagerTabBinding
import com.example.hishab.models.ViewPagerFragmentProxy
import com.example.hishab.utils.BottomNavigationTitleTabLayoutFragmentsMapping
import com.example.hishab.utils.BottomNavigationViewWithViewPagerManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ViewPagerTabFragment : Fragment() {

    lateinit var binding: FragmentViewPagerTabBinding
    val bottomNavigationTitleTabLayoutFragmentsMapping=BottomNavigationTitleTabLayoutFragmentsMapping()
    var viewPagerFragmentList:List<ViewPagerFragmentProxy>?=null
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_view_pager_tab,container,false)
        setUpTabLayoutAndViewPager()
        navController=findNavController()
        return binding.root
    }

    private fun setUpTabLayoutAndViewPager() {
        binding.viewPager.adapter=ViewPagerAdapter(requireActivity(),getSelectedItemId(),bottomNavigationTitleTabLayoutFragmentsMapping)
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text=bottomNavigationTitleTabLayoutFragmentsMapping.getViewPagerFragmentAtPosition(getSelectedItemId(),position).title
        }.attach()

    }


    private fun getSelectedItemId():Int {
        if(BottomNavigationViewWithViewPagerManager.instance!=null)
            return BottomNavigationViewWithViewPagerManager.instance!!.getCurrentlySelectedIndex()
        return 0
    }

}