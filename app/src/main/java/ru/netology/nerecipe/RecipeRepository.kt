package ru.netology.nerecipe

import androidx.lifecycle.LiveData

interface RecipeRepository {
    val data: LiveData<List<Recipe>>
    fun getAll(): LiveData<List<Recipe>>
    fun favourite(id: Long)
    fun delete(id: Long)
    fun save(recipe: Recipe): Long

    companion object {
        const val NEW_RECIPE_ID = 0L
    }
}