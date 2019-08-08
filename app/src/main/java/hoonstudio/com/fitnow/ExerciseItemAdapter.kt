package hoonstudio.com.fitnow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseItemAdapter: RecyclerView.Adapter<ExerciseItemAdapter.ExerciseItemViewHolder>(){
    private var exerciseItemList = emptyList<ExerciseItem>()

    fun setExerciseItemList(exerciseItemList: List<ExerciseItem>){
        this.exerciseItemList = exerciseItemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseItemViewHolder {
        var itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return exerciseItemList.size
    }

    override fun onBindViewHolder(holder: ExerciseItemViewHolder, position: Int) {
        var current = exerciseItemList.get(position)
        holder.textViewExercise.text = current.exerciseName

    }

    class ExerciseItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var textViewExercise: TextView

        init{
            textViewExercise = itemView.findViewById(R.id.text_view_exercise)
        }

    }
}