package com.github.hughnew.proxier3.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.makeToast(msg:CharSequence, shortDuration:Boolean = true) {
    Toast.makeText(this,msg,
        if (shortDuration) Toast.LENGTH_SHORT
        else Toast.LENGTH_LONG
    ).show()
}

fun View.makeSnack(msg:CharSequence, shortDuration:Boolean = true) {
    Snackbar.make(this,msg,
        if (shortDuration) Snackbar.LENGTH_SHORT
        else Snackbar.LENGTH_LONG
    ).show()
}