package ru.netology.nerecipe

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    fun getAll(): LiveData<List<RecipeEntity>>

    @Insert
    fun insert(recipe: RecipeEntity): Long


    @Query("UPDATE recipes SET name = :name, content = :content, author = :author, categoryRecipe = :categoryRecipe, image = :image WHERE id = :id")
    fun updateRecipeById(id: Long, name: String, author: String, content: String, categoryRecipe: String, image: String)


    @Query("""
        UPDATE recipes SET
        favourite = CASE WHEN favourite THEN 0 ELSE 1 END
        WHERE id = :id
        """)
    fun favourite(id: Long)

    @Query("DELETE FROM recipes WHERE id = :id")
    fun removeById(id: Long)

    @Query("SELECT * FROM steps ORDER BY idRecipe AND idStep DESC")
    fun getAllStep(): LiveData<List<StepEntity>>

    @Insert
    fun insertStep(step: StepEntity)

    @Query("DELETE FROM steps WHERE idStep = :id AND idRecipe = :idRecipe")
    fun removeStepById(id: Long, idRecipe: Long)

    @Query("UPDATE steps SET content = :content, image = :image WHERE idStep = :id AND idRecipe = :idRecipe")
    fun updateStepById(id: Long, idRecipe: Long, content: String, image: String)

}