package com.wisky.tiku.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.wisky.tiku.ui.TestPaperRoute
import com.wisky.tiku.ui.TextBookRoute
import com.wisky.tiku.ui.WrongQuestionRoute


const val textbookRoute = "tiku_route/test_book"
const val testPaperRoute = "tiku_route/test_paper"
const val wrongQuestionRoute = "tiku_route/wrong_question"


fun NavController.navigateToTextBook(navOptions: NavOptions? = null) {
    this.navigate(textbookRoute, navOptions)
}

fun NavController.navigateToTestPaper(navOptions: NavOptions? = null) {
    this.navigate(testPaperRoute, navOptions)
}

fun NavController.navigateToWrongQuestion(navOptions: NavOptions? = null) {
    this.navigate(wrongQuestionRoute, navOptions)
}

fun NavGraphBuilder.forTextBookScreen() {
    composable(
        route = textbookRoute,
    ) {
        TextBookRoute()
    }
}


fun NavGraphBuilder.forTestPaperScreen() {
    composable(
        route = testPaperRoute,
    ) {
        TestPaperRoute()
    }
}

fun NavGraphBuilder.forWrongQuestScreen() {
    composable(
        route = wrongQuestionRoute,
    ) {
        WrongQuestionRoute()
    }
}