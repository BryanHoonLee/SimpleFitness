package hoonstudio.com.fitnow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseItemAdapter(onExerciseItemListener: OnExerciseItemListener): RecyclerView.Adapter<ExerciseItemAdapter.ExerciseItemViewHolder>(){
    private var exerciseItemList = emptyList<ExerciseItem>()
    private var onExerciseItemListener: OnExerciseItemListener

    init{
        this.onExerciseItemListener = onExerciseItemListener
    }

    fun setExerciseItemList(exerciseItemList: List<ExerciseItem>){
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

    fun getExerciseItemAt(position: Int): ExerciseItem{
        return exerciseItemList.get(position)
    }

    override fun onBindViewHolder(holder: ExerciseItemViewHolder, position: Int) {
        var current = exerciseItemList.get(position)
        holder.textViewExercise.text = current.exerciseName
        holder.textViewSets.setText(current.sets.toString())
        holder.textViewReps.setText(current.reps.toString())
        holder.textViewWeight.setText(current.weight.toString())

    }

    class ExerciseItemViewHolder(itemView: View, onExerciseItemListener: OnExerciseItemListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        lateinit var textViewExercise: TextView
        lateinit var textViewSets: TextView
        lateinit var textViewReps: TextView
        lateinit var textViewWeight: TextView

        var onExerciseItemListener: OnExerciseItemListener

        init{
            initViews()
            itemView.setOnClickListener(this)
            this.onExerciseItemListener = onExerciseItemListener
        }

        override fun onClick(v: View?) {
            onExerciseItemListener.onExerciseItemClick(adapterPosition)
        }

        private fun initViews(){
            textViewExercise = itemView.findViewById(R.id.edit_text_exercise_name)
            textViewReps = itemView.findViewById(R.id.edit_text_reps)
            textViewSets = itemView.findViewById(R.id.edit_text_sets)
            textViewWeight = itemView.findViewById(R.id.text_view_weight)
        }
    }

    interface OnExerciseItemListener{
        fun onExerciseItemClick(position: Int)
    }
}