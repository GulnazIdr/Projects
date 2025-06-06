package com.example.englishreviser.helpers

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.englishreviser.MainActivity
import com.example.englishreviser.NavigationDrawer
import com.example.englishreviser.ui.theme.EnglishReviserTheme
import kotlinx.coroutines.flow.first

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStoreManager = DataStoreManager(this)

        setContent {
            EnglishReviserTheme {
                LaunchedEffect(Unit) {
                    var isRegistered = dataStoreManager.getCurrentUser.first()
                    val destination = if (isRegistered.isBlank()) MainActivity::class.java
                                      else NavigationDrawer::class.java

                    startActivity(Intent(applicationContext, destination).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })

                    finish()
                }
            }
        }
    }
}