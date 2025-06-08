package com.example.englishreviser.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.englishreviser.R
import com.example.englishreviser.room.UserInfoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userInfo: UserInfoEntity?
){

    var userName by remember { mutableStateOf(userInfo?.name ?: "") }
    var userEmail by remember { mutableStateOf(userInfo?.email ?: "") }
    var userPhone by remember { mutableStateOf(userInfo?.phone ?: "") }
    var userPassword by remember { mutableStateOf(userInfo?.password ?: "") }

    var textStyle = TextStyle.Default.copy(fontSize = 18.sp)
    var textStyle2 = TextStyle.Default.copy(fontSize = 15.sp)
    var modifierText = Modifier.padding(top = 5.dp, bottom = 17.dp)

    var showPassword by rememberSaveable { mutableStateOf(false) }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "userImage",
                    Modifier.width(100.dp).height(100.dp)
                )
                BasicTextField(
                    value = userName,
                    onValueChange = {userName = it},
                    enabled = false,
                    textStyle = TextStyle.Default.copy(fontSize = 20.sp)
                )
            }

            Column( modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 10.dp)
            ) {
                BasicTextField(
                    value = userEmail,
                    onValueChange = {userEmail = it},
                    enabled = false,
                    textStyle = textStyle
                )
                Text(
                    text = "User email",
                    style = textStyle2,
                    modifier = modifierText
                )

                BasicTextField(
                    value = userPhone,
                    onValueChange = {userPhone = it},
                    enabled = false,
                    textStyle = textStyle
                )
                Text(
                    text = "User phone",
                    modifier = modifierText,
                    style = textStyle2
                )

                BasicTextField(
                    value = userPassword,
                    onValueChange = {userPassword = it},
                    enabled = false,
                    textStyle = textStyle,
                    visualTransformation = if(showPassword) VisualTransformation.None
                    else PasswordVisualTransformation()
                )
                Text(
                    text = "User password",
                    modifier = modifierText,
                    style = textStyle2
                )
            }
        }
}