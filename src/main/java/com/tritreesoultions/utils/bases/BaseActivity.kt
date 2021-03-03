package com.tritreesoultions.utils.bases

import android.app.Activity
import android.content.Intent
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class BaseActivity: AppCompatActivity() {
    inline fun<reified A: Activity> createIntent() = Intent(this, A::class.java)

    fun navigate(intent: Intent, requestCode: Int?){
        when(requestCode){
            null -> startActivity(intent)
            else -> startActivityForResult(intent, requestCode)
        }
    }

    fun navigateAsNewTask(intent: Intent){
        startActivity(intent)
        finish()
    }

    fun navigate(fragment: Fragment, @IdRes idRes: Int, addToBackStack: Boolean, backStackName: String?){
        val transaction = supportFragmentManager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            replace(idRes, fragment, fragment::class.simpleName)
            if(addToBackStack){
                addToBackStack(backStackName)
            }
            commit()
        }
    }
}