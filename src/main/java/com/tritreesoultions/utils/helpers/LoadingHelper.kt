package com.tritreesoultions.utils.helpers

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.tritreesoultions.utils.R

class LoadingHelper {
    fun getLoadingDialog(context: Context): AlertDialog {
        val dialog = AlertDialog.Builder(context)
        val dialogView = LayoutInflater.from(context).inflate(R.layout.layout_progress_dialog, null, false)
        dialog.setView(dialogView)
        dialog.setCancelable(false)

        return dialog.create()
    }
}