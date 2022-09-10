package ru.netology.nerecipe

import androidx.lifecycle.LiveData

interface StepRecipeRepository {
    val dataStep: LiveData<List<Step>>
    fun getAllStep(): LiveData<List<Step>>
    fun deleteStep(id: Long, idRecipe: Long)
    fun saveStep(step: Step)

    companion object {
        const val NEW_STEP_ID = 0L
    }}