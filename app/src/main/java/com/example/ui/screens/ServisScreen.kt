package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.NeoButton
import com.example.ui.components.NeoCard
import com.example.ui.components.NeoTextField
import com.example.ui.theme.*

@Composable
fun ServisScreen(viewModel: MainViewModel) {
    val serviceJobs by viewModel.serviceJobs.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }, 
                containerColor = AccentBlue,
                contentColor = White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Servis")
            }
        },
        containerColor = BlackPrimary
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Manajemen Servis", 
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = White
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
                items(serviceJobs) { job ->
                    ServiceJobCard(job)
                }
            }
        }
    }

    if (showDialog) {
        var brand by remember { mutableStateOf("") }
        var model by remember { mutableStateOf("") }
        var imei by remember { mutableStateOf("") }
        var keluhan by remember { mutableStateOf("") }
        var kelengkapan by remember { mutableStateOf("") }
        var passwordHp by remember { mutableStateOf("") }
        var estimasi by remember { mutableStateOf("") }
        var dp by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showDialog = false }) {
            NeoCard(
                backgroundColor = White,
                shadowColor = AccentBlue,
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
                            "Terima Servis Baru", 
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = BlackPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    
                    item { NeoTextField(value = brand, onValueChange = { brand = it }, label = "Brand (e.g. Samsung)") }
                    item { NeoTextField(value = model, onValueChange = { model = it }, label = "Model") }
                    item { NeoTextField(value = imei, onValueChange = { imei = it }, label = "IMEI/SN") }
                    item { NeoTextField(value = keluhan, onValueChange = { keluhan = it }, label = "Keluhan Utama") }
                    item { NeoTextField(value = kelengkapan, onValueChange = { kelengkapan = it }, label = "Kelengkapan (Unit, Dus, dll)") }
                    item { NeoTextField(value = passwordHp, onValueChange = { passwordHp = it }, label = "Password/PIN HP") }
                    item { 
                        NeoTextField(
                            value = estimasi, 
                            onValueChange = { estimasi = it }, 
                            label = "Estimasi Biaya", 
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        ) 
                    }
                    item { 
                        NeoTextField(
                            value = dp, 
                            onValueChange = { dp = it }, 
                            label = "DP (Opsional)", 
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        ) 
                    }
                    
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
                            NeoButton(
                                onClick = {
                                    viewModel.addServiceJob(
                                        customerId = "default_customer", 
                                        brand = brand, 
                                        model = model, 
                                        imei = imei, 
                                        keluhan = keluhan, 
                                        kelengkapan = kelengkapan,
                                        passwordHp = passwordHp,
                                        estimasi = estimasi.toDoubleOrNull() ?: 0.0,
                                        dp = dp.toDoubleOrNull() ?: 0.0
                                    )
                                    showDialog = false
                                },
                                backgroundColor = AccentBlue
                            ) {
                                Text("Simpan", color = White, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }
        }
    }
}
