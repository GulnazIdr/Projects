package com.example.englishreviser.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "card_data")
data class CardInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val cardId:Int = 0,
    val nativeWord:String,
    val translatedWord: String,
    val folderId: Int
)
