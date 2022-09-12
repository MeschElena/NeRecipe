package ru.netology.nerecipe

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

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
    val idEditeRecipe = MutableLiveData<Long?>(null)
    val currentRecipe = MutableLiveData<Recipe?>(null)
    val currentStep = MutableLiveData<Step?>(null)
    val navigateToRecipeScreenEven = SingleLiveEvent<Long>()
//    val navigateToPostContentViewEven = SingleLiveEvent<String>()
    val navigateToStepScreenView = SingleLiveEvent<Long>()


    fun onSaveButtonClicked(recipe: Recipe) {
        if (recipe.content.isBlank() || recipe.author.isBlank()
            || recipe.name.isBlank() || recipe.categoryRecipe.isBlank()) return
        val recipeEdit = currentRecipe.value?.copy(
            content = recipe.content,
            name = recipe.name,
            author = recipe.author,
            categoryRecipe = recipe.categoryRecipe,
            image = recipe.image
        ) ?: Recipe(
            id = RecipeRepository.NEW_RECIPE_ID,
            content = recipe.content,
            name = recipe.name,
            author = recipe.author,
            categoryRecipe = recipe.categoryRecipe,
            image = recipe.image
        )
        idEditeRecipe.value = repository.save(recipeEdit)
        currentRecipe.value = null
    }

    override fun onFavouriteClicked(recipe: Recipe) = repository.favourite(recipe.id)

    override fun onRemoveClicked(recipe: Recipe) = repository.delete(recipe.id)

    override fun onEditClicked(recipe: Recipe) {
        navigateToRecipeScreenEven.value = recipe.id
        currentRecipe.value = recipe
    }

    fun onAddClicked(){
      //  navigateToRecipeScreenEven.call()
        navigateToRecipeScreenEven.value = -1L
    }

    fun getRecipe(recipeId:Long) = checkNotNull(repository.getAll().value).filter {it.id == recipeId}[0]

    fun onAddClickedStep() {
       // navigateToStepScreenView.call()
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