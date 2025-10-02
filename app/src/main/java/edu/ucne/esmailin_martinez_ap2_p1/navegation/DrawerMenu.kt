package edu.ucne.esmailin_martinez_ap2_p1.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun DrawerMenu(
    drawerState: DrawerState,
    navHostController: NavHostController,
    content: @Composable () -> Unit
) {
    val selectedItem = remember { mutableStateOf(Screen.ListEntradaHuacales.route) }
    val scope = rememberCoroutineScope()

    fun handleItemClick(screen: Screen) {
        navHostController.navigate(screen.route) {
            popUpTo(navHostController.graph.findStartDestination().id) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
        selectedItem.value = screen.route
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Menú Huacales",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider()
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
                    item {
                        DrawerItem(
                            title = "Nueva Entrada",
                            icon = Icons.Filled.Add,
                            isSelected = false,
                            screen = Screen.EditEntradaHuacales
                        ) { handleItemClick(Screen.EditEntradaHuacales) }
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        content()
    }
}
