package hoonstudio.com.fitnow

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseCategoryRepository{

    private  var exerciseCategoryDao: ExerciseCategoryDao
    private lateinit var allExerciseCategory: LiveData<List<ExerciseCategory>>
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

    }

    fun delete(exerciseCategory: ExerciseCategory){
        DeleteExerciseCategory(exerciseCategoryDao).delete(exerciseCategory)
    }

    fun deleteAllExerciseCategory(){
        DeleteAllExerciseCategory(exerciseCategoryDao).deleteAll()
    }

    fun getAllExerciseCategory(): LiveData<List<ExerciseCategory>>{
        return allExerciseCategory
    }


    companion object{
        private val scope = CoroutineScope(Dispatchers.Default)
        private class InsertExerciseCategory internal constructor(private val exerciseCategoryDao: ExerciseCategoryDao?){


            fun insert(exerciseCategory: ExerciseCategory) = scope.launch{
                exerciseCategoryDao?.insert(exerciseCategory)
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
    }
}