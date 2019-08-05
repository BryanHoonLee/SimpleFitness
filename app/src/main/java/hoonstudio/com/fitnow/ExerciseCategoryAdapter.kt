package hoonstudio.com.fitnow

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseCategoryAdapter: RecyclerView.Adapter<ExerciseCategoryAdapter.ExerciseCategoryHolder>(){

    private var exerciseCategoryList = emptyList<ExerciseCategory   >()

    fun setExerciseCategoryList(exerciseCategoryList: List<ExerciseCategory>){
        this.exerciseCategoryList = exerciseCategoryList
        notifyDataSetChanged()
    }

    fun getExerciseCategoryAt(position: Int): ExerciseCategory{
        return exerciseCategoryList.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseCategoryHolder {
        var itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.exercise_category_item, parent, false)
        return ExerciseCategoryHolder(itemView)
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount", exerciseCategoryList.size.toString())
        return exerciseCategoryList.size
    }

    override fun onBindViewHolder(holder: ExerciseCategoryHolder, position: Int) {
        var current = exerciseCategoryList.get(position)
        holder.textViewCategory.text = current.category
    }

    class ExerciseCategoryHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var textViewCategory: TextView

        init{
            textViewCategory = itemView.findViewById(R.id.text_view_category)
        }
    }
}