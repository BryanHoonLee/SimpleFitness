package hoonstudio.com.fitnow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ExerciseItemViewModel(application : Application) : AndroidViewModel(application){
    private var exerciseItemRepository: ExerciseItemRepository

    init {
        exerciseItemRepository = ExerciseItemRepository(application)
    }

    fun insert(exerciseItem: ExerciseItem){
        exerciseItemRepository.insert(exerciseItem)
    }

    fun update(exerciseItem: ExerciseItem){
        exerciseItemRepository.update(exerciseItem)
    }

    fun delete(exerciseItem: ExerciseItem){
        exerciseItemRepository.delete(exerciseItem)
    }

    fun getAllExerciseItemById(exerciseCategoryId: Long): LiveData<List<ExerciseItem>>{
        return exerciseItemRepository.getAllExerciseItemById(exerciseCategoryId)
    }

}