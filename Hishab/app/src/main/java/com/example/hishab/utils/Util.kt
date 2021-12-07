package com.example.hishab.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.example.hishab.changedinter.IHandleAlertDialog
import com.example.hishab.models.AddItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.ShoppingItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormatSymbols


class Util {
    companion object{

        fun getMonthForInt(num: Int): String {
            var month = "wrong"
            val dfs = DateFormatSymbols()
            val months: Array<String> = dfs.getMonths()
            if (num >= 0 && num <= 11) {
                month = months[num]
            }
            return month
        }
        fun getType(item: Any):String?
        {
            return item::class.simpleName
        }

        fun showItemSwipeDeleteAlertDialog(context: Context, title:String, message:String, deleteId:Long?, handleAlertDialog: IHandleAlertDialog)
        {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                    android.R.string.yes,
                    DialogInterface.OnClickListener { dialog, which ->
                            if (deleteId != null)
                                handleAlertDialog.onHandleDialog(true,deleteId)
                    })
                .setNegativeButton(android.R.string.no, DialogInterface.OnClickListener{ dialog, which ->
                    if(deleteId!=null)
                    handleAlertDialog.onHandleDialog(false,deleteId)
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
        }

        fun  convertPurchaseHistoryToAddItemProxy(proxyId:Long,buyingId:Long,purchaseHistory: PurchaseHistory): AddItemProxy {
            var category=Category()
            category.categoryId= purchaseHistory.getCategoryId()!!
            category.setCategoryName(purchaseHistory.getCategoryName()!!)
            var shoppingItem=ShoppingItem()
            shoppingItem.productId= purchaseHistory.getShoppingId()!!
            shoppingItem.setProductName(purchaseHistory.getCategoryName()!!)
            shoppingItem.CategoryId= purchaseHistory.getCategoryId()!!
            var purchaseItem=PurchaseItem()
            purchaseItem.setCost(purchaseHistory.getCost()!!)
            purchaseItem.setPurchaseId(purchaseHistory.getPurchaseId()!!)
            purchaseItem.setDescription(purchaseHistory.getDescipt()!!)
            purchaseItem.productId=(purchaseHistory.getShoppingId()!!)
            purchaseItem.buyingId=buyingId
            return AddItemProxy(proxyId,category,shoppingItem,purchaseItem)
        }
    }
}