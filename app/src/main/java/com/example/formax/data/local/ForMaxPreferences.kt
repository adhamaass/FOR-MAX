package com.example.formax.data.local

import android.content.Context
import com.example.formax.domain.models.AppLanguage

object ForMaxPreferences {
    private const val PREFS_NAME = "formax_app_preferences"
    private const val KEY_LANGUAGE = "selected_app_language"

    fun getLanguage(context: Context): AppLanguage {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val code = prefs.getString(KEY_LANGUAGE, "ar") ?: "ar"
        return AppLanguage.fromCode(code)
    }

    fun setLanguage(context: Context, language: AppLanguage) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, language.code).apply()
    }
}
