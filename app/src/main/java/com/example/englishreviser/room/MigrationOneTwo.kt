package com.example.englishreviser.room

import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class MigrationOneTwo : AutoMigrationSpec{
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Recreate the table with NOT NULL constraint
            database.execSQL("""
            CREATE TABLE new_folder_data (
                folderId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                folderName TEXT NOT NULL,
                userId INTEGER NOT NULL
            )
        """)
            database.execSQL("""
            INSERT INTO new_folder_data (folderId, folderName, userId)
            SELECT folderId, folderName, userId FROM folder_data
        """)
            database.execSQL("DROP TABLE folder_data")
            database.execSQL("ALTER TABLE new_folder_data RENAME TO folder_data")
        }
    }
}