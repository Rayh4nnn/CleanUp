package com.eco.cleanup.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    companion object {
        val MUSIK_LATAR  = booleanPreferencesKey("musik_latar")
        val EFEK_SUARA   = booleanPreferencesKey("efek_suara")
    }

    val musikLatar: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[MUSIK_LATAR] ?: true }

    val efekSuara: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[EFEK_SUARA] ?: true }

    suspend fun setMusikLatar(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[MUSIK_LATAR] = enabled }
    }

    suspend fun setEfekSuara(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[EFEK_SUARA] = enabled }
    }
}

