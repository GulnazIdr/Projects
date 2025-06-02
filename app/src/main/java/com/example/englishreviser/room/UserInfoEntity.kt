package com.example.englishreviser.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data")
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val name:String,

    val email: String,
    val phone: String,
    val password: String
)