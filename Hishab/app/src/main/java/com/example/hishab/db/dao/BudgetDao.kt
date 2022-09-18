package com.example.hishab.db.dao

import androidx.room.*
import com.example.hishab.models.BudgetCategoryQuery
import com.example.hishab.models.CategoryCostModel
import com.example.hishab.models.MonthlySpentModel
import com.example.hishab.models.entities.Budget
import io.reactivex.Flowable

//b inner join category c on b.category_id=c.category_id where month=:month and year=:year Limit 1
@Dao
interface BudgetDao {

    /*@Query("Select budget_id as budgetId, b.category_id as categoryId, budget as budget, month as month,year as year from tbl_category_budget b")
    fun getBudgetFlowable(month:Int, year:Int): Flowable<List<Budget>>*/

  /*  @Query("Select budget_id as budgetId, b.category_id as categoryId, budget as budget, month as month,year as year from tbl_category_budget b")
    fun getBudgetList(month:Int,year:Int): List<Budget>*/

    @Query("Select * from tbl_category_budget b where month=:month and year=:year")
    fun getBudgetFlowable(month: Int, year: Int): Flowable<List<Budget>>
    @Insert
    fun insert(b:Budget):Long


    @Insert
    abstract fun insertAll(vararg budgetList:Budget)
    @Update
    fun updateBudgetList(budgetList: List<Budget>)
    @Query("delete from tbl_category_budget where category_id=:deleteId")
    fun deleteByCategoryId(deleteId: Long)
    @Query("Select v.year as year,v.month as month,sum(v.cost) as totalCost,-1 as budget from vw_category_spent v where year<:currentYear or (year=:currentYear and month<:currentMonth) group by year,month order by year,month")
    fun getPreviousBudgetSpentList(currentMonth:Int,currentYear:Int):Flowable<List<MonthlySpentModel>>
    @Query("Select sum(budget) from tbl_category_budget where month=:month and year=:year group by month,year ")
    fun getBudgetFromMonthAndYear(year: Int, month: Int):Int
    //@Query("select post.budget_id as budgetID,cat.category_id as categoryId,category_name as categoryName, ifnull(post.budget,0) as budget from category cat left join (select category_id, budget_id,budget from tbl_category_budget where month =:month and year =:year) post  on cat.category_id = post.category_id")
    @Query("select post.budget_id as budgetID,cat.category_id as categoryId,category_name as categoryName, ifnull(post.budget,0) as budget from category cat left join tbl_category_budget post  on cat.category_id = post.category_id where month =:month and year =:year")
    fun getCategoryAndBudgetList(year: Int, month: Int):List<BudgetCategoryQuery>
    @Query("update tbl_category_budget set budget=:budget where category_id=:id and month=:month and year=:year")
    suspend fun updateBudgetById(id:Long,budget:Int,month:Int,year:Int)
}