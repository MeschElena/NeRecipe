package ru.netology.nerecipe

import androidx.lifecycle.LiveData

interface RecipeRepository {
//    val dataRecipe: LiveData<List<Recipe>>
//    val dataStep: LiveData<List<Step>>
//    fun getAllRecipe(): LiveData<List<Recipe>>
//    fun getAllStep(): LiveData<List<Step>>
//    fun favorite(id: Long)
//    fun deleteRecipe(id: Long)
//    fun deleteStep(id: Long)
//    fun saveRecipe(recipe: Recipe)
//    fun saveStep(step: Step)

    val data: LiveData<List<Recipe>>
  //  val dataStep: LiveData<List<Step>>
    fun getAll(): LiveData<List<Recipe>>
    fun favourite(id: Long)
    fun delete(id: Long)
    fun save(recipe: Recipe)

    companion object {
        const val NEW_RECIPE_ID = 0L
  //      const val NEW_STEP_ID = 0L
    }
}