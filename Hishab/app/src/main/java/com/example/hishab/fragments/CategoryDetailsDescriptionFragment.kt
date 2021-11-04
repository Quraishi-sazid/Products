package com.example.hishab.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.hishab.R
import com.example.hishab.viewmodel.CategoryDetailsDescriptionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class CategoryDetailsDescriptionFragment : Fragment() {
    // TODO: Rename and change types of parameters

    //val args: CategoryDetailsDescriptionFragmentArgs by navArgs()
    private val categoryDetailsDescriptionViewModel:CategoryDetailsDescriptionViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_category_details_description, container, false)
        /*CoroutineScope(Dispatchers.IO).launch {
            val getdetailsOfCategoryfromDate =
                categoryDetailsDescriptionViewModel.getdetailsOfCategoryfromDate(
                    args.categoryId,
                    args.dateModel
                )
            withContext(Dispatchers.Main)
            {
                //initiate recycler view

            }
        }*/
        return inflate
    }


}