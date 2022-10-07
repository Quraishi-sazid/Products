package com.example.hishab.utils

import android.R
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.Observable


class CustomAlertDialog<B : ViewDataBinding>(
    val context: Context,
    val layoutId: Int,
    val submitButtonId: Int = -1
) {
    lateinit var onSubmitButtonPressed: Observable<B>
    var onViewCreated: Observable<B>
    var alertDialog: AlertDialog

    init {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        val viewDataBinding =
            DataBindingUtil.inflate<B>(LayoutInflater.from(context), layoutId, null, false)
        dialogBuilder.setView(viewDataBinding.root)
        alertDialog = dialogBuilder.show()
        onViewCreated = Observable.create<B> { emitter ->
            emitter.onNext(viewDataBinding)
        }
        if (submitButtonId != -1) {
            onSubmitButtonPressed = Observable.create<B> { emitter ->
                viewDataBinding.root.findViewById<Button>(submitButtonId).setOnClickListener {
                    emitter.onNext(viewDataBinding)
                }
            }
        }
    }

    fun dismiss() {
        alertDialog.dismiss()
    }

}