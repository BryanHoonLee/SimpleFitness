package hoonstudio.com.fitnow

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

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

    suspend fun getExerciseItemById(exerciseItemId: Long): ExerciseItem{
        return exerciseItemRepository.getExerciseById(exerciseItemId)
    }

    fun getAllExerciseItemById(exerciseCategoryId: Long): LiveData<List<ExerciseItem>>{
        return exerciseItemRepository.getAllExerciseItemById(exerciseCategoryId)
    }

    fun getCountDownTimer(holder: ExerciseItemAdapter.ExerciseItemViewHolder): LiveData<CountDownTimer>{
        var countDownTimer = object : CountDownTimer(convertToMillis(holder.timer), 1000){
            override fun onTick(millisUntilFinished: Long) {
                holder.textViewTimer.setText(formatTime(millisUntilFinished))
                var progress: Int = convertToSeconds(millisUntilFinished)
                holder.progressBar.setProgress(progress)
            }

            override fun onFinish() {
                holder.textViewTimer.setText("00:00")
                holder.toggleButtonTimer.toggle()
                vibrate(holder)
            }
        }

        return countDownTimer
    }

    private fun convertToMillis(i: Int): Long {
        var convert = i.toString()
        convert += "000"
        return convert.toLong()
    }

    private fun convertToSeconds(millis: Long): Int {
        return (millis / 1000).toString().toInt()
    }

    fun formatTime(millis: Long): String {
        val seconds = millis / 1000
        return String.format("%02d:%02d", seconds % 3600 / 60, seconds % 60)
    }

    private fun vibrate(holder: ExerciseItemAdapter.ExerciseItemViewHolder) {
        val vibrator = holder.toggleButtonTimer.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(1200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(1200)
        }
    }

}