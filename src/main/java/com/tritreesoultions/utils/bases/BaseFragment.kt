package com.lamiatelecom.base

import android.app.Activity
import android.content.Intent
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class BaseFragment: Fragment() {
    inline fun<reified A: Activity> createIntent() = Intent(activity, A::class.java)

    fun navigate(intent: Intent, requestCode: Int?){
        when(requestCode){
            null -> startActivity(intent)
            else -> startActivityForResult(intent, requestCode)
        }
    }

    fun navigate(fragment: Fragment, @IdRes idRes: Int, addToBackStack: Boolean, backStackName: String?){
        val transaction = requireActivity().supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(idRes, fragment, fragment::class.simpleName)
            if(addToBackStack){
                addToBackStack(backStackName)
            }
            commit()
        }
    }
}