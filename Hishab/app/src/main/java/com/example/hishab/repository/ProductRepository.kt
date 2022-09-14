package com.example.hishab.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.hishab.db.AppDatabase
import com.example.hishab.db.dao.ProductDao
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.ProductDetailsModel
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PayLoad
import com.example.hishab.models.entities.Product
import com.example.hishab.retrofit.ApiURL.Companion.PRODUCT_ADD_OR_UPDATE
import com.example.hishab.retrofit.RetrofitHelper
import com.example.hishab.retrofit.request.CategoryRequest
import com.example.hishab.retrofit.request.ProductRequest
import com.example.hishab.retrofit.response.ProductResponse
import com.example.hishab.utils.Constant
import com.example.hishab.utils.PreferenceHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class ProductRepository(context: Context) {

    @Inject
    lateinit var categoryRepository: CategoryRepository

    @Inject
    lateinit var payloadRepository: PayloadRepository

    private var productDao: ProductDao

    init {
        productDao = AppDatabase.getDatabase(context).ProductDao()
    }

    suspend fun insertProduct(product: Product): Long {
        return productDao.insert(product)
    }

    suspend fun getShoppingItemFromNameAndCId(itemName: String, categoryId: Long): Product {
        return productDao.getShoppingItemFromItemNameAndCategoryId(itemName, categoryId);
    }

    fun getProductCategoryListLeftJoin(): LiveData<List<CategoryAndProductModel>> {
        return productDao.getProductCategoryListLeftJoin()
    }

    fun getProductCategoryListInnerJoin(): LiveData<List<CategoryAndProductModel>> {
        return productDao.getProductCategoryListInnerJoin()
    }

    fun updateProductName(productId: Long, productName: String) {
        productDao.updateProductName(productName, productId)
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
    ): Long {
        if (categoryId == 0L) {
            var quariedCategory = categoryRepository.getCategoryFromName(categoryName)
            var insertedCategoryId = -1L
            if (quariedCategory == null) {
                insertedCategoryId =
                    categoryRepository.insertCategoryLocally(Category(categoryName))
            } else
                insertedCategoryId = quariedCategory.categoryId
            return insertProduct(Product(productName, insertedCategoryId))
        } else {
            var queriedShoppingItem =
                getProductFromCategoryIdAndProductNameByInsertingOrFetching(
                    categoryId,
                    productName
                )
            return queriedShoppingItem.productId
        }
    }

    suspend fun saveToRemote(product: Product, _category: Category? = null) {
        var category = _category
        if (category == null)
            category = categoryRepository.getCategoryById(product.categoryId)
        var productRequest = createProductRequest(product, category)
        var response = RetrofitHelper.hishabApi.addOrUpdateProduct(productRequest)
        if (response.isSuccessful) {
            var productResponse = response.body()
            GlobalScope.launch {
                handleSuccess(productResponse, product.payloadId)
            }
        } else {
            if (product.payloadId == -1L) {
                GlobalScope.launch {
                    var payLoadId = payloadRepository.InsertIntoPayload(
                        PayLoad(
                            0,
                            PRODUCT_ADD_OR_UPDATE,
                            productRequest.convertToJson()
                        )
                    )
                    productDao.updateRemoteAndPayloadId(
                        -1,
                        payLoadId,
                        productRequest.localId.toLong()
                    )
                }
            }
        }
    }

    public suspend fun handleSuccess(
        productResponse: ProductResponse?, payLoadId: Long
    ) {
        if (productResponse != null && productResponse.product != null) {
            payloadRepository.deleteFromPayloadById(payLoadId)
            productDao.updateRemoteAndPayloadId(
                productResponse!!.product!!.productId.toInt(),
                productResponse.product!!.payloadId,
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
        var quariedProduct = getShoppingItemFromNameAndCId(
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
    ): Response<List<ProductResponse>> {
        return RetrofitHelper.hishabApi.addOrUpdateProductList(productRequestList)
    }

}