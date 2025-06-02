package com.example.englishreviser.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserInfoViewModelFactory(private val dao: UserInfoDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UserInfoViewModel::class.java))(
            return UserInfoViewModel(dao) as T
        )

        throw IllegalArgumentException("Unknown viewmodel")
    }
}