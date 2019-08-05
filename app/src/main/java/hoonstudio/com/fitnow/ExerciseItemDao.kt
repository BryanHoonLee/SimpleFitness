package hoonstudio.com.fitnow

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExerciseItemDao{

    @Query("SELECT * FROM exercise_item_table")
    fun getAll(): LiveData<List<ExerciseItem>>

    @Query("SELECT * FROM exercise_item_table WHERE exercise_item_category_id = :exerciseItemCategoryId")
    fun getAllByExerciseCategoryId(exerciseItemCategoryId: Long): LiveData<List<ExerciseItem>>

    @Insert
    fun insertAll(vararg exerciseItems: ExerciseItem)
}