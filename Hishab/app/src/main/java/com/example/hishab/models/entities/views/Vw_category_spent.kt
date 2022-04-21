package com.example.hishab.models.entities.views

import androidx.room.DatabaseView



@DatabaseView("Select c.category_id as category_id,c.category_name as category_name,cd.month,cd.year,sum(p.cost) as cost from category c inner join product_table pr on c.category_id=pr.category_id inner join tbl_shopping_item p on pr.product_id=p.product_id inner join tbl_shopping s on p.shopping_id=s.shopping_id inner join custom_date cd on s.date_id=cd.date_id group by c.category_id,cd.month,cd.year")
data class Vw_category_spent(val category_id:Int, val category_name:Int, val month:Int, val year:Int, val cost:Int) {


}