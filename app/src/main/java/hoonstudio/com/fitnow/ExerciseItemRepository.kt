package hoonstudio.com.fitnow

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseItemRepository(application: Application){

    private var exerciseItemDao: ExerciseItemDao

    init{
        val database = ExerciseDatabase.getInstance(application)!!
        exerciseItemDao = database.exerciseItemDao()

    }

    fun insert(exerciseItem: ExerciseItem){
        InsertExerciseItem(exerciseItemDao).insert(exerciseItem)
    }

    fun update(exerciseItem: ExerciseItem){
        UpdateExerciseItem(exerciseItemDao).update(exerciseItem)
    }

    fun delete(exerciseItem: ExerciseItem){
        DeleteExerciseItem(exerciseItemDao).delete(exerciseItem)

    }

    suspend fun getExerciseById(exerciseItemId: Long): ExerciseItem{
        return GetExerciseItemById(exerciseItemDao).getExerciseItemById(exerciseItemId)
    }

    fun getAllExerciseItemById(exerciseCategoryId: Long): LiveData<List<ExerciseItem>>{
        return GetAllExerciseItemById(exerciseItemDao).getAllExerciseItemById(exerciseCategoryId)
    }

    companion object{
        private val scope = CoroutineScope(Dispatchers.Default)

        private class InsertExerciseItem internal constructor(private val exerciseItemDao: ExerciseItemDao?){
            fun insert(exerciseItem: ExerciseItem) = scope.launch {
                exerciseItemDao!!.insert(exerciseItem)
            }
        }

        private class UpdateExerciseItem internal constructor(private val exerciseItemDao: ExerciseItemDao?){
            fun update(exerciseItem: ExerciseItem) = scope.launch {
                exerciseItemDao!!.update(exerciseItem)
            }
        }

        private class DeleteExerciseItem internal constructor(private val exerciseItemDao: ExerciseItemDao?){
            fun delete(exerciseItem: ExerciseItem) = scope.launch {
                exerciseItemDao!!.delete(exerciseItem)
            }
        }

        private class GetExerciseItemById internal constructor(private val exerciseItemDao: ExerciseItemDao?){
            suspend fun getExerciseItemById(exerciseItemId: Long): ExerciseItem{
                return withContext(Dispatchers.IO){
                    exerciseItemDao!!.getExerciseById(exerciseItemId)
                }
            }
        }

        private class GetAllExerciseItemById internal constructor(private val exerciseItemDao: ExerciseItemDao?){
            fun getAllExerciseItemById(exerciseCategoryId: Long): LiveData<List<ExerciseItem>>{
                return exerciseItemDao!!.getAllByExerciseCategoryId(exerciseCategoryId)
            }
        }
    }
}