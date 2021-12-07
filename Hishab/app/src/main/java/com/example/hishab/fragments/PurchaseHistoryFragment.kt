package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hishab.R
import com.example.hishab.adapter.PurchaseItemsAdapter
import com.example.hishab.changedinter.IHandleAlertDialog
import com.example.hishab.changedinter.ISwipeItemCallback
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.CategoryAndProductModel
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.repository.Repository
import com.example.hishab.utils.SwipeToDeleteCallback
import com.example.hishab.utils.Util
//import com.example.hishab.databinding.FragmentPurchaseHistoryBinding
import com.example.hishab.viewmodel.PurchaseHistoryViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class PurchaseHistoryFragment : Fragment() {
    val purchaseHistoryViewModel:PurchaseHistoryViewModel by viewModels()
    lateinit var recyclerView: RecyclerView
    var adapter=PurchaseItemsAdapter()
    val args: PurchaseHistoryFragmentArgs by navArgs()
    lateinit var categoryAndProductModelList:List<CategoryAndProductModel>
    lateinit var distinctCategoryList:List<Category?>
    lateinit var swipeItemCallback: ISwipeItemCallback
    lateinit var handleAlertDialog: IHandleAlertDialog
    @Inject
    lateinit var repository: Repository
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_purchase_history, container, false)
        if(args.purchaseId==-1)//
        {
            CoroutineScope(Dispatchers.Main).launch {//Main thread query needs to be changed
                purchaseHistoryViewModel.getPurchaseItems().observe(viewLifecycleOwner, Observer
                {
                    val convertedList = purchaseHistoryViewModel.convert(it);
                    adapter.submitList(convertedList)
                })
                purchaseHistoryViewModel.getProductCategoryList().observe(viewLifecycleOwner,{
                    categoryAndProductModelList=it
                    distinctCategoryList = categoryAndProductModelList.distinctBy { it.getCategoryId() }.map { it.getCategoryId()?.let { it1 ->
                        it.getCategoryName()?.let { it2 -> Category(it1, it2) }
                    } }
                });
            }
        }
        else
        {
            inflate.findViewById<FloatingActionButton>(R.id.fab).visibility=View.GONE
            CoroutineScope(Dispatchers.Main).launch {//Main thread query needs to be changed
                purchaseHistoryViewModel.getdetailsOfCategoryfromDate(args.purchaseId,args.dateModel!!).observe(viewLifecycleOwner, Observer
                {
                    val convertedList = purchaseHistoryViewModel.convert(it);
                    adapter.submitList(convertedList)
                })
                purchaseHistoryViewModel.getProductCategoryList().observe(viewLifecycleOwner,{
                    categoryAndProductModelList=it
                });
            }
        }
        recyclerView = inflate.findViewById<RecyclerView>(R.id.recycler_view)!!
        recyclerView.layoutManager=LinearLayoutManager(activity)
        recyclerView.adapter= adapter;
        val fab = inflate.findViewById<FloatingActionButton>(R.id.fab)

        fab.setOnClickListener(View.OnClickListener {
           // val action = PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToAddShoppingFragment(categoryAndProductModelList.toTypedArray(),distinctCategoryList.toTypedArray())
           // findNavController().navigate(action);
        })
        setHandleAlertDialog()
        setSwipeItemCallback()
        ItemTouchHelper(SwipeToDeleteCallback(context,swipeItemCallback)).attachToRecyclerView(recyclerView)
        return inflate
    }

    private fun setSwipeItemCallback() {
        swipeItemCallback= object: ISwipeItemCallback {
            override fun onSwipeItem(viewHolder: RecyclerView.ViewHolder, direction: Int){
                if(direction==ItemTouchHelper.LEFT)
                {

                    if(adapter.getElementAt(viewHolder.adapterPosition) is PurchaseHistory)
                    {
                        context?.let { Util.showItemSwipeDeleteAlertDialog(it,"Delete Entry","Do you want to delete this entry?",(adapter.getElementAt(viewHolder.adapterPosition) as PurchaseHistory).getPurchaseId(),handleAlertDialog) }
                    }
                }
                else
                    ItemTouchHelper.LEFT
                run {
                    var purchaseHistory =(adapter.getElementAt(viewHolder.adapterPosition) as PurchaseHistory)
                    /*    var directions=PurchaseHistoryFragmentDirections.actionPurchaseHistoryFragmentToAddShoppingFragment()
                        directions.editHistory=purchaseHistory
                        purchaseHistoryFragment.findNavController()
                            .navigate(directions)*/
                }
            }
        }
    }
    private fun setHandleAlertDialog() {
        handleAlertDialog=object :IHandleAlertDialog{
            override fun onHandleDialog(isYes: Boolean, deleteId: Long) {
                if(isYes)
                {
                    CoroutineScope(Dispatchers.IO).launch{
                        purchaseHistoryViewModel.delete(deleteId)
                    }
                }
                else
                {
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }


}