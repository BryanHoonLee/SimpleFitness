package hoonstudio.com.fitnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditExerciseItemActivity : AppCompatActivity() {
    private lateinit var exerciseItemViewModel: ExerciseItemViewModel

    private lateinit var editTextExerciseName: EditText
    private lateinit var editTextSets: EditText
    private lateinit var editTextReps: EditText
    private lateinit var editTextWeight: EditText
    private lateinit var buttonSetsDecrement: ImageButton
    private lateinit var buttonSetsIncrement: ImageButton
    private lateinit var buttonRepsDecrement: ImageButton
    private lateinit var buttonRepsIncrement: ImageButton
    private lateinit var buttonWeightDecrement: ImageButton
    private lateinit var buttonWeightIncrement: ImageButton

    private var exerciseId: Long = 0
    private var exerciseCategoryId: Long = 0
    private lateinit var exerciseName: String
    private lateinit var currentExercise: ExerciseItem

    private val job = Job()
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

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

        uiScope.launch {
            currentExercise = exerciseItemViewModel.getExerciseItemById(exerciseId)
            initViews()
        }



        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        setTitle("Edit ${exerciseName}")
    }

    fun initViews(){
        editTextExerciseName = findViewById(R.id.edit_text_exercise_name)
        editTextSets = findViewById(R.id.edit_text_sets)
        editTextReps = findViewById(R.id.edit_text_reps)
        editTextWeight = findViewById(R.id.edit_text_weight)
        buttonSetsDecrement = findViewById(R.id.button_sets_decrement)
        buttonSetsIncrement = findViewById(R.id.button_sets_increment)
        buttonRepsDecrement = findViewById(R.id.button_reps_decrement)
        buttonRepsIncrement = findViewById(R.id.button_reps_increment)
        buttonWeightDecrement = findViewById(R.id.button_weight_decrement)
        buttonWeightIncrement = findViewById(R.id.button_weight_increment)

        if(::currentExercise.isInitialized){
            editTextExerciseName.setText(currentExercise.exerciseName)

        }
    }

    fun setViews(){

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
