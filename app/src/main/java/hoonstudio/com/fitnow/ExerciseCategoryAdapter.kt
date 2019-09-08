package hoonstudio.com.fitnow

import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
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
        var categoryName = current.categoryName
        var numberExercise = "${current.exerciseCount} Exercises"
        holder.textViewCategory.setText(categoryName)
        holder.textViewNumberExercises.setText(numberExercise)

        var toggledDays = mutableListOf<String>()
        if (current.dayMonday){
            toggledDays.add("Mo")
        }
        if(current.dayTuesday){
            toggledDays.add("Tu")
        }
        if(current.dayWednesday){
            toggledDays.add("We")
        }
        if(current.dayThursday){
            toggledDays.add("Th")
        }
        if(current.dayFriday){
            toggledDays.add("Fr")
        }
        if(current.daySaturday){
            toggledDays.add("Sa")
        }
        if(current.daySunday){
            toggledDays.add("Su")
        }

        var days: String = ""
        if(toggledDays.size == 7){
            days = "Every Day"
        }else{
            for(i in 0 until toggledDays.size){
                if(i == toggledDays.size-1){
                    days += "${toggledDays.get(i)}"
                }else if(toggledDays.size > 1 && i == toggledDays.size - 2){
                    days += "${toggledDays.get(i)} & "
                }else{
                    days += "${toggledDays.get(i)}, "

                }
            }
        }


        holder.textViewDays.setText(days)
    }

    class ExerciseCategoryHolder(itemView: View, onCategoryListener: OnCategoryListener): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var textViewCategory: TextView
        var textViewDays: TextView
        var textViewNumberExercises: TextView
//        var buttonDayMonday: ToggleButton
//        var buttonDayTuesday: ToggleButton
//        var buttonDayWednesday: ToggleButton
//        var buttonDayThursday: ToggleButton
//        var buttonDayFriday: ToggleButton
//        var buttonDaySaturday: ToggleButton
//        var buttonDaySunday: ToggleButton
        //for setting text for days, put all enabled days (toggle for each) into array/list and for loop. size - 2 for inserting &amp;

        var onCategoryListener: OnCategoryListener


        init{
            textViewCategory = itemView.findViewById(R.id.text_view_category)
            textViewDays = itemView.findViewById(R.id.text_view_days)
            textViewNumberExercises = itemView.findViewById(R.id.text_view_number_exercises)
//            buttonDayMonday = itemView.findViewById(R.id.button_day_monday)
//            buttonDayTuesday = itemView.findViewById(R.id.button_day_tuesday)
//            buttonDayWednesday = itemView.findViewById(R.id.button_day_wednesday)
//            buttonDayThursday = itemView.findViewById(R.id.button_day_thursday)
//            buttonDayFriday = itemView.findViewById(R.id.button_day_friday)
//            buttonDaySaturday = itemView.findViewById(R.id.button_day_saturday)
//            buttonDaySunday = itemView.findViewById(R.id.button_day_sunday)

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