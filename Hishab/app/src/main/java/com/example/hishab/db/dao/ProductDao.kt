package com.example.hishab.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hishab.models.CategoryAndProductModel
import com.example.hishab.models.ProductDetailsModel
import com.example.hishab.models.entities.Product

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product): Long

    @Query("select * from product_table where product_name=:itemName and category_id=:categoryId")
    suspend fun getShoppingItemFromItemNameAndCategoryId(
        itemName: String,
        categoryId: Long
    ): Product

    @Query("select s.product_name as productName,s.product_id as productId,c.category_id as categoryId,c.category_name as categoryName from  category c left join product_table s on c.category_id=s.category_id")
    fun getProductCategoryListLeftJoin(): LiveData<List<CategoryAndProductModel>>

    @Query("select s.product_name as productName,s.product_id as productId,c.category_id as categoryId,c.category_name as categoryName from  category c inner join product_table s on c.category_id=s.category_id order by c.category_id")
    fun getProductCategoryListInnerJoin(): LiveData<List<CategoryAndProductModel>>

    @Query("update product_table set product_name=:pName,isSynced = 0 where product_id=:pId")
    fun updateProductName(pName: String, pId: Long)

    @Query("select count(*) from tbl_shopping_item where product_id=:productId")
    fun getPurchaseCountOfProductId(productId: Long): Int

    @Query("delete from product_table where product_id =:productId")
    fun deleteByProductById(productId: Long)

    @Insert
    fun insertProducts(vararg products: Product)

    @Query("Select count(product_id) from product_table where category_id=:id")
    suspend fun getProductCountMappedWithCategoryId(id: Long): Int

    @Query("select pr.product_id as productId, s.shopping_id as shoppingId,pr.product_name as productName,si.cost as cost,s.time as time,cd.day as day,cd.month as month,cd.year as year from product_table pr inner join tbl_shopping_item si on pr.product_id = si.product_id inner join tbl_shopping s on si.shopping_id = s.shopping_id inner join custom_date cd  on cd.date_id = s.date_id where pr.product_id =:id")
    fun getProductDetails(id: Long): LiveData<List<ProductDetailsModel>>

    @Query("select s.product_name as productName,s.product_id as productId,c.category_id as categoryId,c.category_name as categoryName from  category c inner join product_table s on c.category_id=s.category_id where c.category_id =:categoryID")
    fun getProductListFromCategoryIdInnerJoin(categoryID: Long): LiveData<List<CategoryAndProductModel>>

    @Query("update product_table set remoteId =:remoteId, isSynced = 1 where product_id =:productId")
    suspend fun updateRemoteId(remoteId: Int, productId: Long)
    @Query("Select * from product_table where isSynced=0")
    suspend fun getUnupdatedProducts():List<Product>
    @Query("Select * from product_table")
    suspend fun getAllProducts(): List<Product>
}