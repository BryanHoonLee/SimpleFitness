package hoonstudio.com.fitnow

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExerciseItemDao{

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(exerciseItem: ExerciseItem)

    @Update
    suspend fun update(exerciseItem: ExerciseItem)

    @Delete
    suspend fun delete(exerciseItem: ExerciseItem)

    @Query("SELECT * FROM exercise_item_table")
    fun getAll(): LiveData<List<ExerciseItem>>

    @Query("SELECT * FROM exercise_item_table WHERE id = :exerciseItemId")
    fun getExerciseById(exerciseItemId: Long): ExerciseItem

    @Query("SELECT * FROM exercise_item_table WHERE exercise_item_category_id = :exerciseItemCategoryId")
    fun getAllByExerciseCategoryId(exerciseItemCategoryId: Long): LiveData<List<ExerciseItem>>


}