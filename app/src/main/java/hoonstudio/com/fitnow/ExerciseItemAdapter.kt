package hoonstudio.com.fitnow

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import android.os.CountDownTimer
import android.os.Vibrator
import android.os.VibrationEffect
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders


class ExerciseItemAdapter(onExerciseItemListener: OnExerciseItemListener) :
    RecyclerView.Adapter<ExerciseItemAdapter.ExerciseItemViewHolder>() {
    private var exerciseItemList = emptyList<ExerciseItem>()
    private var onExerciseItemListener: OnExerciseItemListener

    init {
        this.onExerciseItemListener = onExerciseItemListener
    }

    fun setExerciseItemList(exerciseItemList: List<ExerciseItem>) {
        this.exerciseItemList = exerciseItemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseItemViewHolder {
        var itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)

        return ExerciseItemViewHolder(itemView, onExerciseItemListener)
    }

    override fun getItemCount(): Int {
        return exerciseItemList.size
    }

    fun getExerciseItemAt(position: Int): ExerciseItem {
        return exerciseItemList.get(position)
    }

    override fun onBindViewHolder(holder: ExerciseItemViewHolder, position: Int) {
        var current = exerciseItemList.get(position)
        holder.timer = current.timer
        holder.textViewExercise.text = current.exerciseName
        holder.textViewSets.setText(current.sets.toString())
        holder.textViewReps.setText(current.reps.toString())
        holder.textViewWeight.setText(current.weight.toString())
        holder.textViewTimer.setText(formatTime(convertToMillis(current.timer)))
        holder.progressBar.max = current.timer
        holder.progressBar.progress = current.timer
        holder.countDownTimer = ExerciseCountDownTimer(holder, convertToMillis(current.timer), 1000)

        setView(holder)
//        initTimer(holder)
// holder gets outdated maybe.

    }

    private fun initTimer(holder: ExerciseItemViewHolder) {
        holder.countDownTimer = object : CountDownTimer(convertToMillis(holder.timer), 1000) {
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
    }

    private fun setView(holder: ExerciseItemViewHolder) {
        holder.toggleButtonTimer.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorPrimary))


        holder.toggleButtonTimer.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.toggleButtonTimer.setTextColor(
                    ContextCompat.getColor(
                        holder.toggleButtonTimer.context,
                        R.color.red_stop
                    )
                )
                holder.progressBar.setProgress(holder.timer)
                holder.countDownTimer.start()
            } else {
                holder.countDownTimer.cancel()
                holder.toggleButtonTimer.setTextColor(
                    ContextCompat.getColor(
                        holder.toggleButtonTimer.context,
                        R.color.colorPrimary
                    )
                )
                holder.progressBar.setProgress(holder.timer)
                holder.textViewTimer.setText(formatTime(convertToMillis(holder.timer)))
            }
        }

         holder.progressBar.setProgress(holder.timer)

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

    private fun vibrate(holder: ExerciseItemViewHolder) {
        val vibrator = holder.toggleButtonTimer.context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(1200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(1200)
        }
    }

    class ExerciseItemViewHolder(itemView: View, onExerciseItemListener: OnExerciseItemListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        lateinit var textViewExercise: TextView
        lateinit var textViewSets: TextView
        lateinit var textViewReps: TextView
        lateinit var textViewWeight: TextView
        lateinit var textViewTimer: TextView
        lateinit var progressBar: ProgressBar
        lateinit var toggleButtonTimer: ToggleButton
        lateinit var countDownTimer: CountDownTimer

        var timer: Int = 1

        var onExerciseItemListener: OnExerciseItemListener

        init {
            initView()
            itemView.setOnClickListener(this)
            this.onExerciseItemListener = onExerciseItemListener
        }

        override fun onClick(v: View?) {
            onExerciseItemListener.onExerciseItemClick(adapterPosition)
        }

        private fun initView() {
            textViewExercise = itemView.findViewById(R.id.edit_text_exercise_name)
            textViewReps = itemView.findViewById(R.id.edit_text_reps)
            textViewSets = itemView.findViewById(R.id.edit_text_sets)
            textViewWeight = itemView.findViewById(R.id.text_view_weight)
            textViewTimer = itemView.findViewById(R.id.text_view_timer)
            progressBar = itemView.findViewById(R.id.progress_bar)
            toggleButtonTimer = itemView.findViewById(R.id.toggle_button_timer)


        }
    }

    interface OnExerciseItemListener {
        fun onExerciseItemClick(position: Int)
    }
}