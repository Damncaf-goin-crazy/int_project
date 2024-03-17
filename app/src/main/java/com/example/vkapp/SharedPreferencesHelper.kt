package com.example.vkapp

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.vkapp.SharedPreferencesHelper.Companion.CONFIGS_LIST
import com.google.gson.Gson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow

class SharedPreferencesHelper(context: Context) {

    companion object {
        const val SHARED_PREFS_NAME = "MY_SHARED"
        const val PASSWORD = "PASSWORD"
        const val DEFAULT_PASSWORD = "null"
        const val CONFIGS_LIST = "LIST"
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }
    private val gson by lazy {
        Gson()
    }


    fun getPassword(): String? {
        if (!sharedPreferences.contains(PASSWORD)) return null
        return sharedPreferences.getString(PASSWORD, DEFAULT_PASSWORD)
    }

    fun setPassword(password: String) {
        with(sharedPreferences.edit()) {
            putString(PASSWORD, password)
        }.apply()
    }

    fun getListFlow() = callbackFlow<List<WebSiteConfig>> {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            Log.d("121212", key.toString())
            if (key == CONFIGS_LIST) {
                trySend(getWebSiteConfigs())
            }
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        if (sharedPreferences.contains(CONFIGS_LIST)) {
            send(getWebSiteConfigs())
        }
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.buffer(Channel.UNLIMITED)

    private fun getWebSiteConfigs(): List<WebSiteConfig> {
        if (sharedPreferences.getString(CONFIGS_LIST, null) == "null") return emptyList()
        return sharedPreferences.getString(CONFIGS_LIST, null)?.split("////")
            ?.mapNotNull { gson.fromJson(it, WebSiteConfig::class.java) } ?: emptyList()
    }

    fun addWebSiteSettings(config: WebSiteConfig) {
        with(sharedPreferences.edit()) {
            val current = sharedPreferences.getString(CONFIGS_LIST, null)
            putString(
                CONFIGS_LIST,
                "${current ?: ""}${gson.toJson(config, WebSiteConfig::class.java)}////"
            )
        }.apply()
    }

    fun updateWebSiteConfig(config: WebSiteConfig) {
        val currentList = getWebSiteConfigs()
        var str = ""
        for (i in currentList.indices) {
            str += if (currentList[i].id == config.id) {
                gson.toJson(config, WebSiteConfig::class.java) + "////"
            } else {
                gson.toJson(currentList[i], WebSiteConfig::class.java) + "////"
            }

        }

        with(sharedPreferences.edit()) {
            putString(CONFIGS_LIST, str)
        }.apply()

    }

}
