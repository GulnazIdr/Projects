//package com.example.englishreviser.room
//
//import kotlinx.coroutines.flow.Flow
//
//class OfflineUsersRepository(private val userInfoDAO: UserInfoDAO): UserInfoRepository {
//    override fun getAllUsersInfo(): Flow<List<UserInfoEntity>> = userInfoDAO.getUsersInfo()
//
//    override fun getUserInfo(name: String): Flow<UserInfoEntity?> = userInfoDAO.getUserInfo(name)
//
//    override suspend fun insertUser(userInfo: UserInfoEntity) = userInfoDAO.insertUser(userInfo)
//
//    override suspend fun deleteUser(userInfo: UserInfoEntity) = userInfoDAO.deleteUser(userInfo)
//
//    override suspend fun updateUser(userInfo: UserInfoEntity) = userInfoDAO.updateUser(userInfo)
//}