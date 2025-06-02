package com.example.englishreviser

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.example.englishreviser.room.UserInfoDAO
import com.example.englishreviser.room.UserInfoViewModel
import com.example.englishreviser.room.UserInfoViewModelFactory
import com.example.englishreviser.room.UsersDatabase
import com.example.englishreviser.ui.theme.EnglishReviserTheme

class LogInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this).application
        val userDao: UserInfoDAO = UsersDatabase.getDatabase(application).userDao()
        val viewModelFactory = UserInfoViewModelFactory(userDao)
        val dbViewModel = ViewModelProvider(this, viewModelFactory)[(UserInfoViewModel::class.java)]

        enableEdgeToEdge()
        setContent {
            lifecycle.coroutineScope
            EnglishReviserTheme {
                LogInForm(
                    dbViewModel,
                    userDao,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun LogInForm(
    dbViewModel: UserInfoViewModel,
    userDAO: UserInfoDAO,
    modifier: Modifier = Modifier
){
    var username by remember { mutableStateOf("") }
    var userPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    val isValidUser by dbViewModel.checkUser(username, userPassword, userDAO).collectAsState(initial = false)

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Log in",
            fontSize = 30.sp,
            modifier = Modifier.padding(vertical = 15.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = {Text("Name")},
            isError = false,
            supportingText = {Text("")}
        )

        OutlinedTextField(
            value = userPassword,
            onValueChange = { userPassword = it },
            label = {Text("Password")},
            isError = false,
            supportingText = {Text("")},
            visualTransformation = PasswordVisualTransformation()
        )

        OutlinedButton(
            onClick = {
                if(isValidUser)
                    context.startActivity(Intent(context, NavigationDrawer::class.java))
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
        ) {
            Text("Log in")
        }

        Text(
            text = "Registration",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(10.dp)
                .clickable(onClick = {
                    context.startActivity(Intent(context, MainActivity::class.java))
                })
        )
    }
}