package com.example.roomtransactionlock.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kotlin.random.Random

@Database(entities = [ExampleEntity::class], version = 1)
abstract class ExampleDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao

    fun getRandomEntity() = exampleDao().getExampleEntityFlow(0)

    open suspend fun insertRandomEntity() {
        val randomCode = Random.nextLong()
        val entity = ExampleEntity(0, randomCode)
        exampleDao().compareAndUpdateExampleEntity(entity)
    }
}