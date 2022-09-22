package ru.netology.nerecipe

import android.net.Uri
import androidx.lifecycle.LiveData

data class Recipe (
    val id: Long,
    val name: String,
    val author: String,
    val categoryRecipe: String,
    val content: String,
    val image: Uri = Uri.parse("android.resource://ru.netology.nerecipe./drawable/pngwing.png"),
    val favourite: Boolean = false
)

