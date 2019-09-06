package hoonstudio.com.fitnow

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExerciseCategoryRepository{

    private  var exerciseCategoryDao: ExerciseCategoryDao
    private lateinit var allExerciseCategory: LiveData<MutableList<ExerciseCategory>>
    private val scope = CoroutineScope(Dispatchers.Default)
    var isAllExerciseCategoryInit: Boolean = false


    constructor(application: Application) {
        val database = ExerciseDatabase.getInstance(application)!!
        exerciseCategoryDao = database.exerciseCategoryDao()
        allExerciseCategory = exerciseCategoryDao.getAllCategories()
        if(this::allExerciseCategory.isInitialized){
            isAllExerciseCategoryInit = true
        }
    }
    
    fun insert(exerciseCategory: ExerciseCategory){
        InsertExerciseCategory(exerciseCategoryDao).insert(exerciseCategory)
    }

    fun update(exerciseCategory: ExerciseCategory){
        UpdateExerciseCategory(exerciseCategoryDao).update(exerciseCategory)
    }

    fun delete(exerciseCategory: ExerciseCategory){
        DeleteExerciseCategory(exerciseCategoryDao).delete(exerciseCategory)
    }

     fun getExerciseCount(exerciseCategoryId: Long): LiveData<Int> {
        return GetExerciseCount(exerciseCategoryDao).getExerciseCount(exerciseCategoryId)
    }

    fun deleteAllExerciseCategory(){
        DeleteAllExerciseCategory(exerciseCategoryDao).deleteAll()
    }

    suspend fun getExerciseCategoryById(exerciseCategoryId: Long): ExerciseCategory{
        return GetExerciseCategoryById(exerciseCategoryDao).getExerciseCategoryById(exerciseCategoryId)
    }

    fun getAllExerciseCategory(): LiveData<MutableList<ExerciseCategory>>{
        return allExerciseCategory
    }


    companion object{
        private val scope = CoroutineScope(Dispatchers.Default)
        private class InsertExerciseCategory internal constructor(private val exerciseCategoryDao: ExerciseCategoryDao?){
            fun insert(exerciseCategory: ExerciseCategory) = scope.launch{
                exerciseCategoryDao?.insert(exerciseCategory)
            }
        }

        private class UpdateExerciseCategory internal constructor(private val exerciseCategoryDao: ExerciseCategoryDao?){
            fun update(exerciseCategory: ExerciseCategory) = scope.launch {
                exerciseCategoryDao?.update(exerciseCategory)
            }
        }

        private class GetExerciseCount internal constructor(private val exerciseCategoryDao: ExerciseCategoryDao?){
             fun getExerciseCount(exerciseCategoryId: Long) : LiveData<Int>{
                return exerciseCategoryDao!!.getExerciseCount(exerciseCategoryId)

            }
        }

        private class DeleteExerciseCategory internal constructor(private val exerciseCategoryDao: ExerciseCategoryDao?){
            fun delete(exerciseCategory: ExerciseCategory) = scope.launch{
                exerciseCategoryDao?.delete(exerciseCategory)
            }
        }

        private class DeleteAllExerciseCategory internal constructor(private val exerciseCategoryDao: ExerciseCategoryDao?){
            fun deleteAll() = scope.launch {
                exerciseCategoryDao?.deleteAllCategories()
            }
        }

        private class GetExerciseCategoryById internal constructor(private val exerciseCategoryDao: ExerciseCategoryDao?){
            suspend fun getExerciseCategoryById(exerciseCategoryId: Long): ExerciseCategory {
                return withContext(Dispatchers.IO){
                    exerciseCategoryDao!!.getExerciseCategoryById(exerciseCategoryId)
                }

            }
        }
    }
}