package com.example.roomtransactionlock.database

import android.util.Log
import androidx.room.*
import com.example.roomtransactionlock.MainActivity
import kotlinx.coroutines.flow.Flow

private const val TAG = "DB"

@Dao
abstract class ExampleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertExampleEntity(entity: ExampleEntity)

    @Query("SELECT * FROM ExampleEntity WHERE id=:id")
    protected abstract suspend fun getExampleEntity(id: Int): ExampleEntity?

    @Query("SELECT * FROM ExampleEntity WHERE id=:id")
    abstract fun getExampleEntityFlow(id: Int): Flow<ExampleEntity?>

    @Transaction
    open suspend fun compareAndUpdateExampleEntity(entity: ExampleEntity) {
        Log.d(TAG, "Starting to compare and update $entity")
        val existingEntity = getExampleEntity(entity.id)

        Log.d(TAG, "Starting artificial delay.")
        Thread.sleep(MainActivity.ARTIFICIAL_DELAY_MS)

        Log.d(TAG, "Existing entity $existingEntity")
        if (existingEntity?.code != entity.code) {
            insertExampleEntity(entity)
        }

        Log.d(TAG, "Finished comparing.")
    }
}