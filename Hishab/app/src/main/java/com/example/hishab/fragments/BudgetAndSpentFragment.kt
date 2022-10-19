package com.example.hishab.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hishab.R
import com.example.hishab.adapter.SimpleGenericAdapterWithBinding
import com.example.hishab.databinding.FragmentBudgetAndSpentBinding
import com.example.hishab.databinding.LayoutBudgetSpentItemBinding
import com.example.hishab.interfaces.IViewPagerSwipeListener
import com.example.hishab.models.entities.Budget
import com.example.hishab.models.entities.CustomDate
import com.example.hishab.utils.AlarmReceiver
import com.example.hishab.utils.Constant
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.BudgetAndSpentViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class BudgetAndSpentFragment : Fragment(), IViewPagerSwipeListener {
    private lateinit var binding: FragmentBudgetAndSpentBinding;
    private val args: BudgetAndSpentFragmentArgs by navArgs()
    private val budgetAndSpentViewModel: BudgetAndSpentViewModel by viewModels()
    private lateinit var simpleGenericAdapterWithBinding: SimpleGenericAdapterWithBinding<Budget, LayoutBudgetSpentItemBinding>
    private var month = Util.getCurrentMonth()
    private var year = Util.getCurrentYear()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            if (args != null && args!!.year != -1 && args!!.month != -1) {
                year = args!!.year
                month = args!!.month
            }
        } catch (exception: Exception) {
        }

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_budget_and_spent, container, false)
        fetchCategoryBudgetListFromDatabase()
        setRecyclerView()
        setFabClick()
        return binding.root
    }

    private fun setFabClick() {
        binding.fabEditBudget.setOnClickListener {
            try {
                var directions =
                    ViewPagerTabFragmentDirections.actionViewPagerTabFragmentToAddBudgetFragment()
                directions.budgetList =
                    simpleGenericAdapterWithBinding.dataSource.toTypedArray()
                findNavController().navigate(directions)
            } catch (exception: Exception) {
                var directions =
                    BudgetAndSpentFragmentDirections.actionBudgetAndSpentFragmentToAddBudgetFragment()
                directions.budgetList =
                    simpleGenericAdapterWithBinding.dataSource.toTypedArray()
                findNavController().navigate(directions)
            }

        }
    }

    private fun setRecyclerView() {
        simpleGenericAdapterWithBinding =
            SimpleGenericAdapterWithBinding.Create<Budget, LayoutBudgetSpentItemBinding>(R.layout.layout_budget_spent_item)
        simpleGenericAdapterWithBinding.viewInlateLiveData.observe(viewLifecycleOwner) {
            it.second.budget = it.first
        }
        simpleGenericAdapterWithBinding.viewClickLiveData.observe(viewLifecycleOwner) { budget ->
            try {
                var directions =
                    BudgetAndSpentFragmentDirections.actionBudgetAndSpentFragmentToPurchaseHistoryFragment()
                directions.categoryId = budget.categoryId.toInt()
                directions.customDateModel = CustomDate(budget.year, budget.month, -1)
                findNavController().navigate(directions)
            } catch (exception: Exception) {
                var directions =
                    ViewPagerTabFragmentDirections.actionViewPagerTabFragmentToPurchaseHistoryFragment()
                directions.categoryId = budget.categoryId.toInt()
                directions.customDateModel = CustomDate(budget.year, budget.month, -1)
                findNavController().navigate(directions)
            }
        }
        binding.rvCategoryBudget.layoutManager = LinearLayoutManager(activity)
        binding.rvCategoryBudget.adapter = simpleGenericAdapterWithBinding
        onSwipedRightOrLeft = Util.getViewSwipeObservable(binding.rvCategoryBudget)
    }

    private fun fetchCategoryBudgetListFromDatabase() {
        budgetAndSpentViewModel.getBudgetAndCategoryList(
            month,
            year
        ).subscribeOn(Schedulers.io())
            .map { budgetList ->
                var categoryMap = budgetAndSpentViewModel.getCategoryIdAndNameMapping()
                var categoryIdList = ArrayList<Long>()
                budgetList.forEach {
                    categoryIdList.add(it.categoryId)
                    if (it.categoryName == null)
                        it.categoryName = categoryMap[it.categoryId]
                }
                val categorySpentsOfMonth = budgetAndSpentViewModel.getCategorySpentsOfMonth(
                    categoryIdList,
                    month, year
                )

                for (categoryCost in categorySpentsOfMonth) {
                    var budget = budgetList.filter { budget ->
                        budget.categoryId == categoryCost.getCategoryId().toLong()
                    }[0]
                    budget.spent = categoryCost.getCost()
                    budget.categoryName = categoryCost.getCategoryName()
                }
                return@map budgetList
            }.observeOn(AndroidSchedulers.mainThread()).filter { it != null }
            .subscribe { budgetList ->
                updateViewOnDataBaseChange(budgetList)
            }
    }

    private fun updateViewOnDataBaseChange(budgetList: List<Budget>) {
        simpleGenericAdapterWithBinding.update(budgetList)
    }

    override lateinit var onSwipedRightOrLeft: Observable<Pair<Float, Float>>

}