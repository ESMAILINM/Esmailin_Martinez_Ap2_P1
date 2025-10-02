// Screen.kt
package edu.ucne.esmailin_martinez_ap2_p1.navigation

sealed class Screen(val route: String) {

    object ListEntradaHuacales : Screen("list_entrada_huacales_screen")


    object EditEntradaHuacales : Screen("edit_entrada_huacales_screen/{entradaId}") {
        fun createRoute(entradaId: Int) = "edit_entrada_huacales_screen/$entradaId"
    }
}
