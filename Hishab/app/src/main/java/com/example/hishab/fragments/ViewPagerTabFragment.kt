package com.example.hishab.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.hishab.R
import com.example.hishab.adapter.ViewPagerAdapter
import com.example.hishab.databinding.FragmentViewPagerTabBinding
import com.example.hishab.interfaces.IViewPagerFragmentChanged
import com.example.hishab.interfaces.IViewPagerSwipeListener
import com.example.hishab.models.ViewPagerFragmentProxy
import com.example.hishab.utils.BottomNavigationTitleTabLayoutFragmentsMapping
import com.example.hishab.utils.BottomNavigationViewWithViewPagerManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.Observable.just
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class ViewPagerTabFragment : Fragment() {

    lateinit var binding: FragmentViewPagerTabBinding
    val bottomNavigationTitleTabLayoutFragmentsMapping =
        BottomNavigationTitleTabLayoutFragmentsMapping()
    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager_tab, container, false)
        setUpTabLayoutAndViewPager()
        navController = findNavController()
        return binding.root
    }

    private fun setUpTabLayoutAndViewPager() {
        // binding.viewPager.isUserInputEnabled = false
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                var fragment =
                    bottomNavigationTitleTabLayoutFragmentsMapping.getViewPagerFragmentAtPosition(
                        getSelectedItemId(),
                        position
                    ).fragment
                if (fragment is IViewPagerFragmentChanged) {
                    fragment.onFragmentChanged()
                }
                if (/*fragment is IViewPagerSwipeListener*/ false) {

                    io.reactivex.Observable.just(1).delay(1000, TimeUnit.MILLISECONDS).subscribe {
                        var listener = fragment as IViewPagerSwipeListener
                        listener.onSwipedRightOrLeft.subscribe {
                            if (it.first < 0 && binding.viewPager.currentItem != 0) {
                                binding.viewPager.currentItem--
                            } else if (it.first > 0)
                                binding.viewPager.currentItem++
                        }
                    }
                }
            }
        })
        binding.viewPager.adapter = ViewPagerAdapter(
            requireActivity(),
            getSelectedItemId(),
            bottomNavigationTitleTabLayoutFragmentsMapping
        )
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            var viewPagerFragmentProxy =
                bottomNavigationTitleTabLayoutFragmentsMapping.getViewPagerFragmentAtPosition(
                    getSelectedItemId(),
                    position
                )
            tab.text = viewPagerFragmentProxy.title
        }.attach()

    }


    private fun getSelectedItemId(): Int {
        if (BottomNavigationViewWithViewPagerManager.instance != null)
            return BottomNavigationViewWithViewPagerManager.instance!!.getCurrentlySelectedIndex()
        return 0
    }

}