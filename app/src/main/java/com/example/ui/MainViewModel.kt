package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.GankApplication
import com.example.data.AppRepository
import com.example.data.Customer
import com.example.data.ServiceJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    val customers: StateFlow<List<Customer>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val serviceJobs: StateFlow<List<ServiceJob>> = repository.allServiceJobs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCustomer(nama: String, telepon: String, alamat: String, catatan: String) {
        viewModelScope.launch {
            val newCustomer = Customer(
                id = UUID.randomUUID().toString(),
                nama = nama,
                telepon = telepon,
                alamat = alamat,
                catatan = catatan
            )
            repository.insertCustomer(newCustomer)
        }
    }

    fun addServiceJob(
        customerId: String,
        brand: String,
        model: String,
        imei: String,
        keluhan: String,
        kelengkapan: String,
        passwordHp: String,
        estimasi: Double,
        dp: Double
    ) {
        viewModelScope.launch {
            val newJob = ServiceJob(
                id = UUID.randomUUID().toString(),
                customerId = customerId,
                brand = brand,
                model = model,
                imei = imei,
                keluhan = keluhan,
                kelengkapan = kelengkapan,
                passwordHp = passwordHp,
                diagnosa = "Menunggu Pengecekan",
                status = "Menunggu",
                estimasiBiaya = estimasi,
                dp = dp
            )
            repository.insertServiceJob(newJob)
        }
    }
    
    fun updateServiceStatus(id: String, status: String) {
        viewModelScope.launch {
            repository.updateServiceStatus(id, status)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as GankApplication
                return MainViewModel(application.container.appRepository) as T
            }
        }
    }
}
