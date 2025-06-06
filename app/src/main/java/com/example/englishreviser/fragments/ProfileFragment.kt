package com.example.englishreviser.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishreviser.R
import com.example.englishreviser.room.UserInfoEntity
import com.example.englishreviser.ui.theme.EnglishReviserTheme

@Composable
fun ProfileScreen(
    userInfo: UserInfoEntity?
){
    var userName by remember { mutableStateOf(userInfo?.name ?: "") }
    var userEmail by remember { mutableStateOf(userInfo?.email ?: "") }
    var userPhone by remember { mutableStateOf(userInfo?.phone ?: "") }
    var userPassword by remember { mutableStateOf(userInfo?.password ?: "") }

    var colors =TextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        disabledTextColor = Color.Black,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        errorContainerColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent, // Remove underline when focused
        unfocusedIndicatorColor = Color.Transparent, // Remove underline when not focused
        disabledIndicatorColor = Color.Transparent)

    var textStyle = TextStyle.Default.copy(fontSize = 16.sp)

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "userImage",
                Modifier.width(100.dp).height(100.dp)
            )
            TextField(
                value = userName,
                onValueChange = {userName = it},
                enabled = false,
                colors = colors,
                textStyle = TextStyle.Default.copy(fontSize = 20.sp)
            )
        }

        TextField(
            value = userEmail,
            onValueChange = {userEmail = it},
            enabled = false,
            colors = colors,
            textStyle = textStyle
        )
        Text(text = "User email")

        TextField(
            value = userPhone,
            onValueChange = {userPhone = it},
            enabled = false,
            colors = colors,
            textStyle = textStyle
        )
        Text(text = "User phone")

        TextField(
            value = userPassword,
            onValueChange = {userPassword = it},
            enabled = false,
            colors = colors,
            textStyle = textStyle
        )
        Text(text = "User password")
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegistrationPreview(){
    ProfileScreen(userInfo = UserInfoEntity("gulnaz", "gul@gmai.com", "79962559708","5343" ))
}