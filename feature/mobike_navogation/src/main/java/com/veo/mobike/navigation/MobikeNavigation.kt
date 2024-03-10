package com.veo.mobike.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.veo.mobike.ui.MapDetailPaperRoute


const val mapDetailRoute = "map_route/map_detail"


fun NavController.navigateToMapDetail(navOptions: NavOptions? = null) {
    this.navigate(mapDetailRoute, navOptions)
}



fun NavGraphBuilder.forMapDetailScreen() {
    composable(
        route = mapDetailRoute,
    ) {
        MapDetailPaperRoute()
    }
}
