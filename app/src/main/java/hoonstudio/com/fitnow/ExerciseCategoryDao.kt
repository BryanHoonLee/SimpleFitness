package hoonstudio.com.fitnow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface ExerciseCategoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(exerciseCategory: ExerciseCategory)

    @Update
    suspend fun update(exerciseCategory: ExerciseCategory)

    @Delete
    suspend fun delete(exerciseCategory: ExerciseCategory)

    //Query is for custom database operations
    @Query("DELETE FROM exercise_category_table")
    suspend fun deleteAllCategories()

    @Query("SELECT * FROM exercise_category_table WHERE id = :exerciseCategoryId")
    fun getExerciseCategoryById(exerciseCategoryId: Long) : ExerciseCategory

    // cant use suspend with livedata because livedata is already working in background thread
    //https://stackoverflow.com/questions/54566663/room-dao-livedata-as-return-type-causing-compile-time-error
    @Query("SELECT * FROM exercise_category_table ORDER BY categoryName DESC")
    fun getAllCategories(): LiveData<List<ExerciseCategory>>


}