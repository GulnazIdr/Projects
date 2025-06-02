package com.example.englishreviser.room

sealed interface ActionEvent {
    object SaveFolder: ActionEvent
    data class AddFolder(val folderName: String, val userName: String): ActionEvent
    data class DeleteFolder(val folder: FolderInfoEntity): ActionEvent

    object SaveCard: ActionEvent
    data class AddCard(val words: WORDS, val value: String, val folderId: Int) : ActionEvent
    data class DeleteCard(val card: CardInfoEntity) : ActionEvent
    data class GetCardsByFolder(val folderId: Int): ActionEvent
}

enum class WORDS {NATIVE, TRANSLATED}