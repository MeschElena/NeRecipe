package ru.netology.nerecipe

import android.net.Uri

data class Step (
    val idStep: Long,
    val idRecipe: Long,
    val content: String,
    val image: Uri = Uri.parse("android.resource://ru.netology.nerecipe./drawable/pngegg.png")
    )