package com.example.englishreviser.room

sealed interface UserInfoEvent {
    object SaveUserInfo: UserInfoEvent
    data class InsertUserInfo(val fields: FIELDS, val value: String): UserInfoEvent
    data class DeleteUserInfo(val userInfo: UserInfoEntity): UserInfoEvent
}

enum class FIELDS {NAME, EMAIL, PHONE, PASSWORD}