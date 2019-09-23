package hoonstudio.com.fitnow

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator

class ExerciseCountDownTimer(
    var holder: ExerciseItemAdapter.ExerciseItemViewHolder,
    millisInFuture: Long,
    countDownInterval: Long
) : CountDownTimer(millisInFuture, countDownInterval) {

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