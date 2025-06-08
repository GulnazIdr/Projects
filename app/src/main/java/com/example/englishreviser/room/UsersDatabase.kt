package com.example.englishreviser.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserInfoEntity::class,
        FolderInfoEntity::class,
        CardInfoEntity::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = MigrationOneTwo::class)
    ]
)

abstract class UsersDatabase : RoomDatabase() {
    abstract fun userDao(): UserInfoDAO
    abstract fun folderDao(): FolderDAO
    abstract fun cardDao(): CardDAO

    companion object{
        @Volatile
        private var Instance: UsersDatabase? = null

        fun getDatabase(context: Context): UsersDatabase{
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, UsersDatabase::class.java, "revisor.db")
                    .build()
                    .also { Instance = it }
            }
        }
    }


}

