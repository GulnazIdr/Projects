package com.example.englishreviser.room

sealed interface ActionEvent {
    object SaveFolder: ActionEvent
    data class AddFolder(val folder: FolderInfoEntity): ActionEvent
    data class DeleteFolder(val folder: FolderInfoEntity): ActionEvent

    object SaveCard: ActionEvent
    data class AddCard(val card: CardInfoEntity) : ActionEvent
    data class DeleteCard(val card: CardInfoEntity) : ActionEvent
}