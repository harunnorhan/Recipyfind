package com.example.therecipeapp.utils

import android.text.Html
import androidx.compose.ui.text.AnnotatedString

fun parseHtmlToAnnotatedString(htmlText: String): AnnotatedString {
    val spanned = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
    return AnnotatedString.Builder().apply {
        append(spanned.toString())
    }.toAnnotatedString()
}