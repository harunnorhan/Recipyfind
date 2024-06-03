package com.example.therecipeapp.utils

fun String.capitalizeWords(): String = split(" ").joinToString(" ") { it.capitalize() }.trim()