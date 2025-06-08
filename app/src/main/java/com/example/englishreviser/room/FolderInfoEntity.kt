package com.example.englishreviser.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder_data")
data class FolderInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val folderId:Int = 0,
    val folderName:String,
    val userName: String
)