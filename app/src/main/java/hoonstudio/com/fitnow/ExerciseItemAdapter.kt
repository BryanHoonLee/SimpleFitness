package hoonstudio.com.fitnow

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
        holder.editTextSets.setText(current.sets.toString())
        holder.editTextReps.setText(current.reps.toString())
        holder.editTextWeight.setText(current.weight.toString())

    }

    class ExerciseItemViewHolder(itemView: View, onExerciseItemListener: OnExerciseItemListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        lateinit var textViewExercise: TextView
        lateinit var buttonSetsIncrement: ImageButton
        lateinit var buttonSetsDecrement: ImageButton
        lateinit var buttonRepsIncrement: ImageButton
        lateinit var buttonRepsDecrement: ImageButton
        lateinit var buttonWeightIncrement: ImageButton
        lateinit var buttonWeightDecrement: ImageButton
        lateinit var editTextSets: EditText
        lateinit var editTextReps: EditText
        lateinit var editTextWeight: EditText

        var onExerciseItemListener: OnExerciseItemListener

        init{
            initViews()

            this.onExerciseItemListener = onExerciseItemListener
        }

        override fun onClick(v: View?) {
            onExerciseItemListener.onExerciseItemClick(adapterPosition)
        }

        private fun initViews(){
            textViewExercise = itemView.findViewById(R.id.text_view_exercise)
            buttonRepsDecrement = itemView.findViewById(R.id.button_reps_decrement)
            buttonRepsIncrement = itemView.findViewById(R.id.button_reps_increment)
            buttonSetsDecrement = itemView.findViewById(R.id.button_sets_decrement)
            buttonSetsIncrement = itemView.findViewById(R.id.button_sets_increment)
            buttonWeightDecrement = itemView.findViewById(R.id.button_weight_decrement)
            buttonWeightIncrement = itemView.findViewById(R.id.button_weight_increment)
            editTextReps = itemView.findViewById(R.id.edit_text_reps)
            editTextSets = itemView.findViewById(R.id.edit_text_sets)
            editTextWeight = itemView.findViewById(R.id.edit_text_weight)


        }
    }

    interface OnExerciseItemListener{
        fun onExerciseItemClick(position: Int)
    }
}