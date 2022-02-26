package com.example.hishab.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import com.example.hishab.interfaces.IHandleAlertDialog
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.Product
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

        fun  convertPurchaseHistoryToAddItemProxy(proxyId:Long,buyingId:Long,purchaseHistory: PurchaseHistory): ShoppingItemProxy {
            var category=Category()
            category.categoryId= purchaseHistory.getCategoryId()!!
            category.setCategoryName(purchaseHistory.getCategoryName()!!)
            var shoppingItem=Product()
            shoppingItem.productId= purchaseHistory.getShoppingId()!!
            shoppingItem.setProductName(purchaseHistory.getItemName()!!)
            shoppingItem.categoryId= purchaseHistory.getCategoryId()!!
            var purchaseItem=PurchaseItem()
            purchaseItem.setCost(purchaseHistory.getCost()!!)
            purchaseItem.setPurchaseId(purchaseHistory.getPurchaseId()!!)
            purchaseItem.setDescription(purchaseHistory.getDescipt()!!)
            purchaseItem.productId=(purchaseHistory.getShoppingId()!!)
            purchaseItem.shoppingId=buyingId
            return ShoppingItemProxy(proxyId,category,shoppingItem,purchaseItem)
        }



    }
}