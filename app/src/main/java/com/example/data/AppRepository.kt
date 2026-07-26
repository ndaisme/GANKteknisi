package com.example.data

import android.content.Context
import androidx.room.Room

class AppRepository(private val appDao: AppDao) {
    val allCustomers = appDao.getAllCustomers()
    val allServiceJobs = appDao.getAllServiceJobs()

    suspend fun insertCustomer(customer: Customer) {
        appDao.insertCustomer(customer)
    }

    suspend fun insertServiceJob(serviceJob: ServiceJob) {
        appDao.insertServiceJob(serviceJob)
    }

    suspend fun updateServiceStatus(id: String, status: String) {
        appDao.updateServiceStatus(id, status)
    }

    suspend fun updateWarranty(id: String, tanggalMulai: Long, jenis: String, durasiHari: Int, riwayatKlaim: String) {
        appDao.updateWarranty(id, tanggalMulai, jenis, durasiHari, riwayatKlaim)
    }
}

class AppContainer(private val context: Context) {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(context, AppDatabase::class.java, "gank_database")
            .fallbackToDestructiveMigration()
            .build()
    }
    
    val appRepository: AppRepository by lazy {
        AppRepository(database.appDao())
    }
}
