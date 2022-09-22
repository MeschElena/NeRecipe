package ru.netology.nerecipe

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Query

class RecipeViewModel(application: Application) : AndroidViewModel(application), RecipeIteractionListener, StepRecipeIteractionListener {

    private val repository: RecipeRepository = RecipeRepositoryImpl(
        dao = AppDb.getInstance(
            context = application
        ).recipeDao
    )
    private val repositoryStep: StepRecipeRepository = StepRecipeRepositoryImpl(
            dao = AppDb.getInstance(
                context = application
            ).recipeDao
    )
    val data get() = repository.data
    val dataStep get() = repositoryStep.dataStep
    val categoryRecipe = arrayOf("Европейская", "Азиатская", "Паназиатская", "Восточная", "Американская", "Русская", "Среднеземноморская")
    var imageRecipe : Uri = Uri.parse("android.resource://ru.netology.nerecipe./drawable/pngwing.png")
    var querySearch: String = ""
    var idEditeRecipe : Long = -1L
    val currentRecipe = MutableLiveData<Recipe?>(null)
    val currentStep = MutableLiveData<Step?>(null)
    val navigateToRecipeScreenEven = SingleLiveEvent<Long>()
    val navigateToStepScreenView = SingleLiveEvent<Long>()


    fun getFavotites() = checkNotNull(repository.getAll().value).filter {it.favourite == true}

    fun onSaveButtonClicked(recipe: Recipe) {
        if (recipe.content.isBlank() || recipe.author.isBlank()
            || recipe.name.isBlank() || recipe.categoryRecipe.isBlank()) return
        val recipeEdit = currentRecipe.value?.copy(
            name = recipe.name,
            author = recipe.author,
            categoryRecipe = recipe.categoryRecipe,
            content = recipe.content,
            image = recipe.image
        ) ?: Recipe(
            id = RecipeRepository.NEW_RECIPE_ID,
            name = recipe.name,
            author = recipe.author,
            categoryRecipe = recipe.categoryRecipe,
            content = recipe.content,
            image = recipe.image
        )
        idEditeRecipe = repository.save(recipeEdit)
        currentRecipe.value = recipeEdit.copy(id = idEditeRecipe)
    }

    override fun onFavouriteClicked(recipe: Recipe) = repository.favourite(recipe.id)

    override fun onRemoveClicked(recipe: Recipe) = repository.delete(recipe.id)

    override fun onEditClicked(recipe: Recipe) {
        navigateToRecipeScreenEven.value = recipe.id
        currentRecipe.value = recipe
    }

    fun onAddClicked(){
        navigateToRecipeScreenEven.value = -1L
    }

    fun getRecipe(recipeId:Long) = checkNotNull(repository.getAll().value).filter {it.id == recipeId}[0]

    fun getRecipesByQuery(query: String) = checkNotNull(repository.getAll().value).filter {it.name.contains(query) }

    fun getFavoritesByQuery(query: String) = checkNotNull(repository.getAll().value).filter { it.favourite }.filter {it.name.contains(query)}

    fun onAddClickedStep() {
        navigateToStepScreenView.value = -1L
    }

    override fun onStepRemoveClicked(step: Step) = repositoryStep.deleteStep(step.idStep, step.idRecipe)

    override fun onStepEditClicked(step: Step) {
        navigateToStepScreenView.value = step.idStep
        currentStep.value = step
    }

    fun getStepsByRecipeId(recipeId: Long) = checkNotNull(repositoryStep.getAllStep().value).filter {it.idRecipe == recipeId}

    fun onSaveStepButtonClicked(step: Step) {
        if (step.content.isBlank()) return
        val stepEdit = currentStep.value?.copy(
            content = step.content,
            image = step.image
        ) ?: Step(
            idStep = StepRecipeRepository.NEW_STEP_ID,
            idRecipe = step.idRecipe,
            content = step.content,
            image = step.image
        )
        repositoryStep.saveStep(stepEdit)
        currentStep.value = null
    }
}