package com.example.travelupa

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.travelupa.ui.theme.TravelupaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TravelupaTheme {
                RekomendasiTempatScreen()
            }
        }
    }
}

@Composable
fun GreetingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Selamat Datang di Travelupa!",
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Solusi buat kamu yang lupa kemana-mana",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal),
            )
        }
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .width(360.dp)
                .padding(bottom = 16.dp)
        ) {
            Text(text = "Mulai")
        }
    }
}

data class TempatWisata(
    val nama: String,
    val deskripsi: String,
    val gambarUriString: String? = null,
    val gambarResId: Int? = null
)

@Composable
fun RekomendasiTempatScreen() {
    var daftarTempatWisata by remember {
        mutableStateOf(
            listOf(
                TempatWisata(
                    "Tumpak Sewu",
                    "Air terjun tercantik di Jawa Timur.",
                    gambarResId = R.drawable.tumpak_sewu
                ),
                TempatWisata(
                    "Gunung Bromo",
                    "Matahari terbitnya bagus banget.",
                    gambarResId = R.drawable.gunung_bromo
                )
            )
        )
    }

    var showTambahDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showTambahDialog = true },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Tambah Tempat Wisata")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(daftarTempatWisata) { tempat ->
                    TempatItemEditable(
                        tempat = tempat,
                        onDelete = {
                            daftarTempatWisata =
                                daftarTempatWisata.filter { it != tempat }
                        }
                    )
                }
            }
        }

        if (showTambahDialog) {
            TambahTempatWisataDialog(
                onDismiss = { showTambahDialog = false },
                onTambah = { nama, deskripsi, gambarUri ->
                    val tempatBaru = TempatWisata(
                        nama = nama,
                        deskripsi = deskripsi,
                        gambarUriString = gambarUri
                    )
                    daftarTempatWisata = daftarTempatWisata + tempatBaru
                    showTambahDialog = false
                }
            )
        }
    }
}

@Composable
fun TempatItemEditable(
    tempat: TempatWisata,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colors.surface),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Gambar wisata
            Image(
                painter = tempat.gambarUriString?.let { uriString ->
                    rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(Uri.parse(uriString))
                            .build()
                    )
                } ?: tempat.gambarResId?.let {
                    painterResource(id = it)
                } ?: painterResource(id = R.drawable.default_image),
                contentDescription = tempat.nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tempat.nama,
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = tempat.deskripsi,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = "Hapus Tempat Wisata",
                        tint = MaterialTheme.colors.error
                    )
                }
            }
        }
    }
}

@Composable
fun TambahTempatWisataDialog(
    onDismiss: () -> Unit,
    onTambah: (String, String, String?) -> Unit
) {
    var nama by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var gambarUri by remember { mutableStateOf<Uri?>(null) }

    val gambarLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        gambarUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Tempat Wisata Baru") },
        text = {
            Column {
                TextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama Tempat") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    label = { Text("Deskripsi") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                GambarPicker(
                    gambarUri = gambarUri,
                    onPilihGambar = { gambarLauncher.launch("image/*") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nama.isNotBlank() && deskripsi.isNotBlank()) {
                        onTambah(nama, deskripsi, gambarUri?.toString())
                    }
                }
            ) {
                Text("Tambah")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.surface
                )
            ) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun GambarPicker(
    gambarUri: Uri?,
    onPilihGambar: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Jika pengguna sudah memilih gambar → tampilkan hasil pilihan
        if (gambarUri != null) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(gambarUri)
                        .build()
                ),
                contentDescription = "Gambar Tempat Wisata",
                modifier = Modifier
                    .size(200.dp)
                    .clickable { onPilihGambar() },
                contentScale = ContentScale.Crop
            )
        } else {
            // Jika belum memilih gambar -> tampilkan gambar default
            Image(
                painter = painterResource(id = R.drawable.default_image),
                contentDescription = "Gambar Default",
                modifier = Modifier
                    .size(200.dp)
                    .clickable { onPilihGambar() },
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onPilihGambar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Pilih Gambar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pilih Gambar")
            }
        }
    }
}