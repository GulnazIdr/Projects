package com.example.englishreviser.fragments

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.englishreviser.MainActivity
import com.example.englishreviser.helpers.DataStoreManager
import com.example.englishreviser.room.UserInfoEntity
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    navController: NavHostController,
    drawerState: DrawerState,
    dataStoreManager: DataStoreManager,
    userInfo: UserInfoEntity?
){
    val context = LocalContext.current
    val standardModifier = Modifier.fillMaxWidth().padding(5.dp)
    var showProfile by remember { mutableStateOf(false) }

    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        val scope = rememberCoroutineScope()

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            listOf("Home", "Settings").forEach { screen ->
                Text(
                    text = screen,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(screen.lowercase())
                            scope.launch { drawerState.close() }
                        }
                        .padding(12.dp) //padding between items
                )
            }
            if (showProfile) {
                ProfileScreen(userInfo)
            }
            Row(modifier = standardModifier, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    showProfile = true
                }) {
                    Icon(Icons.Filled.PermIdentity, contentDescription = "Profile")
                }
                Text(text = "Profile")
            }

            Row(modifier = standardModifier, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    val intent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }

                    scope.launch {
                        dataStoreManager.saveCurrentUser("")
                    }

                    context.startActivity(intent)
                }) {
                    Icon(Icons.Filled.Logout, contentDescription = "Log out")
                }

                Text(text = "Log out")
            }
        }
    }
}