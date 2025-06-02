package com.example.englishreviser.helpers

import android.content.Intent
import android.os.Bundle
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

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataStoreManager = DataStoreManager(this)

        setContent {
            EnglishReviserTheme {
                var isRegistered by remember { mutableStateOf("developer") }

                LaunchedEffect(Unit) {
                    dataStoreManager.getCurrentUser.collect { info ->
                        isRegistered = info
                    }
                }

                if (isRegistered.equals("developer"))
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                else
                    startActivity(Intent(applicationContext, NavigationDrawer::class.java))

                finish()
            }
        }
    }
}