package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.hishab.R
import com.example.hishab.adapter.GenericAdapterForTwoViewTypes
import com.example.hishab.databinding.FragmentBudgetAndSpentHistoryBinding
import com.example.hishab.databinding.LayoutBudgetSpentHistoryBinding
import com.example.hishab.databinding.LayoutMonthItemBinding
import com.example.hishab.interfaces.IViewPagerSwipeListener
import com.example.hishab.models.MonthlySpentModel
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.BudgetAndSpentHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.function.Predicate


@AndroidEntryPoint
class BudgetAndSpentHistoryFragment : Fragment(),IViewPagerSwipeListener {
    lateinit var binding:FragmentBudgetAndSpentHistoryBinding
    private val viewModel: BudgetAndSpentHistoryViewModel by viewModels()
    lateinit var genericAdapterForTwoViewTypes: GenericAdapterForTwoViewTypes<MonthlySpentModel,String,LayoutBudgetSpentHistoryBinding,LayoutMonthItemBinding>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_budget_and_spent_history,container,false)
        setUpRecyclerView()
        fetchBudgetHistory()

        return binding.root
    }

    private fun setUpRecyclerView() {
        var predicate=Predicate<Int>{genericAdapterForTwoViewTypes.getItemAt(it) is MonthlySpentModel}
        genericAdapterForTwoViewTypes= GenericAdapterForTwoViewTypes.Create(R.layout.layout_budget_spent_history,R.layout.layout_month_item,predicate)
        binding.rvBudgetHistory.adapter=genericAdapterForTwoViewTypes
        genericAdapterForTwoViewTypes.firstViewInlateLiveData.observe(viewLifecycleOwner){
            it.second.monthlySpentModel=it.first
        }
        genericAdapterForTwoViewTypes.firstviewClickLiveData.observe(viewLifecycleOwner){
            var direction=ViewPagerTabFragmentDirections.actionViewPagerTabFragmentToBudgetAndSpentFragment()
            direction.year=it.getYear()
            direction.month=it.getMonth()
            findNavController().navigate(direction)
        }
        genericAdapterForTwoViewTypes.secondViewInlateLiveData.observe(viewLifecycleOwner){
            it.second.month1=it.first
        }
        onSwipedRightOrLeft=Util.getViewSwipeObservable(binding.rvBudgetHistory)
    }

    private fun fetchBudgetHistory() {
        viewModel.getPreviousMonthsBudgetAndSpentHistory(Util.getCurrentYear(),Util.getCurrentMonth()+1).subscribeOn(
            Schedulers.io()).map {
                it.forEach{
                    it.budget=viewModel.getBudgetFromMonthAndYear(it.getYear(),it.getMonth())
                }
            return@map it
        }.map {
            return@map viewModel.prepareMultipleViewTypeList(it)
        }.observeOn(AndroidSchedulers.mainThread()).subscribe { list->
            genericAdapterForTwoViewTypes.submitList(list)
        }
    }

    override lateinit var onSwipedRightOrLeft: Observable<Pair<Float, Float>>

}