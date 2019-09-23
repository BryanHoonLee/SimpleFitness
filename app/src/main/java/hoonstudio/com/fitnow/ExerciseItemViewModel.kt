package hoonstudio.com.fitnow

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ExerciseItemViewModel(application: Application) : AndroidViewModel(application) {
    private var exerciseItemRepository: ExerciseItemRepository

    init {
        exerciseItemRepository = ExerciseItemRepository(application)
    }

    fun insert(exerciseItem: ExerciseItem) {
        exerciseItemRepository.insert(exerciseItem)
    }

    fun update(exerciseItem: ExerciseItem) {
        exerciseItemRepository.update(exerciseItem)
    }

    fun delete(exerciseItem: ExerciseItem) {
        exerciseItemRepository.delete(exerciseItem)
    }

    suspend fun getExerciseItemById(exerciseItemId: Long): ExerciseItem {
        return exerciseItemRepository.getExerciseById(exerciseItemId)
    }

    fun getAllExerciseItemById(exerciseCategoryId: Long): LiveData<List<ExerciseItem>> {
        return exerciseItemRepository.getAllExerciseItemById(exerciseCategoryId)
    }


}