package com.wisky.questionbank.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.wisky.common.data.util.NetworkMonitor
import com.wisky.questionbank.R
import com.wisky.tiku.navigation.forTestPaperScreen
import com.wisky.tiku.navigation.forTextBookScreen
import com.wisky.tiku.navigation.forWrongQuestScreen
import com.wisky.tiku.navigation.textbookRoute
import com.wisky.usercenter.navigation.forMineScreen


@Composable
fun QuestionBankApp(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    appState: QueAppState = rememberQueAppState(
        networkMonitor = networkMonitor,
        windowSizeClass = windowSizeClass,
    ),
) {
    val bottomNavItem = getBottomNavItems()
    val screensWithShowNavBar = listOf(
        QuestionBankScreens.Textbook.name,
        QuestionBankScreens.TestPaper.name,
        QuestionBankScreens.WrongQuestion.name,
        QuestionBankScreens.PreExam.name,
    )
    val backStackEntry = appState.navController.currentBackStackEntryAsState()

    var shouldHideBottomBar: Boolean by remember {
        mutableStateOf(true)
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = shouldHideBottomBar,
                enter = fadeIn(animationSpec = tween(delayMillis = 500, easing = LinearOutSlowInEasing)),
                exit = fadeOut(animationSpec = tween(delayMillis = 500, easing = LinearOutSlowInEasing)),
            ) {
                BottomNavigationBar(
                    backStackEntry,
                    bottomNavItem,
                    screensWithShowNavBar,
                    appState.navController,
                )
            }
        },
    ) { _ ->
        NavHost(
            navController = appState.navController,
            modifier = Modifier,
            startDestination = textbookRoute,
        ) {
            forTextBookScreen()
            forTestPaperScreen()
            forWrongQuestScreen()
            forMineScreen()
        }
    }
}

@Composable
fun BottomNavigationBar(
    backStackEntry: State<NavBackStackEntry?>,
    bottomNavItem: List<BottomNavItem>,
    screensWithShowNavBar: List<String>,
    navController: NavHostController,
) {
    if (backStackEntry.value?.destination?.route in screensWithShowNavBar) {
        NavigationBar(containerColor = Color.Transparent, modifier = Modifier.height(125.dp)) {
            bottomNavItem.forEach { item ->
                NavigationBarItem(
                    alwaysShowLabel = true,
                    icon = {
                        Image(
                            painter = painterResource(
                                id = if (backStackEntry.value?.destination?.route == item.route) {
                                    item.icon
                                } else {
                                    item.unSelecticon
                                },
                            ),
                            contentDescription = item.name,
                            alpha = 1f,
                        )
                    },
                    label = {
                        Text(
                            text = item.name,
                            color = if (backStackEntry.value?.destination?.route == item.route) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.secondary
                            },
                            fontWeight = if (backStackEntry.value?.destination?.route == item.route) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            },
                        )
                    },
                    selected = backStackEntry.value?.destination?.route == item.route,
                    onClick = {
                        val currentDestination = navController.currentBackStackEntry?.destination?.route
                        if (item.route != currentDestination) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    },
                )
            }
        }
    }
}

fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(
            name = "教材同步",
            route = QuestionBankScreens.Textbook.name,
            icon = R.drawable.app_jiaocai_select,
            unSelecticon = R.drawable.app_jiaocai_unselect,
        ),
        BottomNavItem(
            name = "名校试卷",
            route = QuestionBankScreens.TestPaper.name,
            icon = R.drawable.app_shijuan_slelect,
            unSelecticon = R.drawable.app_shijuan_unselect,
        ),
        BottomNavItem(
            name = "错题本",
            route = QuestionBankScreens.WrongQuestion.name,
            icon = R.drawable.app_error_select,
            unSelecticon = R.drawable.app_error_unselect,
        ),
        BottomNavItem(
            name = "考前测评",
            route = QuestionBankScreens.PreExam.name,
            icon = R.drawable.app_kaoqian_select,
            unSelecticon = R.drawable.app_kaoqian_unselect,
        ),
    )
}
