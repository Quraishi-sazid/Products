package com.example.hishab.models

import java.util.function.Predicate

data class TwoPredicate(val predicate1:Predicate<Int>,val predicate2:Predicate<Int>) {
    fun isFirstPredicate(position:Int):Boolean{
        return  predicate1.test(position)
    }
    fun isSecondPredicate(position:Int):Boolean{
        return  predicate2.test(position)
    }
}