package edu.ucne.esmailin_martinez_ap2_p1.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.esmailin_martinez_ap2_p1.domain.model.EntradaHuacales
import edu.ucne.esmailin_martinez_ap2_p1.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEntradaHuacalesScreen(
    navController: NavController,
    viewModel: ListEntradaHuacalesVIewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.EntradaHuacales.createRoute(0)) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir entrada")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.entradas.isEmpty() -> {
                    Text(
                        "No hay entradas. ¡Añade una!",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.entradas, key = { it.identradaHuacales }) { entrada ->
                            EntradaHuacalesItem(
                                entrada = entrada,
                                onEntradaClick = {
                                    navController.navigate(
                                        Screen.EntradaHuacales.createRoute(entrada.identradaHuacales)
                                    )
                                },
                                onDeleteClick = {
                                    viewModel.onEvent(
                                        ListEntradaHuacalesUiEvent.OnDeleteEntradaClick(entrada)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EntradaHuacalesItem(
    entrada: EntradaHuacales,
    onEntradaClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onEntradaClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(entrada.nombreCliente, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Cantidad: ${entrada.cantidad}", style = MaterialTheme.typography.bodyMedium)
                Text("Precio: ${entrada.precio}", style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar entrada",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
