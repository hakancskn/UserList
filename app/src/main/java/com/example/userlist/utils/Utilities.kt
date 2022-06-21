package com.example.userlist.utils

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.example.userlist.R



    fun setErrorMessage(
        context: Context,
        description: String,
        positiveClick: (view: View, Dialog) -> Unit
    ) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.alert_dialog)
        dialog.setCancelable(false)

        val btnRetry = dialog.findViewById(R.id.retry_button) as Button
        val txtDesc = dialog.findViewById(R.id.txt_alert_dialog_desc) as TextView

        txtDesc.text = description

        btnRetry.setOnClickListener {
            positiveClick(it, dialog)
        }

        dialog.show()
    }


