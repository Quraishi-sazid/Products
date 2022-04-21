package com.example.hishab.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.hishab.interfaces.IPagingQuery
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.PagingQueryModel
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.models.entities.CustomDate
import com.example.hishab.paging.CustomPagingSource
import com.example.hishab.repository.Repository
import com.example.hishab.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private lateinit var purchaseHistoryListLiveData: LiveData<List<PurchaseHistory>>
    var access = 3

    @Inject
    lateinit var repository: Repository

    fun getPurchaseItems(lastPurchaseId: Long, loadSize: Int): List<PurchaseHistory> {
        return repository.getPurchaseHistory(lastPurchaseId, loadSize);
    }

    fun getDetailsOfCategory(
        categoryId: Int,
        lastPurchaseId: Long,
        loadSize: Int
    ): List<PurchaseHistory> {
        return repository.getDetailsOfCategory(categoryId, lastPurchaseId, loadSize)
    }

    fun getDetailsOfCategoryOfMonthAndYear(
        lastPurchaseId: Long,
        limit: Int,
        categoryId: Int,
        month: Int,
        year: Int
    ): List<PurchaseHistory> {
        return repository.getDetailsOfCategoryOfMonthAndYear(
            lastPurchaseId,
            limit,
            categoryId,
            month,
            year
        )
    }


    fun getDateSeparatedPurchaseHistoryList(list: List<PurchaseHistory>): List<Any> {
        var objectList = ArrayList<Any>()
        var pMonth = "";
        for (it in list) {
            val month_name = Util.getMonthForInt(it.getMonth())
            if (!pMonth.equals(month_name)) {
                objectList.add(month_name);
                pMonth = month_name
            }
            objectList.add(it)
        }
        return objectList
    }

    suspend fun delete(position: Long) {
        repository.deletePurchaseHistory(position);
    }

    suspend fun getProductCategoryList(): LiveData<List<CategoryAndProductModel>> {
        return repository.getProductCategoryListLeftJoin()
    }

    private fun generatePagingQuery(
        categoryId: Int = -1,
        customDate: CustomDate? = null
    ): IPagingQuery<Any> {
        return object : IPagingQuery<Any> {
            override fun getPagingData(lastObject: Any?, loadSize: Int): PagingQueryModel<Any> {
                var lastPurchaseId: Long = 9999999999999L
                if (lastObject is PurchaseHistory) {
                    lastPurchaseId = lastObject.getPurchaseId()!!
                }
                var isShowAllPurchaseHistory = customDate == null && categoryId == -1;
                var purchaseHistoryList = if (isShowAllPurchaseHistory) {
                    getPurchaseItems(lastPurchaseId, loadSize)
                } else {
                    if (customDate == null) {
                        getDetailsOfCategory(
                            categoryId,
                            lastPurchaseId,
                            loadSize
                        )
                    } else {
                        getDetailsOfCategoryOfMonthAndYear(
                            lastPurchaseId!!,
                            loadSize,
                            categoryId,
                            customDate!!.getMonth(), customDate!!.getYear()
                        )
                    }
                }
                var commonList = getDateSeparatedPurchaseHistoryList(purchaseHistoryList);
                return PagingQueryModel<Any>(
                    commonList,
                    if (commonList.isNullOrEmpty()) null else commonList[commonList.size - 1]
                )
            }
        }
    }

    fun getFlow(categoryId: Int = -1, customDate: CustomDate? = null): Flow<PagingData<Any>> {
        return Pager(PagingConfig(20)) {
            CustomPagingSource<Any>(generatePagingQuery(categoryId, customDate))
        }.flow.cachedIn(viewModelScope)
    }


    //fun getPagingQuery
}