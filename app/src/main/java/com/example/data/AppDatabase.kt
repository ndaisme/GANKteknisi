package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Customer::class, ServiceJob::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
