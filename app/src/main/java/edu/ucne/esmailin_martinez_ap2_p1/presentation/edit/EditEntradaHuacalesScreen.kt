package edu.ucne.esmailin_martinez_ap2_p1.presentation.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun EditEntradaHuacalesScreen(
    navController: NavController,
    entradaId: Int?,
    viewModel: EditEntradaHuacalesViewModel = hiltViewModel()
) {
    LaunchedEffect(entradaId) {
        viewModel.onEvent(EditEntradaHuacalesUiEvent.Load(entradaId))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navController.popBackStack()
        }
    }

    EditEntradaHuacalesBody(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun EditEntradaHuacalesBody(
    state: EditEntradaHuacalesUiState,
    onEvent: (EditEntradaHuacalesUiEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nombre Cliente
            OutlinedTextField(
                value = state.nombreCliente,
                onValueChange = { onEvent(EditEntradaHuacalesUiEvent.NombreClienteChanged(it)) },
                label = { Text("Nombre del Cliente") },
                isError = state.nombreClienteError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.nombreClienteError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Descripción
            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { onEvent(EditEntradaHuacalesUiEvent.DescripcionChanged(it)) },
                label = { Text("Descripción") },
                isError = state.descripcionError != null,
                modifier = Modifier.fillMaxWidth()
            )
            state.descripcionError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Cantidad
            OutlinedTextField(
                value = state.cantidad,
                onValueChange = { onEvent(EditEntradaHuacalesUiEvent.CantidadChanged(it)) },
                label = { Text("Cantidad") },
                isError = state.cantidadError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            state.cantidadError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Precio
            OutlinedTextField(
                value = state.precio,
                onValueChange = { onEvent(EditEntradaHuacalesUiEvent.PrecioChanged(it)) },
                label = { Text("Precio") },
                isError = state.precioError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            state.precioError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onEvent(EditEntradaHuacalesUiEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Guardar")
                }

                if (state.canBeDeleted) {
                    OutlinedButton(
                        onClick = { onEvent(EditEntradaHuacalesUiEvent.Delete) },
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Eliminar")
                    }
                }
            }
        }
    }
}
