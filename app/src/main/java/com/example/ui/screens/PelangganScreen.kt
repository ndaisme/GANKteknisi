package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.NeoCard
import com.example.ui.components.NeoTextField
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PelangganScreen(viewModel: MainViewModel) {
    val customers by viewModel.customers.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }, 
                containerColor = AccentSilver,
                contentColor = BlackPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Pelanggan")
            }
        },
        containerColor = BlackPrimary
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Data Pelanggan", 
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = White
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
                items(customers) { customer ->
                    NeoCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = White,
                        shadowColor = Silver,
                        borderColor = BlackPrimary,
                        cornerRadius = 16.dp,
                        strokeWidth = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(customer.nama, fontWeight = FontWeight.Black, color = BlackPrimary, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(customer.telepon, color = BlackSurfaceVariant, fontWeight = FontWeight.Medium)
                            Text(customer.alamat, color = GrayText, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        var nama by remember { mutableStateOf("") }
        var telepon by remember { mutableStateOf("") }
        var alamat by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showDialog = false }) {
            NeoCard(
                backgroundColor = White,
                shadowColor = AccentSilver,
                borderColor = BlackPrimary,
                cornerRadius = 24.dp,
                strokeWidth = 4.dp
            ) {
                LazyColumn(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            "Tambah Pelanggan Baru", 
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = BlackPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    item { NeoTextField(value = nama, onValueChange = { nama = it }, label = "Nama Pelanggan") }
                    item { 
                        NeoTextField(
                            value = telepon, 
                            onValueChange = { telepon = it }, 
                            label = "Nomor Telepon",
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone)
                        ) 
                    }
                    item { NeoTextField(value = alamat, onValueChange = { alamat = it }, label = "Alamat") }
                    
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Batal", color = GrayText, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            com.example.ui.components.NeoButton(
                                onClick = {
                                    viewModel.addCustomer(nama, telepon, alamat, "")
                                    showDialog = false
                                },
                                backgroundColor = AccentSilver
                            ) {
                                Text("Simpan", color = BlackPrimary, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}
