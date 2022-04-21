package com.example.hishab.models

data class PagingQueryModel<T>(val data:List<T>,var lastItem:T?) {
}