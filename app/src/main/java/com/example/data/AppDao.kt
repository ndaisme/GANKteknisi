package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM customers ORDER BY nama ASC")
    fun getAllCustomers(): Flow<List<Customer>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer)

    @Query("SELECT * FROM service_jobs ORDER BY createdAt DESC")
    fun getAllServiceJobs(): Flow<List<ServiceJob>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServiceJob(serviceJob: ServiceJob)

    @Query("UPDATE service_jobs SET status = :status WHERE id = :id")
    suspend fun updateServiceStatus(id: String, status: String)
}
