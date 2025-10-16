package edu.ucne.esmailin_martinez_ap2_p1.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    val currentTitle = remember { mutableStateOf("Entradas de Huacales") }

    fun handleItemClick(screen: Screen) {
        scope.launch {
            drawerState.close()
            navController.navigate(
                when (screen) {
                    is Screen.ListEntradaHuacales -> screen.route
                    is Screen.EditEntradaHuacales -> Screen.EditEntradaHuacales.createRoute(0)
                }
            ) {
                popUpTo(Screen.ListEntradaHuacales.route) {
                    inclusive = true
                }
                launchSingleTop = true
                restoreState = true
            }
            selectedItem.value = screen.route
        }
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, top = 4.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(
                                text = currentTitle.value,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
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
                    currentTitle.value = "Entradas de Huacales"
                    selectedItem.value = Screen.ListEntradaHuacales.route
                    ListEntradaHuacalesScreen(navController)
                }
                composable(
                    route = "edit_entrada_huacales/{entradaId}",
                    arguments = listOf(navArgument("entradaId") { type = androidx.navigation.NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("entradaId") ?: 0
                    currentTitle.value = if (id == 0) "Nueva Entrada" else "Editar Entrada"
                    selectedItem.value = Screen.EditEntradaHuacales.route
                    EditEntradaHuacalesScreen(navController, entradaId = id)
                }
            }
        }
    }
}
