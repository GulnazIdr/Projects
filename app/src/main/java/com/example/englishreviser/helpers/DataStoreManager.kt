package com.example.englishreviser.helpers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore:  DataStore<Preferences> by preferencesDataStore("data_store")

class DataStoreManager(val context: Context) {

    val CURRENT_USER = stringPreferencesKey("currentUser")

    suspend fun saveCurrentUser(userName: String){
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(CURRENT_USER.toString())] = userName
        }
    }

    val getCurrentUser: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[CURRENT_USER] ?: ""
    }
}