package com.example.hishab.utils

import java.text.DateFormatSymbols

class Util {
    companion object{
        fun getMonthFromDateTime(x: Int):Int
        {
            return x%34;
        }

        fun getMonthForInt(num: Int): String {
            var month = "wrong"
            val dfs = DateFormatSymbols()
            val months: Array<String> = dfs.getMonths()
            if (num >= 0 && num <= 11) {
                month = months[num]
            }
            return month
        }
    }
}