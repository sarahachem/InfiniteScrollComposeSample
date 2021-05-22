package com.example.neugelb.compose.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val body1 = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp
)

private val body2 = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp
)

private val data3 = TextStyle(
        fontSize = 36.sp,
        lineHeight = 48.sp
)

private val header1 = TextStyle(
        fontSize = 32.sp,
        lineHeight = 36.sp
)

private val header3 = TextStyle(
    fontSize = 18.sp,
    lineHeight = 22.sp
)

private val button1 = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp
)

private val detail1 = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp
)

fun typeList() = List(
    header1 = header1,
    header3 = header3,
    body1 = body1,
    body2 = body2,
    secondary = detail1,
    button1 = button1,
    data3 = data3,
)
