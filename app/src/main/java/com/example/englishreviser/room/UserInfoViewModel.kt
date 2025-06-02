package com.example.englishreviser.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class UserInfoViewModel( private val dao: UserInfoDAO ) : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var phone by mutableStateOf("")
    var password by mutableStateOf("")

    fun checkUser(typedName: String, typedPassword: String, dao: UserInfoDAO): Flow<Boolean> = flow {
        var userPasswordMatch = dao.getPasswordByUserName(typedName).toString()
        emit(userPasswordMatch == typedPassword)
    }.flowOn(Dispatchers.IO)

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
        }
    }
}