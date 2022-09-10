package ru.netology.nerecipe

interface RecipeIteractionListener {
    fun onFavouriteClicked(recipe: Recipe)
    fun onRemoveClicked(recipe: Recipe)
    fun onEditClicked(recipe: Recipe)
   // fun onRecipeClicked(recipe: Recipe)
}