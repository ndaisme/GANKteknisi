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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class Sparepart(
    val id: String = UUID.randomUUID().toString(),
    val nama: String,
    val kategori: String,
    val hargaModal: Double,
    val hargaJual: Double,
    val stok: Int
)

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    private val _spareparts = MutableStateFlow<List<Sparepart>>(listOf(
        Sparepart(nama = "LCD Samsung A51", kategori = "Layar", hargaModal = 300000.0, hargaJual = 500000.0, stok = 5),
        Sparepart(nama = "Baterai iPhone X", kategori = "Baterai", hargaModal = 150000.0, hargaJual = 250000.0, stok = 10),
        Sparepart(nama = "Konektor Cas Xiaomi Note 10", kategori = "Konektor", hargaModal = 25000.0, hargaJual = 75000.0, stok = 2)
    ))
    val spareparts: StateFlow<List<Sparepart>> = _spareparts.asStateFlow()

    fun addSparepart(nama: String, kategori: String, hargaModal: Double, hargaJual: Double, stok: Int) {
        val newList = _spareparts.value.toMutableList()
        newList.add(Sparepart(nama = nama, kategori = kategori, hargaModal = hargaModal, hargaJual = hargaJual, stok = stok))
        _spareparts.value = newList
    }

    fun updateSparepart(id: String, nama: String, kategori: String, hargaModal: Double, hargaJual: Double, stok: Int) {
        val newList = _spareparts.value.toMutableList()
        val index = newList.indexOfFirst { it.id == id }
        if (index != -1) {
            newList[index] = Sparepart(id = id, nama = nama, kategori = kategori, hargaModal = hargaModal, hargaJual = hargaJual, stok = stok)
            _spareparts.value = newList
        }
    }

    fun deleteSparepart(id: String) {
        val newList = _spareparts.value.toMutableList()
        newList.removeAll { it.id == id }
        _spareparts.value = newList
    }

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
        noService: String,
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
                noService = noService,
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

    fun updateWarranty(id: String, tanggalMulai: Long, jenis: String, durasiHari: Int, riwayatKlaim: String) {
        viewModelScope.launch {
            repository.updateWarranty(id, tanggalMulai, jenis, durasiHari, riwayatKlaim)
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
