package ru.netology.nerecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map

class StepRecipeRepositoryImpl(private val dao: RecipeDao
) : StepRecipeRepository {

        override val dataStep = dao.getAllStep().map { entities ->
            entities.map { it.toModel() }
        }
        override fun getAllStep(): LiveData<List<Step>> = dataStep

        override fun saveStep(step: Step) {
            if (step.idStep == 0L) dao.insertStep(step.toEntity()) else dao.updateStepById(step.idStep, step.idRecipe, step.content, step.image.toString())
        }

        override fun deleteStep(id: Long, idRecipe: Long) {
            dao.removeStepById(id, idRecipe)
        }

    }