package ru.netology.nerecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.map

class RecipeRepositoryImpl(
    private val dao: RecipeDao
) : RecipeRepository {


    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }
    override fun getAll(): LiveData<List<Recipe>> = data

    override fun save(recipe: Recipe): Long {
        if (recipe.id == 0L) {
            return dao.insert(recipe.toEntity())
        } else {
            dao.updateRecipeById(
                recipe.id, recipe.name, recipe.author,
                recipe.categoryRecipe, recipe.content, recipe.image.toString()
            )
            return recipe.id
        }
    }

    override fun favourite(id: Long) {
        dao.favourite(id)
    }

    override fun delete(id: Long) {
        dao.removeById(id)
    }

}