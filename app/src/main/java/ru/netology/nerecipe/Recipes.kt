package ru.netology.nerecipe

import androidx.core.net.toUri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

internal fun RecipeEntity.toModel() = Recipe(
    id = id,
    author = author,
    content = content,
    name = name,
    categoryRecipe = categoryRecipe,
    image = image.toUri(),
    favourite = favourite
)

internal fun Recipe.toEntity() = RecipeEntity(
    id = id,
    author = author,
    content = content,
    name = name,
    categoryRecipe = categoryRecipe,
    image = image.toString(),
    favourite = favourite
)

internal fun StepEntity.toModel() = Step(
    idStep = idStep,
    idRecipe = idRecipe,
    content = content,
    image = image.toUri()
)

internal fun Step.toEntity() = StepEntity(
    idStep = idStep,
    idRecipe = idRecipe,
    content = content,
    image = image.toString()
)

//abstract class StepConverter {
//    @TypeConverter
//    fun mapListToString(value: List<Step>): String {
//        val gson = Gson()
//        val type = object : TypeToken<List<Step>>() {}.type
//        return gson.toJson(value, type)
//    }
//
//    @TypeConverter
//    fun mapStringToList(value: String): List<Step> {
//        val gson = Gson()
//        val type = object : TypeToken<List<Step>>() {}.type
//        return gson.fromJson(value, type)
//    }
//}