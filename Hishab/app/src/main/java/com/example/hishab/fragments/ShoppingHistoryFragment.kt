package com.example.hishab.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.GenericAdapterForThreeViewTypes
import com.example.hishab.adapter.ShoppingHistoryAdapter
import com.example.hishab.behaviors.FabBehavior
import com.example.hishab.databinding.*
import com.example.hishab.interfaces.IRecyclerViewItemClickCallback
import com.example.hishab.interfaces.IViewPagerSwipeListener
import com.example.hishab.models.ShoppingHistory
import com.example.hishab.models.ThreeLayout
import com.example.hishab.models.TwoPredicate
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.ShoppingHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.function.Predicate

@AndroidEntryPoint
class ShoppingHistoryFragment : Fragment(), IViewPagerSwipeListener {
    private lateinit var buyingListAdapter: GenericAdapterForThreeViewTypes<Int, String, ShoppingHistory, LayoutItemYearBinding, LayoutMonthItemBinding, LayoutBuyingItemBinding>
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentShoppingHistoryBinding
    private lateinit var recyclerViewItemClickCallBack: IRecyclerViewItemClickCallback
    private val viewModel: ShoppingHistoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_history, container, false)
        viewModel
        setRecyclerView()
        fetchBuyingList()
        setButtonClick()
        setNavigationCallBack()
        return binding.root
    }

    private fun setNavigationCallBack() {
        recyclerViewItemClickCallBack = object : IRecyclerViewItemClickCallback {
            override fun onItemClick(buyingHistory: Any) {
                if (buyingHistory is ShoppingHistory) {
                    goToAddBuyingFragment(buyingHistory)
                }
            }
        }
    }

    private fun setButtonClick() {
        binding.fabAddBuying.setOnClickListener(View.OnClickListener {
            goToAddBuyingFragment()

        })
    }

    private fun goToAddBuyingFragment(shoppingHistory: ShoppingHistory? = null) {
        var direction =
            ViewPagerTabFragmentDirections.actionViewPagerTabFragmentToAddShoppingFragment()
        direction.shoppingHistory = shoppingHistory
        findNavController().navigate(direction)

    }

    private fun setRecyclerView() {
        var layouts = ThreeLayout(
            R.layout.layout_item_year,
            R.layout.layout_month_item,
            R.layout.layout_buying_item
        )
        var predicate =
            TwoPredicate(Predicate<Int> { buyingListAdapter.getItemAt(it) is Int },
                Predicate<Int> { buyingListAdapter.getItemAt(it) is String })
        buyingListAdapter = GenericAdapterForThreeViewTypes.Create(layouts, predicate)
        buyingListAdapter.firstViewInlateLiveData.observe(viewLifecycleOwner){
            it.second.year =it.first
        }
        buyingListAdapter.secondViewInlateLiveData.observe(viewLifecycleOwner){
            it.second.month1 =it.first
        }
        buyingListAdapter.thirdViewInlateLiveData.observe(viewLifecycleOwner){
            Log.v("SSS",it.component1().getYear().toString())
            Log.v("SSS",it.component1().getTime().toString())
            it.second.shoppingHistory =it.first
        }
        buyingListAdapter.thirdViewClickLiveData.observe(viewLifecycleOwner){
            goToAddBuyingFragment(it)
        }
        recyclerView = binding.rvBuyingList
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = buyingListAdapter
        onSwipedRightOrLeft = Util.getViewSwipeObservable(recyclerView)
    }

    private fun fetchBuyingList() {
       // CoroutineScope(Dispatchers.IO).launch {
            viewModel.getBuyingHistoryLiveData().observe(viewLifecycleOwner, {
                var processedList = viewModel.processData(it)
                buyingListAdapter.submitList(processedList)
            })
       // }
    }

    override lateinit var onSwipedRightOrLeft: Observable<Pair<Float, Float>>
}