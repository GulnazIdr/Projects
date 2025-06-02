package com.example.englishreviser.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserInfoViewModel( private val dao: UserInfoDAO ) : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var password by mutableStateOf("")

    fun onEvent(event : UserInfoEvent){
        when(event) {
            is UserInfoEvent.InsertUserInfo -> {
                when(event.fields){
                    FIELDS.NAME -> name = event.value
                    FIELDS.EMAIL -> email = event.value
                    FIELDS.PHONE -> phone = event.value
                    FIELDS.PASSWORD -> password = event.value
                }
            }

            is UserInfoEvent.DeleteUserInfo -> TODO()

            UserInfoEvent.SaveUserInfo -> {
                viewModelScope.launch {
                    val userInfo = UserInfoEntity(
                        name = name,
                        email = email,
                        phone = "7$phone",
                        password =  password
                    )

                    dao.insertUser(userInfo)
                }
            }

            is UserInfoEvent.GetUserInfo -> {
                viewModelScope.launch {
                    dao.getUserInfo(name)
                }
            }
        }

    }
}