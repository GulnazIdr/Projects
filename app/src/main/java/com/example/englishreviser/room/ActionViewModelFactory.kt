package com.example.englishreviser.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActionViewModelFactory(private val folderDAO: FolderDAO, private val cardDAO: CardDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ActionViewModel::class.java))
            return ActionViewModel(folderDAO, cardDAO) as T

        throw IllegalArgumentException("Wrong view model")
    }
}