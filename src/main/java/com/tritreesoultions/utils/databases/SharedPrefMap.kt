package com.tritreesoultions.utils.databases

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

interface Cleanable {
    fun clear()
}

open class SharedPrefMap(context: Context) : Cleanable {
    val sharedPref: SharedPreferences =
        context.getSharedPreferences("mySharedPref", Context.MODE_PRIVATE)

    inline fun <reified T : Any> storedMap(
        defaultValue: T? = when (T::class) {
            Int::class -> 0
            Long::class -> 0L
            String::class -> ""
            Float::class -> 0F
            Boolean::class -> false
            else -> null
        } as T?
    ) = sharedPref.delegates(defaultValue, T::class)

    override fun clear() {
        sharedPref.edit().clear().apply()
    }

    fun <T : Any> SharedPreferences.delegates(defaultValue: T?, kClass: KClass<T>) =
        object : ReadWriteProperty<Any, T?> {

            val getter: SharedPreferences.(key: String, defaultValue: T?) -> T? = when (kClass) {
                Int::class -> SharedPreferences::getInt
                Float::class -> SharedPreferences::getFloat
                Boolean::class -> SharedPreferences::getBoolean
                Long::class -> SharedPreferences::getLong
                else -> SharedPreferences::getString
            } as SharedPreferences.(key: String, defaultValue: T?) -> T?

            val setter: SharedPreferences.Editor.(key: String, defaultValue: T?) -> SharedPreferences.Editor =
                when (kClass) {
                    Int::class -> SharedPreferences.Editor::putInt
                    Boolean::class -> SharedPreferences.Editor::putBoolean
                    Float::class -> SharedPreferences.Editor::putFloat
                    Long::class -> SharedPreferences.Editor::putLong
                    else -> SharedPreferences.Editor::putString
                } as SharedPreferences.Editor.(key: String, defaultValue: T?) -> SharedPreferences.Editor

            override fun getValue(thisRef: Any, property: KProperty<*>): T? {
                val target = property.name
                return if (contains(target)) when (kClass) {
                    Int::class, Long::class, Float::class, Boolean::class, String::class -> getter(
                        target,
                        defaultValue!!
                    )
                    else -> Gson().fromJson(getString(target, ""), kClass.java)
                } else null
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
                val target = property.name
                if (value == null) {
                    edit().remove(target).apply()
                } else {
                    when (kClass) {
                        Int::class, Long::class, Float::class, Boolean::class, String::class -> edit().setter(
                            target,
                            value
                        ).apply()
                        else -> edit().putString(target, Gson().toJson(value)).apply()
                    }
                }
            }
        }
}