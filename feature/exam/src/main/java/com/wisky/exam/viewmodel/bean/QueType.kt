package com.wisky.exam.viewmodel.bean

enum class QueType(val cateType: Int, val cateName: String) {
    SINGLE(1, "单选题"),
    BLANKS(2, "填空题"),
    MULTIPLE(3, "多选题"),
    JUDGE(4, "判断题"),
    DRAWING(5, "作图题"),
    EXPERIMENTAL(6, "实验探究题"),
    CALCULATION(7, "计算题"),
    COMPREHENSIVE(8, "综合能力题"),
    ANSWER_EXPLANATION(9, "简答题"),
    CHOICE_EXPLANATION(10, "选择说明"),
    SCIENCE_READING(11, "科普阅读题"),
    SHORT_ANSWER(12, "简答题"),
}