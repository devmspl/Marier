package com.app.marier.utils


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class CSPreferences {
    companion object{
        const val PREF = "pref"

        fun getPref(activity: Activity): SharedPreferences {
            return activity.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        }

        fun putString(activity: Activity, key: String?, value: String?) {
            val editor = getPref(activity).edit()
            editor.putString(key, value)
            editor.commit()
        }

        fun readString(activity: Activity, key: String?): String? {
            return getPref(activity).getString(key, "")
        }

        fun clearAll(activity: Activity) {
            getPref(activity).edit().clear().commit()
        }

    }
}