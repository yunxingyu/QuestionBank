package com.wisky.questionbank.navigation

import com.wisky.tiku.navigation.testPaperRoute
import com.wisky.tiku.navigation.textbookRoute
import com.wisky.tiku.navigation.wrongQuestionRoute
import com.wisky.usercenter.navigation.userNavigationRoute

sealed class QuestionBankScreens(val name: String) {

    data object Textbook : QuestionBankScreens(textbookRoute)
    data object TestPaper : QuestionBankScreens(testPaperRoute)
    data object WrongQuestion : QuestionBankScreens(wrongQuestionRoute)
    data object PreExam : QuestionBankScreens(userNavigationRoute)
}
