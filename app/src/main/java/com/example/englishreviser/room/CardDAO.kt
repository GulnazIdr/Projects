package com.example.englishreviser.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDAO {
    @Upsert
    suspend fun insertCards(card: CardInfoEntity)

    @Delete
    suspend fun deleteCards(card: CardInfoEntity)

    @Update
    suspend fun updateCard(card: CardInfoEntity)

    @Query("SELECT * FROM CARD_DATA WHERE (cardId = :cardId AND folderId = :folderId)")
    fun getCards(cardId: Int, folderId: Int): Flow<List<CardInfoEntity>>

    @Query("SELECT * FROM CARD_DATA WHERE folderId = :folderId")
    fun getCardsByFolder(folderId: Int): Flow<List<CardInfoEntity>>
}