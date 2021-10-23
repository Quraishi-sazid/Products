package com.example.hishab.utils

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
        fun getType(item:Any):String?
        {
            return item::class.simpleName
        }

    }
}