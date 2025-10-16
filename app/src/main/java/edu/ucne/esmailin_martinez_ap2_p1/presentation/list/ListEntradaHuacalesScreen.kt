package edu.ucne.esmailin_martinez_ap2_p1.presentation.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
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
import edu.ucne.esmailin_martinez_ap2_p1.domain.model.EntradaHuacales
import edu.ucne.esmailin_martinez_ap2_p1.navigation.Screen
import java.text.NumberFormat
import java.util.*

fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("es", "DO")).format(value)
}

@Composable
fun EntradaHuacalesItem(
    entrada: EntradaHuacales,
    onEntradaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fechaStr = entrada.fecha

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEntradaClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = fechaStr,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(entrada.nombreCliente, fontWeight = FontWeight.Bold)
                    Text("${entrada.cantidad} x ${formatCurrency(entrada.precio)}", fontSize = 14.sp)
                }
                Text(
                    "= ${formatCurrency(entrada.cantidad * entrada.precio)}",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEntradaHuacalesScreen(
    navController: NavController,
    viewModel: ListEntradaHuacalesVIewModel = hiltViewModel()
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsStateWithLifecycle()
    var filtro by remember { mutableStateOf("") }
    var filtroFecha by remember { mutableStateOf("") }
    var showFilterField by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val filteredEntradas = state.entradas.filter { entrada ->
        val term = filtro.lowercase(Locale.getDefault())
        val termFecha = filtroFecha.lowercase(Locale.getDefault())

        (term.isBlank() ||
                entrada.nombreCliente.lowercase(Locale.getDefault()).contains(term) ||
                entrada.descripcion.lowercase(Locale.getDefault()).contains(term) ||
                entrada.cantidad.toString().contains(term) ||
                entrada.precio.toString().contains(term) ||
                formatCurrency(entrada.cantidad * entrada.precio).contains(term)
                ) && (termFecha.isBlank() || entrada.fecha.contains(termFecha))
    }

    val total = filteredEntradas.sumOf { it.cantidad * it.precio }
    val totalRegistros = if (filtro.isBlank() && filtroFecha.isBlank()) state.entradas.size else filteredEntradas.size

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.EditEntradaHuacales.createRoute(0)) },
                containerColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir entrada")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Card de filtros
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Filtros",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = {
                            showFilterField = !showFilterField
                            if (!showFilterField) {
                                filtro = ""
                                filtroFecha = ""
                            }
                        }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Filtro")
                        }
                    }

                    AnimatedVisibility(visible = showFilterField) {
                        Column {
                            OutlinedTextField(
                                value = filtro,
                                onValueChange = { filtro = it },
                                label = { Text("Buscar en todos los campos") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = filtroFecha,
                                onValueChange = { filtroFecha = it },
                                readOnly = true,
                                label = { Text("Filtrar por fecha (yyyy-MM-dd)") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .clickable { showDatePicker = true },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Filtrar por fecha"
                                        )
                                    }
                                }
                            )
                        }
                    }
                    if (showDatePicker) {
                        DatePickerDialog(
                            onDismissRequest = { showDatePicker = false },
                            confirmButton = {
                                TextButton(onClick = {
                                    val millis = datePickerState.selectedDateMillis
                                    if (millis != null) {
                                        val localDate = java.time.Instant.ofEpochMilli(millis)
                                            .atZone(java.time.ZoneId.systemDefault())
                                            .toLocalDate()
                                        val formatted = localDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                                        filtroFecha = formatted
                                    }
                                    showDatePicker = false
                                }) { Text("OK") }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
                            }
                        ) {
                            DatePicker(state = datePickerState)
                        }
                    }
                }
            }
            if (state.isLoading) {
                Box(Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (filteredEntradas.isEmpty()) {
                Box(Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No hay entradas que coincidan.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredEntradas, key = { it.identradaHuacales }) { entrada ->
                        EntradaHuacalesItem(
                            entrada = entrada,
                            onEntradaClick = {
                                navController.navigate(
                                    Screen.EditEntradaHuacales.createRoute(entrada.identradaHuacales)
                                )
                            }
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("$totalRegistros")
                        Text(" ${formatCurrency(total)}", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
