package com.tritreesoultions.utils.helpers

import android.content.Context
import android.widget.Toast

class Toaster {

    fun showToast(context: Context, message: String, length: Int = Toast.LENGTH_SHORT){
        toast?.cancel()
        toast = Toast.makeText(context, message, length)
        toast?.show()
    }

    companion object{
        var toast: Toast? = null
    }
}