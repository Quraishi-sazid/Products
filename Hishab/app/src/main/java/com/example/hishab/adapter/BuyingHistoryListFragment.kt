package com.example.hishab.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.changedinter.INavigationCallback
import com.example.hishab.databinding.FragmentBuyingHistoryListBinding
import com.example.hishab.viewmodel.BuyingHistoryListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class BuyingHistoryListFragment : Fragment() {

    private lateinit var buyingListAdapter: BuyingListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentBuyingHistoryListBinding
    private lateinit var navigationCallBack:INavigationCallback
    private val viewModel: BuyingHistoryListViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_buying_history_list, container, false)
        fetchBuyingList()
        setButtonClick()
        setNavigationCallBack()
        return binding.root
    }

    private fun setNavigationCallBack() {
        navigationCallBack= object :INavigationCallback{
            override fun navigate(id: Any) {
                if(id is Long)
                {
                    goToAddBuyingFragment(id)
                }
            }

        }
    }

    private fun setButtonClick() {
        binding.fabAddBuying.setOnClickListener(View.OnClickListener {
           goToAddBuyingFragment()

        })
    }

    private fun goToAddBuyingFragment(buyingId:Long=-1L) {
        var direction= BuyingHistoryListFragmentDirections.actionBuyingHistoryListFragmentToAddBuyingFragment()
        direction.buyingId=buyingId.toInt()
        findNavController().navigate(direction)

    }

    private fun setRecyclerView() {
        recyclerView = binding.rvBuyingList
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter=buyingListAdapter
    }

    private fun fetchBuyingList() {
        CoroutineScope(Dispatchers.Main).launch {
            val buyingHistoryList = viewModel.getBuyingHistoryList()
            withContext(Dispatchers.Main)
            {
                buyingListAdapter= BuyingListAdapter(buyingHistoryList,navigationCallBack)
                setRecyclerView()
            }
        }
    }

}