package edu.ucne.esmailin_martinez_ap2_p1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.esmailin_martinez_ap2_p1.navigation.EntradaHuacalesNavHost
import edu.ucne.esmailin_martinez_ap2_p1.ui.theme.Esmailin_Martinez_Ap2_P1Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Esmailin_Martinez_Ap2_P1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    EntradaHuacalesNavHost(navController = navController)
                }
            }
        }
    }
}
