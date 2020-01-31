package com.example.roomtransactionlock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.roomtransactionlock.database.ExampleDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.random.Random

private const val TAG = "Activity"

class MainActivity : AppCompatActivity() {

    private val textView by lazy {
        findViewById<TextView>(R.id.random_number_text)
    }

    private val database by lazy {
        Room.databaseBuilder(this, ExampleDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun CoroutineScope.startUpdatingDatabase() = launch(Dispatchers.IO) {
        while (coroutineContext.isActive) {
            Log.d(TAG, "Entering database update loop.")
            val firstInsertJob = launch { database.insertRandomEntity() }
            val artificialDelayMs = (ARTIFICIAL_DELAY_MS * Random.nextFloat()).toLong()

            Log.d(TAG, "Delaying for $artificialDelayMs")
            delay(artificialDelayMs)

            Log.d(TAG, "Cancelling database job.")
            firstInsertJob.cancel()

            Log.d(TAG, "Inserting second entity.")
            database.insertRandomEntity()
            Log.d(TAG, "Finished inserting second entity.")
        }
    }

    private fun CoroutineScope.startCollectingRandomEntity() = launch(Dispatchers.Main) {
        database.getRandomEntity().collect {
            val code = it?.code?.toString() ?: "Null Code"
            Log.d(TAG, "Updating view text with $code")
            textView.text = code
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenCreated {
            startCollectingRandomEntity()
            startUpdatingDatabase()
        }
    }

    companion object {
        const val ARTIFICIAL_DELAY_MS = 50L
    }
}
