package com.example.milkymate.Nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart

sealed class NavItem {
    object Home :
        Item(path = NavPath.HOME.toString(), title = NavTitle.HOME, icon = Icons.Default.Home)

    object Products :
        Item(path = NavPath.PRODUCTS.toString(), title = NavTitle.PRODUCTS, icon = Icons.Default.Menu)

    object History:
        Item(path = NavPath.HISTORY.toString(), title = NavTitle.HISTORY, icon = Icons.Default.ShoppingCart)

    object Profile :
        Item(path = NavPath.PROFILE.toString(), title = NavTitle.PROFILE , icon = Icons.Default.Person)
}