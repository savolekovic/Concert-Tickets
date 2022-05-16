package com.example.concerttickets.services.persistence

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferences
@Inject constructor(@ApplicationContext private val context: Context) {

    private val prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    var hasNotLoadedFromNetwork: Boolean
        get() = prefs.getBoolean(HAS_NOT_LOADED, true)
        set(value) = prefs.edit().putBoolean(HAS_NOT_LOADED, value).apply()

    companion object {
        private const val PREFERENCES = "com.example.concerttickets.prefs"
        private const val HAS_NOT_LOADED = "HAS_LOADED"
    }

}