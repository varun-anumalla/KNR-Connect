package com.example.knrconnect

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * This is the main database for our app, built using Room.
 *
 * Think of this as the main entry point to all our saved business data. We set it up here
 * to make sure the app only ever creates and uses one single instance of the database,
 * which is a safe and efficient way to handle data.
 */
@Database(entities = [Business::class], version = 3) // Make sure this version is up-to-date
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    /**
     * This function provides the DAO (Data Access Object) that we use
     * to actually run our database commands, like getting or saving businesses.
     */
    abstract fun businessDao(): BusinessDao

    /**
     * This companion object is where we'll manage creating the database itself.
     */
    companion object {
        /**
         * This holds the single instance of our database.
         * The "@Volatile" keyword makes sure that when we set this variable,
         * all parts of our app see the change immediately. It helps prevent bugs.
         */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * This function is how other parts of the app get access to the database.
         * It will either return the database if it already exists, or it will create it
         * for the first time. The "synchronized" block is important because it prevents
         * different parts of the app from trying to create the database at the exact same time,
         * which could cause crashes.
         *
         * @param context The application context needed to create the database.
         * @return The one and only instance of our AppDatabase.
         */
        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "knr_connect_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}