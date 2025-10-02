package edu.ucne.esmailin_martinez_ap2_p1.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun DrawerItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    screen: Screen,
    navigateTo: (Screen) -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        label = { Text(text = title) },
        selected = isSelected,
        onClick = { navigateTo(screen) }
    )
}
