package edu.ucne.esmailin_martinez_ap2_p1.presentation.edit

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntradaHuacalesScreen(
    navController: NavController,
    entradaId: Int?,
    viewModel: EditEntradaHuacalesViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = entradaId) {
        viewModel.onEvent(EditEntradaHuacalesUiEvent.Load(entradaId))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.isSaved) {
        if (state.isSaved) navController.popBackStack()
    }

    EditEntradaHuacalesBody(
        state = state,
        onEvent = viewModel::onEvent,
        onCancel = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntradaHuacalesBody(
    state: EditEntradaHuacalesUiState,
    onEvent: (EditEntradaHuacalesUiEvent) -> Unit,
    onCancel: () -> Unit
) {
    val cantidadDouble = state.cantidad.toDoubleOrNull() ?: 0.0
    val precioDouble = state.precio.toDoubleOrNull() ?: 0.0
    val importeVal = cantidadDouble * precioDouble
    val importeStr = NumberFormat.getCurrencyInstance(Locale("es", "DO")).format(importeVal)
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("")

            OutlinedTextField(
                value = state.fecha,
                onValueChange = {},
                label = { Text("Fecha (yyyy-MM-dd)") },
                readOnly = true,
                isError = state.fechaError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )
            state.fechaError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val millis = datePickerState.selectedDateMillis
                                if (millis != null) {
                                    val formatted = SimpleDateFormat("yyyy-MM-dd", Locale("es", "DO"))
                                        .format(Date(millis))
                                    onEvent(EditEntradaHuacalesUiEvent.FechaChanged(formatted))
                                }
                                showDatePicker = false
                            }
                        ) { Text("Aceptar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { onEvent(EditEntradaHuacalesUiEvent.DescripcionChanged(it)) },
                label = { Text("Descripción") },
                isError = state.descripcionError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 4
            )
            state.descripcionError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )
            }

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
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )
            }

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
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )
            }

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
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )
            }

            OutlinedTextField(
                value = importeStr,
                onValueChange = {},
                label = { Text("Importe") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // Botones: Guardar, Eliminar y Cancelar
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { onEvent(EditEntradaHuacalesUiEvent.Save) },
                        enabled = !state.isSaving,
                        modifier = Modifier.weight(1f).height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) { Text("Guardar") }

                    if (state.canBeDeleted) {
                        OutlinedButton(
                            onClick = { onEvent(EditEntradaHuacalesUiEvent.Delete) },
                            modifier = Modifier.weight(1f).height(52.dp),
                        ) { Text("Eliminar") }
                    }
                }

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) { Text("Cancelar") }
            }

            state.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 12.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                )
            }
        }
    }
}
