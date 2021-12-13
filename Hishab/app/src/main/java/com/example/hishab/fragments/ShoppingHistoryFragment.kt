package com.example.hishab.fragments

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
import com.example.hishab.adapter.ShoppingHistoryAdapter
import com.example.hishab.databinding.FragmentShoppingHistoryBinding
import com.example.hishab.interfaces.IRecyclerViewItemClickCallback
import com.example.hishab.models.ShoppingHistory
import com.example.hishab.viewmodel.ShoppingHistoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingHistoryFragment : Fragment() {

    private lateinit var buyingListAdapter: ShoppingHistoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentShoppingHistoryBinding
    private lateinit var recyclerViewItemClickCallBack:IRecyclerViewItemClickCallback
    private val viewModel: ShoppingHistoryViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_history, container, false)
        fetchBuyingList()
        setButtonClick()
        setNavigationCallBack()
        return binding.root
    }

    private fun setNavigationCallBack() {
        recyclerViewItemClickCallBack= object :IRecyclerViewItemClickCallback{
            override fun onItemClick(buyingHistory: Any) {
                if(buyingHistory is ShoppingHistory)
                {
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

    private fun goToAddBuyingFragment(shoppingHistory: ShoppingHistory? =null) {
        var direction=
            ShoppingHistoryFragmentDirections.actionShoppingHistoryFragmentToAddShoppingFragment()
        direction.shoppingHistory=shoppingHistory
        findNavController().navigate(direction)

    }

    private fun setRecyclerView() {
        recyclerView = binding.rvBuyingList
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.adapter=buyingListAdapter
    }

    private fun fetchBuyingList() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getBuyingHistoryLiveData().observe(viewLifecycleOwner,{
                buyingListAdapter= ShoppingHistoryAdapter(it,recyclerViewItemClickCallBack)
                setRecyclerView()
            })
        }
    }

}