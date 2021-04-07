package com.rigrex.wallpaperapp.helpUtility

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.preference.Preference
import com.rigrex.wallpaperapp.database.dataModels.DownloadModel

class Preferences {
    companion object {
        var preferences: SharedPreferences? = null
        const val TIME = "time_id"
        const val ENABLED = "enable_id"
        const val CURRENT_WALLPAPER = "current_wallp_id"
        const val DEFAULT_VALUES = "nil"

        fun initialize(context: Context) {
            if (preferences == null)
                preferences = context.getSharedPreferences("wal_rex_new", Context.MODE_PRIVATE)
        }

        fun setCurrent(downloadModel: DownloadModel) {
            preferences?.edit {
                putString(CURRENT_WALLPAPER, downloadModel.id)
            }
        }

        fun getCurrent(): String {
            return preferences?.getString(CURRENT_WALLPAPER, DEFAULT_VALUES) ?: DEFAULT_VALUES
        }

        fun setEnabled(value: Boolean) {
            preferences?.edit(commit = true) {
                putBoolean(ENABLED, value)
            }
        }

        fun isEnabled(): Boolean {
            return preferences?.getBoolean(ENABLED, false) ?: false
        }

        fun setTime(value: String) {
            preferences?.edit(commit = true) {
                putString(TIME, value)
            }
        }

        fun getTime(): String {
            return preferences?.getString(TIME, "5 min") ?: "1 min"
        }
    }
}