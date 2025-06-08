package com.example.englishreviser.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInfoDAO {
    //user data
    @Upsert
    suspend fun insertUser(userInfo: UserInfoEntity)

    @Delete
    suspend fun deleteUser(userInfo: UserInfoEntity)

    @Update
    suspend fun updateUser(userInfo: UserInfoEntity)

    @Query("SELECT * FROM user_data")
    fun getUsersInfo(): Flow<List<UserInfoEntity>>

    @Query("SELECT * FROM USER_DATA WHERE name = :name")
    fun getUserInfo(name: String): Flow<UserInfoEntity?>

    @Query("SELECT password FROM USER_DATA WHERE name = :name LIMIT 1")
    fun getPasswordByUserName(name: String): String?
}