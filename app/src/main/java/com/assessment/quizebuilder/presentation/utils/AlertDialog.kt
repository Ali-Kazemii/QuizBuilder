package com.assessment.quizebuilder.presentation.utils

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.assessment.quizebuilder.R

fun Activity.exitAlertDialog() {
    AlertDialog.Builder(this).setMessage(getString(R.string.alert_message))
        .setPositiveButton(getString(R.string.ok)) { _, _ ->
            finishAffinity()
        }
        .setNegativeButton(getString(R.string.no)) { _, _ ->

        }.show()
}

fun Context.alertDialog(
    title :String = "",
    message: String = "",
    listener: ()->Unit
){
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(getString(R.string.ok)) { _, _ ->
          listener.invoke()
        }
        .setNegativeButton(getString(R.string.no)) { _, _ ->}
        .show()
}