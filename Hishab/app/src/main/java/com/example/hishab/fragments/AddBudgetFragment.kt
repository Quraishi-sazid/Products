package com.example.hishab.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hishab.R
import com.example.hishab.adapter.SimpleGenericAdapterWithBinding
import com.example.hishab.databinding.FragmentAddBudgetBinding
import com.example.hishab.databinding.LayoutBudgetAddItemBinding
import com.example.hishab.models.BudgetCategoryQuery
import com.example.hishab.models.entities.Budget
import com.example.hishab.viewmodel.AddBudgetViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AddBudgetFragment : Fragment() {

    private val viewModel: AddBudgetViewModel by viewModels()
    val args: AddBudgetFragmentArgs by navArgs()
    lateinit var binding: FragmentAddBudgetBinding
    lateinit var categoryBudgetMappingAdapter: SimpleGenericAdapterWithBinding<BudgetCategoryQuery, LayoutBudgetAddItemBinding>
    var budgetList = ArrayList<BudgetCategoryQuery>()
    val compositeDisposable = CompositeDisposable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_budget, container, false)
        viewModel //to avoid viewModel object creation in background thread which cause exception
        CoroutineScope(Dispatchers.IO).launch {
            fetchBudgetNotSettledCategoryData()
        }
        setRecyclerView()
        setSearchBox()
        setSubmitButton()
        return binding.root
    }

    private suspend fun fetchBudgetNotSettledCategoryData() {
        viewModel.getBudgetFlowable().map {
            var tempMap = HashMap<Long, Budget>()
            it.forEach { budget -> tempMap[budget.categoryId] = budget }
            return@map tempMap
        }.subscribeOn(Schedulers.io()).map { map ->
            var categoryList = viewModel.getCategoryList()
            categoryList.forEach {
                if (map.containsKey(it.categoryId))
                    budgetList.add(
                        BudgetCategoryQuery(
                            map[it.categoryId]!!.getBudgetId(),
                            it.categoryId,
                            it.getCategoryName(),
                            map[it.categoryId]!!.budget
                        )
                    )
                else
                    budgetList.add(BudgetCategoryQuery(-1, it.categoryId, it.getCategoryName(), 0))
            }
            return@map budgetList
        }.observeOn(AndroidSchedulers.mainThread()).subscribe{
            categoryBudgetMappingAdapter.update(it)
        }

    }


    private fun setSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            budgetList?.forEach {
                CoroutineScope(Dispatchers.IO).launch {
                    if (it.budgetID != -1L) {
                        viewModel.updateBudgetById(
                            it.categoryId,
                            it.budget,
                            viewModel.month,
                            viewModel.year
                        )
                    } else {
                        viewModel.insertBudget(
                            Budget(
                                it.categoryId,
                                it.budget,
                                viewModel.month,
                                viewModel.year
                            )
                        )
                    }
                }
            }
            requireActivity().onBackPressed();
        }
    }


    private fun setRecyclerView() {
        var diffUtilCallback = object : DiffUtil.ItemCallback<BudgetCategoryQuery>() {
            override fun areItemsTheSame(
                oldItem: BudgetCategoryQuery,
                newItem: BudgetCategoryQuery
            ): Boolean {
                return oldItem.budgetID == newItem.budgetID
            }

            override fun areContentsTheSame(
                oldItem: BudgetCategoryQuery,
                newItem: BudgetCategoryQuery
            ): Boolean {
                return oldItem.categoryName.equals(newItem.categoryName) &&
                        oldItem.budget == newItem.budget
            }
        }

        categoryBudgetMappingAdapter =
            SimpleGenericAdapterWithBinding.Create<BudgetCategoryQuery, LayoutBudgetAddItemBinding>(
                R.layout.layout_budget_add_item,
                diffUtilCallback
            )
        categoryBudgetMappingAdapter.viewInlateLiveData.observe(viewLifecycleOwner) {
            it.second.budget = it.first
        }
        binding.rvBudgetInput.layoutManager = LinearLayoutManager(activity)
        binding.rvBudgetInput.adapter = categoryBudgetMappingAdapter
    }

    private fun setSearchBox() {
        compositeDisposable.add(Observable.create<String> { emitter ->
            binding.etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    emitter.onNext(p0.toString().lowercase())
                }

            })
        }.debounce(1000, TimeUnit.MILLISECONDS).filter { budgetList != null }
            .subscribe { searchText ->
                Log.d("AddBudgetFragment", searchText)
                if (searchText == null || searchText.equals("")) {
                    categoryBudgetMappingAdapter.update(budgetList!!)
                } else {
                    var filteredList = budgetList?.filter { budget ->
                        budget.categoryName!!.lowercase().contains(searchText)
                    }
                    categoryBudgetMappingAdapter.update(filteredList!!)
                }
            })


    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}