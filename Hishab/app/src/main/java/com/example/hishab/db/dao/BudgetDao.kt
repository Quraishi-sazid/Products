package com.example.hishab.db.dao

import androidx.room.*
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

    @Query("Select cost as Cost,category_id as CategoryId ,category_name as CategoryName from Vw_category_spent where category_id in (:categoryIds) and month=:month and year=:year")
    fun getCategorySpents(categoryIds:List<Long>, month:Int, year:Int): List<CategoryCostModel>
    @Insert
    abstract fun insertAll(vararg budgetList:Budget)
    @Update
    fun updateBudgetList(budgetList: List<Budget>)
    @Query("delete from tbl_category_budget where category_id=:deleteId")
    fun deleteByCategoryId(deleteId: Long)
    @Query("Select v.year as year,v.month as month,sum(v.cost) as totalCost,-1 as budget from vw_category_spent v where year<:currentYear or (year=:currentYear and month<:currentMonth) group by year,month order by year,month")
    fun getPreviousBudgetSpentList(currentMonth:Int,currentYear:Int):Flowable<List<MonthlySpentModel>>
    @Query("Select sum(budget) from tbl_category_budget where month=:month and year=:year")
    fun getBudgetFromMonthAndYear(year: Int, month: Int):Int


}