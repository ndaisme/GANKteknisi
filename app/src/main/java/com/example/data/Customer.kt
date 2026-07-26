package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class Customer(
    @PrimaryKey val id: String, // UUID
    val nama: String,
    val telepon: String,
    val alamat: String,
    val catatan: String
)
