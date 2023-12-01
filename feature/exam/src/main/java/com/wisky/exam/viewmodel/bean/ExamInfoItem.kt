package com.wisky.exam.viewmodel.bean

/**
 * 题库详情
 */

data class ExamInfoItem(
    val Analyse: String,
    val Answers: List<String>,
    val Cate: Int,
    val CateName: String,
    val Content: String,
    val Date: String,
    val Degree: Double,
    val Discuss: String,
    val ID: String,
    val Label: String,
    val LabelReportID: Any,
    val Method: String,
    val More: Any,
    val Options: List<String>,
    val ParentContent: String,
    val ParentID: Any,
    val ParentSID: Any,
    val Points: List<Point>,
    val QuesChilds: List<Any>,
    val QuesFiles: List<Any>,
    val RC: Double,
    val RealCount: Int,
    val SID: String,
    val Score: Double,
    val Seq: Int,
    val Subject: Int,
)

/**
 * 知识点
 */
data class Point(
    val Key: String,
    val Value: String,
)
