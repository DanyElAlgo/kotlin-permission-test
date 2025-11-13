package com.example.permission_test

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.permission_test.ui.theme.PermissiontestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PermissiontestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PermissionTestScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PermissionTestScreen(modifier: Modifier = Modifier) {
    // Contexto actual de la aplicación
    val context = LocalContext.current

    // Estados para saber si los permisos han sido concedidos
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasMicrophonePermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Lanzador para solicitar el permiso de la cámara. [2, 6]
    // El resultado (isGranted) se recibe en este callback.
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
            if (isGranted) {
                Toast.makeText(context, "Permiso de cámara CONCEDIDO", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permiso de cámara DENEGADO", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // Lanzador para solicitar el permiso del micrófono. [8]
    val microphonePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasMicrophonePermission = isGranted
            if (isGranted) {
                Toast.makeText(context, "Permiso de micrófono CONCEDIDO", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permiso de micrófono DENEGADO", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = if (hasCameraPermission) "Permiso de cámara: SÍ" else "Permiso de cámara: NO")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                // Si el permiso ya está concedido, no hacemos nada.
                // Si no, lanzamos la solicitud. [10]
                if (!hasCameraPermission) {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                } else {
                    Toast.makeText(context, "El permiso de cámara ya fue concedido", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Acceder a la cámara")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = if (hasMicrophonePermission) "Permiso de micrófono: SÍ" else "Permiso de micrófono: NO")
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (!hasMicrophonePermission) {
                    microphonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                } else {
                    Toast.makeText(context, "El permiso de micrófono ya fue concedido", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Acceder al micrófono")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PermissionTestScreenPreview() {
    PermissiontestTheme {
        PermissionTestScreen()
    }
}
