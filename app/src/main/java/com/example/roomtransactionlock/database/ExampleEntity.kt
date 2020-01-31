package com.example.roomtransactionlock.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExampleEntity(
    @PrimaryKey
    val id: Int,
    val code: Long
)