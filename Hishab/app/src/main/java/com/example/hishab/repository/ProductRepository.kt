package com.example.hishab.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.hishab.db.dao.ProductDao
import com.example.hishab.di.FooEntryPoint
import com.example.hishab.di.RepositoryEntryPoint
import com.example.hishab.interfaces.IPayloadHandler
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.ProductDetailsModel
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.Product
import com.example.hishab.retrofit.RetrofitHelper
import com.example.hishab.retrofit.request.CategoryRequest
import com.example.hishab.retrofit.request.ProductRequest
import com.example.hishab.retrofit.response.ProductResponse
import com.example.hishab.utils.Constant
import com.example.hishab.utils.PreferenceHelper
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProductRepository(context: Context) : IPayloadHandler {
    var categoryRepository: CategoryRepository
    var payloadRepository: PayloadRepository

    private var productDao: ProductDao

    init {
        productDao = EntryPointAccessors.fromApplication(context, FooEntryPoint::class.java).database.ProductDao()
        var repositoryEntryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            RepositoryEntryPoint::class.java
        )
        categoryRepository = repositoryEntryPoint.categoryRepository
        payloadRepository = repositoryEntryPoint.payloadRepository
    }

    suspend fun insertProduct(product: Product): Long {
        return productDao.insert(product)
    }

    suspend fun getProductFromNameAndCId(itemName: String, categoryId: Long): Product {
        return productDao.getShoppingItemFromItemNameAndCategoryId(itemName, categoryId);
    }

    fun getProductCategoryListLeftJoin(): LiveData<List<CategoryAndProductModel>> {
        return productDao.getProductCategoryListLeftJoin()
    }

    fun getProductCategoryListInnerJoin(): LiveData<List<CategoryAndProductModel>> {
        return productDao.getProductCategoryListInnerJoin()
    }

    fun updateProductNameAndCategoryId(productId: Long,categoryId: Long, productName: String) {
        productDao.updateProductNameAndCategoryId(productName,categoryId, productId)
    }

    fun getPurchaseCountOfProductId(productId: Long): Int {
        return productDao.getPurchaseCountOfProductId(productId)
    }

    fun deleteByProductById(productId: Long) {
        productDao.deleteByProductById(productId)
    }

    suspend fun getProductCountMappedWithCategoryId(id: Long): Int {
        return productDao.getProductCountMappedWithCategoryId(id)
    }

    fun getProductDetailsLiveData(id: Long): LiveData<List<ProductDetailsModel>> {
        return productDao.getProductDetails(id)
    }

    fun getProductListFromCategoryIdInnerJoin(categoryID: Long): LiveData<List<CategoryAndProductModel>> {
        return productDao.getProductListFromCategoryIdInnerJoin(categoryID)
    }

    suspend fun getProductIdByInsertingInDataBase(
        categoryId: Long,
        categoryName: String,
        productName: String
    ): Product {
        if (categoryId == 0L) {
            var quariedCategory = categoryRepository.getCategoryFromName(categoryName)
            var insertedCategoryId = -1L
            if (quariedCategory == null) {
                insertedCategoryId =
                    categoryRepository.insertCategoryLocally(Category(categoryName))
            } else
                insertedCategoryId = quariedCategory.categoryId
            var insertableProduct = Product(productName, insertedCategoryId)
            insertableProduct.productId = insertProduct(insertableProduct)
            insertableProduct.categoryId = insertedCategoryId
            return insertableProduct
        } else {
            var queriedShoppingItem =
                getProductFromCategoryIdAndProductNameByInsertingOrFetching(
                    categoryId,
                    productName
                )
            return queriedShoppingItem
        }
    }

    suspend fun saveToRemote(product: Product, _category: Category? = null) {
        var category = _category
        if (category == null)
            category = categoryRepository.getCategoryById(product.categoryId)
        var productRequest = createProductRequest(product, category)
        try {
            var response = RetrofitHelper.hishabApi.addOrUpdateProduct(productRequest)
            if (response.isSuccessful) {
                var productResponse = response.body()
                GlobalScope.launch {
                    handleSuccess(productResponse)
                }
            } else {
                // handleError(product, productRequest)
            }
        } catch (ex: Exception) {
            // handleError(product, productRequest)
        }
    }


    suspend fun  handleSuccess(productResponse: ProductResponse?) {
        if (productResponse != null) {
            categoryRepository.handleSuccess(productResponse.categoryResponse)
            productDao.updateRemoteId(
                productResponse!!.productId.toInt(),
                productResponse.localId.toLong()
            )
        }
    }

    private fun createProductRequest(product: Product, category: Category): ProductRequest {
        var newCategoryRequest = CategoryRequest().apply {
            categoryId = category.remoteId.toInt()
            newCategoryName = category.getCategoryName()
            localId = category.categoryId.toInt()
        }

        return ProductRequest().apply {
            productId = product.remoteId
            productName = product.getProductName()
            userId = PreferenceHelper.get(Constant.User_Id, -1)
            localId = product.productId.toInt()
            categoryRequest = newCategoryRequest
        }
    }

    private suspend fun getProductFromCategoryIdAndProductNameByInsertingOrFetching(
        categoryId: Long,
        productName: String
    ): Product {
        var quariedProduct = getProductFromNameAndCId(
            productName,
            categoryId
        )
        if (quariedProduct == null) {
            quariedProduct = Product()
            quariedProduct.categoryId = categoryId
            quariedProduct.setProductName(productName)
            quariedProduct.productId = insertProduct(quariedProduct)
        }
        return quariedProduct
    }

    suspend fun insertOrUpdateProductListInRemote(
        productRequestList: List<ProductRequest>
    ): List<ProductResponse>? {
        try {
            var response = RetrofitHelper.hishabApi.addOrUpdateProductList(productRequestList)
            if (response.isSuccessful)
                return response.body()
            else
                return null
        } catch (ex: Exception) {
            return null
        }

    }

    override suspend fun updateRemote() {
        var productRequestList = ArrayList<ProductRequest>()
        productDao.getUnupdatedProducts().forEach { product ->
            var category = categoryRepository.getCategoryById(product.categoryId)
            var productRequest = ProductRequest(product, category)
            productRequestList.add(productRequest)
            insertOrUpdateProductListInRemote(productRequestList)?.forEach { productResponse ->
                handleSuccess(productResponse)
            }
        }

    }

    suspend fun getAllProductsSuspended() : List<Product> {
        return productDao.getAllProducts()
    }

}