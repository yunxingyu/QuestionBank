/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wisky.questionbank.navigation

import com.wisky.questionbank.R

/**
 * Type for the top level destinations in the application. Each of these destinations
 * can contain one or more screens (based on the window size). Navigation from one screen to the
 * next within a single destination will be handled directly in composables.
 */
enum class TopLevelDestination(
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val iconTextId: Int,
    val titleTextId: Int,
) {
    TEXTBOOK(
        selectedIcon = R.drawable.app_jiaocai_select,
        unselectedIcon = R.drawable.app_jiaocai_unselect,
        iconTextId = R.string.app_text_book,
        titleTextId = R.string.app_text_book,
    ),
    TESTPAPER(
        selectedIcon = R.drawable.app_kaoqian_select,
        unselectedIcon = R.drawable.app_kaoqian_unselect,
        iconTextId = R.string.app_test_paper,
        titleTextId = R.string.app_test_paper,
    ),
    WRONGQUESTION(
        selectedIcon = R.drawable.app_shijuan_slelect,
        unselectedIcon = R.drawable.app_shijuan_unselect,
        iconTextId = R.string.app_wrong_question,
        titleTextId = R.string.app_wrong_question,
    ),
    PreExam(
        selectedIcon = R.drawable.app_error_select,
        unselectedIcon = R.drawable.app_error_unselect,
        iconTextId = R.string.app_pre_exam,
        titleTextId = R.string.app_pre_exam,
    ),
}
