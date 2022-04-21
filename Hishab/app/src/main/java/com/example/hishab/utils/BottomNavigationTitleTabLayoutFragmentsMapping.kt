package com.example.hishab.utils

import com.example.hishab.fragments.*
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.ViewPagerFragmentProxy
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationTitleTabLayoutFragmentsMapping () {
    private val titleTabLayoutFragmentsMapping:HashMap<Int,List<ViewPagerFragmentProxy>> = HashMap<Int,List<ViewPagerFragmentProxy>>()
    init {
        var historyFragmentsList=ArrayList<ViewPagerFragmentProxy>()
        historyFragmentsList.add(ViewPagerFragmentProxy(ShoppingHistoryFragment(),"Shopping History"));
        historyFragmentsList.add(ViewPagerFragmentProxy((PurchaseHistoryFragment()),"Purchase History Flat"));
        historyFragmentsList.add(ViewPagerFragmentProxy((CategoryCostFragment()),"Category Details"));
        titleTabLayoutFragmentsMapping.put(0,historyFragmentsList)

        var budgetFragmentList=ArrayList<ViewPagerFragmentProxy>()
        budgetFragmentList.add(ViewPagerFragmentProxy(BudgetAndSpentFragment(),"Category and Budget"))
        budgetFragmentList.add(ViewPagerFragmentProxy(BudgetAndSpentHistoryFragment(),"Budget History"))
        //budgetFragmentList.add(ViewPagerFragmentProxy(BudgetAndSpentFragment(),"Last month's budget history"))
        titleTabLayoutFragmentsMapping.put(1,budgetFragmentList)

        var categoryProductFragmentList=ArrayList<ViewPagerFragmentProxy>()
        categoryProductFragmentList.add(ViewPagerFragmentProxy(CategoryListFragment(),"Categories"))
        categoryProductFragmentList.add(ViewPagerFragmentProxy(ProductCategoryMappingFragment(),"ProductCategoryMapping"))
        //categoryProductFragmentList.add(ViewPagerFragmentProxy(BudgetAndSpentFragment(),"ProductDetails"))
        titleTabLayoutFragmentsMapping.put(2,categoryProductFragmentList)
    }


    fun getViewPagerFragmentAtPosition(bottomNavigationId:Int,viewPagerPosition:Int):ViewPagerFragmentProxy
    {
        return titleTabLayoutFragmentsMapping[bottomNavigationId]!!.get(viewPagerPosition)
    }
    fun getFragmentListFromBottomnavigationId(bottomNavigationId:Int):List<ViewPagerFragmentProxy>
    {
         if (titleTabLayoutFragmentsMapping[bottomNavigationId]==null)
             return ArrayList<ViewPagerFragmentProxy>()
        return  titleTabLayoutFragmentsMapping[bottomNavigationId]!!
    }
}