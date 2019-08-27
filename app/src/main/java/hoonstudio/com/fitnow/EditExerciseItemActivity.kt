package hoonstudio.com.fitnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class EditExerciseItemActivity : AppCompatActivity() {
    private lateinit var exerciseItemViewModel: ExerciseItemViewModel

    private var exerciseId: Long = 0
    private var exerciseCategoryId: Long = 0
    private lateinit var exerciseName: String

    companion object{
        val EXERCISE_ID_EXTRA = "hoonstudio.com.fitnow.EditExerciseItemActivity.EXERCISE_ID_EXTRA"
        val EXERCISE_NAME_EXTRA = "hoonstudio.com.fitnow.EditExerciseItemActivity.EXERCISE_NAME_EXTRA"
        val EXERCISE_CATEGORY_ID_EXTRA = "hoonstudio.com.fitnow.EditExerciseItemActivity.EXERCISE_CATEGORY_ID_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exercise_item)

        exerciseId = intent.getLongExtra(EXERCISE_ID_EXTRA, 0)
        exerciseCategoryId = intent.getLongExtra(EXERCISE_CATEGORY_ID_EXTRA, 0)
        exerciseName = intent.getStringExtra(EXERCISE_NAME_EXTRA)

        exerciseItemViewModel = ViewModelProviders.of(this).get(ExerciseItemViewModel::class.java)
        exerciseItemViewModel.getAllExerciseItemById(exerciseCategoryId).observe(this, Observer<List<ExerciseItem>> {

        })

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        setTitle("Edit ${exerciseName}")
    }
}
