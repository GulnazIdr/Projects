package com.example.englishreviser.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ActionViewModel(private val folderDAO: FolderDAO, private val cardDAO: CardDAO): ViewModel() {

    var nativeWord by mutableStateOf("")
    var translatedWord by mutableStateOf("")

    var folderName by mutableStateOf("")
    var userName by mutableStateOf("")

    fun onEvent(event: ActionEvent){
        when(event){
            //cards
            is ActionEvent.AddCard -> {
                when(event.words){
                    WORDS.NATIVE -> nativeWord = event.value
                    WORDS.TRANSLATED -> translatedWord = event.value
                }
            }

            is ActionEvent.DeleteCard -> TODO()

            is ActionEvent.GetCardsByFolder -> TODO()

            ActionEvent.SaveCard -> {
                viewModelScope.launch {
                    val card = CardInfoEntity(
                        nativeWord = nativeWord,
                        translatedWord = translatedWord,
                        folderId = 1 // TODO:
                    )
                    cardDAO.insertCards(card)
                }

            }

            //folders
            is ActionEvent.AddFolder -> {
                folderName = event.folderName
                userName = event.userName
            }

            is ActionEvent.DeleteFolder -> TODO()

            ActionEvent.SaveFolder -> {
                viewModelScope.launch {
                    val folder = FolderInfoEntity(
                        folderName = folderName,
                        userName = userName
                    )
                    folderDAO.insertFolder(folder)
                }
            }



        }
    }
}