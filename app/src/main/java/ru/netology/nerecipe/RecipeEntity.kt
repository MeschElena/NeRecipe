package ru.netology.nerecipe

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "recipes")
class RecipeEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "categoryRecipe")
    val categoryRecipe: String,
    @ColumnInfo(name = "favourite")
    val favourite: Boolean,
    @ColumnInfo(name = "image")
    val image: String = "",
//    @ColumnInfo(name = "steps")
//    @TypeConverters(StepConverter::class)
//    val steps: List<Step>,
)


//class StepConverter {
//    @TypeConverter
//    fun mapListToString(value: List<Step>): String {
//        val gson = Gson()
//        val type = object : TypeToken<List<Step>>() {}.type
//        return gson.toJson(value, type)
//    }
//    @TypeConverter
//    fun mapStringToList(value: String): List<Step> {
//        val gson = Gson()
//        val type = object : TypeToken<List<Step>>() {}.type
//        return gson.fromJson(value, type)
//    }

//}
@Entity(tableName = "steps", foreignKeys = arrayOf(ForeignKey(entity = RecipeEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("idRecipe"),
    onDelete = ForeignKey.CASCADE)))

class StepEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idStep")
    val idStep: Long,
    @ColumnInfo(name = "idRecipe", index = true)
    val idRecipe: Long,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "image")
    val image: String = "",
)