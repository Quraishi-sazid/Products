package com.example.hishab.utils

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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


    }


}