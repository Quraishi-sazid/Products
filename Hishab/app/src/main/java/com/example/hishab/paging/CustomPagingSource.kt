package com.example.hishab.paging;

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.hishab.interfaces.IPagingQuery

class CustomPagingSource<Value : Any>(private val iPagingQuery: IPagingQuery<Value>): PagingSource<Int, Value>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 0
    }
    var lastItem:Value?=null
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Value> {
        val position = params.key ?: INITIAL_PAGE_INDEX
        val pagingData=iPagingQuery.getPagingData(lastItem,params.loadSize)
        lastItem = pagingData.lastItem
        return LoadResult.Page(
            data = pagingData.data,
            prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
            nextKey = if (pagingData.data.isNullOrEmpty()) null else position + 1
        )
    }
    override fun getRefreshKey(state: PagingState<Int, Value>): Int? {
        return null
    }

}