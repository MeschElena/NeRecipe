package ru.netology.nerecipe

interface StepRecipeIteractionListener {
    fun onStepRemoveClicked(step: Step)
    fun onStepEditClicked(step: Step)
}