package com.example.englishreviser.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class ActionViewModel(private val folderDAO: FolderDAO, private val cardDAO: CardDAO): ViewModel() {
    var card by mutableStateOf<CardInfoEntity?>(null)
        private set

    var folder by mutableStateOf<FolderInfoEntity?>(null)
        private set

    var folderId by mutableIntStateOf(0)

    fun onEvent(event: ActionEvent){
        when(event){
            //cards
            is ActionEvent.AddCard -> { card = event.card }

            is ActionEvent.DeleteCard -> TODO()

            ActionEvent.SaveCard -> {
                viewModelScope.launch {
                    val card = CardInfoEntity(
                        nativeWord = card?.nativeWord ?: "",
                        translatedWord = card?.translatedWord ?: "",
                        folderId = folderId
                    )
                    cardDAO.insertCards(card)
                }
            }

            //folders
            is ActionEvent.AddFolder -> { folder = event.folder }

            is ActionEvent.DeleteFolder -> TODO()

            ActionEvent.SaveFolder -> {
                viewModelScope.launch {
                    val folder = FolderInfoEntity(
                        folderName = folder?.folderName ?: "",
                        userName = folder?.userName ?: ""
                    )
                    folderDAO.insertFolder(folder)
                }
            }
        }
    }
}