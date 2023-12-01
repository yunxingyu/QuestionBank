package com.wisky.exam.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

const val EXAM_RESOURCE_ID = "examId"
const val examNavigationRoute = "exam_route/exam_detail/{$EXAM_RESOURCE_ID}"

fun NavController.navigateToExam(navOptions: NavOptions? = null) {
    this.navigate(examNavigationRoute, navOptions)
}
