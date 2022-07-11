package com.example.hishab.fragments

//import com.example.hishab.databinding.FragmentPurchaseHistoryBinding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.PagingAdapterForThreeViewTypes
import com.example.hishab.adapter.PagingAdapterForTwoViewTypes
import com.example.hishab.databinding.LayoutItemYearBinding
import com.example.hishab.databinding.LayoutMonthItemBinding
import com.example.hishab.databinding.LayoutPurchaseItemBinding
import com.example.hishab.interfaces.IViewPagerSwipeListener
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.models.ThreeLayout
import com.example.hishab.models.TwoPredicate
import com.example.hishab.repository.Repository
import com.example.hishab.utils.Util
import com.example.hishab.viewmodel.PurchaseHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.util.function.Predicate
import javax.inject.Inject


@AndroidEntryPoint
class PurchaseHistoryFragment : Fragment(), IViewPagerSwipeListener {
    val purchaseHistoryViewModel: PurchaseHistoryViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    lateinit var pagingFlow: Flow<PagingData<Any>>

    lateinit var adapter: PagingAdapterForThreeViewTypes<Int, String, PurchaseHistory, LayoutItemYearBinding, LayoutMonthItemBinding, LayoutPurchaseItemBinding>
    val args: PurchaseHistoryFragmentArgs by navArgs()
    var count = 1

    @Inject
    lateinit var repository: Repository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_purchase_history, container, false)
        setUpRecyclerView(inflate)
        pagingFlow = try {
            purchaseHistoryViewModel.getFlow(args.categoryId, args.customDateModel)
        } catch (exception: Exception) {
            purchaseHistoryViewModel.getFlow()
        }
        CoroutineScope(Dispatchers.IO).launch {
            pagingFlow.collectLatest {
                adapter.submitData(it)
            }
        }
        return inflate
    }

    private fun setUpRecyclerView(inflate: View) {
        var layouts = ThreeLayout(
            R.layout.layout_item_year,
            R.layout.layout_month_item,
            R.layout.layout_purchase_item
        )
        var twoPredicate = TwoPredicate(Predicate<Int> { adapter.getItemAt(it) is Int },
            Predicate<Int> { adapter.getItemAt(it) is String })
        adapter = PagingAdapterForThreeViewTypes.Create(layouts,twoPredicate)
        adapter.firstViewInlateLiveData.observe(viewLifecycleOwner) {
            it.second.year = it.first;
        }
        adapter.secondViewInlateLiveData.observe(viewLifecycleOwner) {
            it.second.month1 = it.first;
        }
        adapter.thirdViewInlateLiveData.observe(viewLifecycleOwner) {
            it.second.purchaseHistory = it.first
        }
        adapter.thirdViewClickLiveData.observe(viewLifecycleOwner){
            try{
                var directions = ViewPagerTabFragmentDirections.actionViewPagerTabFragmentToAddShoppingFragment()
                directions.productId = it.getProductId()
                directions.time = it.getTime()
                directions.buyingId = it.getBuyingId()!!.toInt()
                findNavController().navigate(directions)
            }catch (exception: Exception){
                var directions = PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToAddShoppingFragment()
                directions.productId = it.getProductId()
                directions.time = it.getTime()
                directions.buyingId = it.getBuyingId()!!.toInt()
                findNavController().navigate(directions)
            }

        }
        recyclerView = inflate.findViewById<RecyclerView>(R.id.recycler_view)!!
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.adapter = adapter
        onSwipedRightOrLeft = Util.getViewSwipeObservable(recyclerView)
    }

    private fun getDiffUtilCallBack(): DiffUtil.ItemCallback<Any> {
        var diffUtilCallBack = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (!Util.getType(oldItem).equals(Util.getType(newItem))) {
                    return false;
                }
                if (oldItem is String && newItem is String)
                    return oldItem.equals(newItem)
                else if (oldItem is PurchaseHistory && newItem is PurchaseHistory) {
                    return oldItem.getPurchaseId() == newItem.getPurchaseId()
                }
                return false
            }

            override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
                if (oldItem is PurchaseHistory && newItem is PurchaseHistory) {
                    val b = oldItem.getCategoryName().equals(newItem.getCategoryName()) &&
                            oldItem.getItemName().equals(newItem.getItemName()) &&
                            oldItem.getCost() == newItem.getCost() &&
                            oldItem.getDay() == newItem.getDay() &&
                            oldItem.getMonth() == newItem.getMonth() &&
                            oldItem.getYear() == newItem.getYear()
                    return b;
                }
                return true
            }
        }
        return diffUtilCallBack
    }

    override lateinit var onSwipedRightOrLeft: Observable<Pair<Float, Float>>

}