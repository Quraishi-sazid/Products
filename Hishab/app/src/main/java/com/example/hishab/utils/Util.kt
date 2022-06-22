package com.example.hishab.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TimePicker
import com.example.hishab.interfaces.IHandleAlertDialog
import com.example.hishab.models.ShoppingItemProxy
import com.example.hishab.models.entities.Category
import com.example.hishab.models.PurchaseHistory
import com.example.hishab.models.entities.PurchaseItem
import com.example.hishab.models.entities.Product
import io.reactivex.Observable
import java.text.DateFormatSymbols
import java.util.*
import kotlin.math.abs


class Util {
    companion object{

        fun getMonthForInt(num: Int): String {
            var month = "wrong"
            val dfs = DateFormatSymbols()
            val months: Array<String> = dfs.getMonths()
            if (num >= 0 && num <= 11) {
                month = months[num-1]
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

        fun getCurrentMonth():Int{
            return Calendar.getInstance().get(Calendar.MONTH)+1
        }
        fun getCurrentYear():Int{
            return Calendar.getInstance().get(Calendar.YEAR)
        }
        fun getCurrentDay():Int{
            return Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        }
        fun getCurrentMili():Long{
            return System.currentTimeMillis()
        }

        fun getTimePickerHour(milisecond : Long): Int {
            var calendar = Calendar.getInstance();
            calendar.timeInMillis = milisecond
            return calendar.get(Calendar.HOUR_OF_DAY)
        }

        fun getTimePickerMin(milisecond : Long): Int {
            var calendar = Calendar.getInstance();
            calendar.timeInMillis = milisecond
            return calendar.get(Calendar.MINUTE)
        }

        fun getMilisecFromTimePicker(calendar: Calendar,timePicker : TimePicker): Long {
            var y = calendar.get(Calendar.YEAR)
            var m = calendar.get(Calendar.MONTH)
            var d = calendar.get(Calendar.DAY_OF_MONTH)
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour);
            calendar.set(Calendar.MINUTE, timePicker.minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.time.time
        }


        fun getViewSwipeObservable(view: View): Observable<Pair<Float,Float>>
        {
            return Observable.create<Pair<Float,Float>> {
                view.setOnTouchListener(object : View.OnTouchListener {
                    var downCordinateX = 0.0f
                    var downCordinateY = 0.0f
                    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
                        when (event?.getAction()) {
                            MotionEvent.ACTION_DOWN -> {
                                downCordinateY = event.y
                                downCordinateX = event.x
                                Log.v("GTAG", "saved downCordinate: $downCordinateX")
                            }
                            MotionEvent.ACTION_MOVE -> {
                                Log.v("GTAG", "moving: (" + event.x + ", " + event.y + ")")
                                if(downCordinateX<0.0001)
                                {
                                    downCordinateX=event.x
                                    downCordinateY=event.y
                                }
                            }
                            MotionEvent.ACTION_UP -> {
                                Log.v("GTAG", "up Cordinate: " + event.y)
                                if(abs(downCordinateX-event.x) >10 && abs(downCordinateY - event.y) <300)
                                {
                                    it.onNext(Pair(downCordinateX-event.x,downCordinateY - event.y))
                                    downCordinateX=0.0f
                                    downCordinateY=0.0f
                                    return true
                                }
                                downCordinateX=0.0f
                                downCordinateY=0.0f
                            }
                        }
                        return false
                    }
                })
            }
        }


    }


}