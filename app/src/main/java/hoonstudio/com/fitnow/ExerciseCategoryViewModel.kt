package hoonstudio.com.fitnow

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class ExerciseCategoryViewModel (@NonNull application: Application) : AndroidViewModel(application) {

    private var exerciseCategoryRepository: ExerciseCategoryRepository
    private  lateinit var allExerciseCategory: LiveData<MutableList<ExerciseCategory>>
    var isAllExerciseCategoryInit: Boolean = false

    init{
        exerciseCategoryRepository = ExerciseCategoryRepository(application)
        if(exerciseCategoryRepository.isAllExerciseCategoryInit) {
            allExerciseCategory = exerciseCategoryRepository.getAllExerciseCategory()
            isAllExerciseCategoryInit = true
        }

    }

    fun insert(exerciseCategory: ExerciseCategory){
        exerciseCategoryRepository.insert(exerciseCategory)
    }

    fun update(exerciseCategory: ExerciseCategory){
        exerciseCategoryRepository.update(exerciseCategory)
    }

     fun getExerciseCount(exerciseCategoryId: Long) : LiveData<Int> {
        return exerciseCategoryRepository.getExerciseCount(exerciseCategoryId)
    }

    fun delete(exerciseCategory: ExerciseCategory){
        exerciseCategoryRepository.delete(exerciseCategory)
    }

    fun deleteAllExerciseCategory(){
        exerciseCategoryRepository.deleteAllExerciseCategory()
    }

    suspend fun getExerciseCategoryById(exerciseCategoryId: Long): ExerciseCategory{
        return exerciseCategoryRepository.getExerciseCategoryById(exerciseCategoryId)
    }

    fun getAllExerciseCategory(): LiveData<MutableList<ExerciseCategory>>{
        return allExerciseCategory
    }


}