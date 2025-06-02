package com.example.englishreviser.room

import com.example.englishreviser.room.UserInfoEntity
import kotlinx.coroutines.flow.Flow

interface UserInfoRepository {
    fun getAllUsersInfo(): Flow<List<UserInfoEntity>>

    fun getUserInfo(id: Int): Flow<UserInfoEntity?>

    suspend fun insertUser(userInfo: UserInfoEntity)

    suspend fun deleteUser(userInfo: UserInfoEntity)

    suspend fun updateUser(userInfo: UserInfoEntity)
}