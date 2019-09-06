package hoonstudio.com.fitnow

import android.app.Activity
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView

class ExerciseCategoryAdapter(onCategoryListener: OnCategoryListener): RecyclerView.Adapter<ExerciseCategoryAdapter.ExerciseCategoryHolder>(){

    private var exerciseCategoryList = emptyList<ExerciseCategory   >()
    private var onCategoryListener: OnCategoryListener

    init {
        this.onCategoryListener = onCategoryListener
    }

    fun setExerciseCategoryList(exerciseCategoryList: List<ExerciseCategory>){
        this.exerciseCategoryList = exerciseCategoryList
        notifyDataSetChanged()
    }

    fun getExerciseCategoryAt(position: Int): ExerciseCategory{
        return exerciseCategoryList.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseCategoryHolder {
        var itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_category, parent, false)
        return ExerciseCategoryHolder(itemView, onCategoryListener)
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount", exerciseCategoryList.size.toString())
        return exerciseCategoryList.size
    }

    override fun onBindViewHolder(holder: ExerciseCategoryHolder, position: Int) {
        var current = exerciseCategoryList.get(position)
        var categoryName = SpannableString(current.categoryName + "\n" + current.exerciseCount + " Exercises")
        categoryName.setSpan(RelativeSizeSpan(0.5f), current.categoryName.length, categoryName.length, 0)
        holder.textViewCategory.setText(categoryName)
    }

    class ExerciseCategoryHolder(itemView: View, onCategoryListener: OnCategoryListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var textViewCategory: TextView

        var onCategoryListener: OnCategoryListener


        init{
            textViewCategory = itemView.findViewById(R.id.text_view_category)
            itemView.setOnClickListener(this)
            this.onCategoryListener = onCategoryListener
        }

        override fun onClick(v: View?) {
            onCategoryListener.onCategoryClick(adapterPosition)
        }

    }

    interface OnCategoryListener{
        fun onCategoryClick(position: Int)
    }
}