package com.example.knrconnect

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Business::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun businessDao(): BusinessDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "knr_connect_database"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}