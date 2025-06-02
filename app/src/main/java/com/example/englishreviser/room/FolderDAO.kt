package com.example.englishreviser.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDAO {
    @Upsert
    suspend fun insertFolder(folder: FolderInfoEntity)

    @Delete
    suspend fun deleteFolders(folder: FolderInfoEntity)

    @Update
    suspend fun updateFolder(folder: FolderInfoEntity)

    @Query("SELECT * FROM FOLDER_DATA WHERE folderId = :folderId")
    fun getFolders(folderId: Int): Flow<List<FolderInfoEntity>>

    @Query("SELECT * FROM FOLDER_DATA WHERE userName = :userName")
    fun getFoldersByUser(userName: String): Flow<List<FolderInfoEntity>>
}