package hoonstudio.com.fitnow

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        private class GetAllExerciseItemById internal constructor(private val exerciseItemDao: ExerciseItemDao?){
            fun getAllExerciseItemById(exerciseCategoryId: Long): LiveData<List<ExerciseItem>>{
                return exerciseItemDao!!.getAllByExerciseCategoryId(exerciseCategoryId)
            }
        }
    }
}