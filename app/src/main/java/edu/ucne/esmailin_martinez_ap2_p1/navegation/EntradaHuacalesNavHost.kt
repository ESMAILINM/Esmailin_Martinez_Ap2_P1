// NavHost.kt
package edu.ucne.esmailin_martinez_ap2_p1.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import edu.ucne.esmailin_martinez_ap2_p1.presentation.edit.EditEntradaHuacalesScreen
import edu.ucne.esmailin_martinez_ap2_p1.presentation.list.ListEntradaHuacalesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntradaHuacalesNavHost(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedItem = remember { mutableStateOf(Screen.ListEntradaHuacales.route) }

    fun handleItemClick(screen: Screen) {
        navController.navigate(screen.route) {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
        selectedItem.value = screen.route
        scope.launch { drawerState.close() }
    }

    val currentScreenIconAndTitle = when (selectedItem.value) {
        Screen.ListEntradaHuacales.route -> Icons.Filled.List to "Entradas de Huacales"
        Screen.EditEntradaHuacales.route -> Icons.Filled.Add to "Editar Entrada"
        else -> Icons.Filled.List to "Entradas"
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Gestión de Huacales",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp)
                )
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    item {
                        DrawerItem(
                            title = "Entradas",
                            icon = Icons.Filled.List,
                            isSelected = selectedItem.value == Screen.ListEntradaHuacales.route,
                            screen = Screen.ListEntradaHuacales
                        ) { handleItemClick(it) }
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = currentScreenIconAndTitle.first,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(currentScreenIconAndTitle.second)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.ListEntradaHuacales.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.ListEntradaHuacales.route) {
                    selectedItem.value = Screen.ListEntradaHuacales.route
                    ListEntradaHuacalesScreen(navController = navController)
                }

                composable(
                    route = Screen.EditEntradaHuacales.route,
                    arguments = listOf(navArgument("entradaId") { type = androidx.navigation.NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("entradaId")
                    selectedItem.value = Screen.EditEntradaHuacales.route
                    EditEntradaHuacalesScreen(navController = navController, entradaId = id)
                }
            }
        }
    }
}
