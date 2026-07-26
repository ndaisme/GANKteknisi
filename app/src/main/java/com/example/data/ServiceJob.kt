package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_jobs")
data class ServiceJob(
    @PrimaryKey val id: String, // UUID
    val customerId: String,
    val brand: String,
    val model: String,
    val imei: String,
    val keluhan: String,
    val kelengkapan: String = "",
    val passwordHp: String = "",
    val diagnosa: String,
    val status: String, // Menunggu, Pengerjaan, Selesai
    val estimasiBiaya: Double,
    val dp: Double = 0.0,
    val createdAt: Long = System.currentTimeMillis()
)
