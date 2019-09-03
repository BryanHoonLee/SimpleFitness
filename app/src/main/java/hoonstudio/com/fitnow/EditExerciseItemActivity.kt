package hoonstudio.com.fitnow

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
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

    companion object {
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

    fun initViews() {
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

        editTextExerciseName.setText(currentExercise.exerciseName)
        editTextSets.setText(currentExercise.sets.toString())
        editTextReps.setText(currentExercise.reps.toString())
        editTextWeight.setText(currentExercise.weight.toString())
    }

    fun setViews() {

    }


    private fun saveExercise(){
        currentExercise.exerciseName = editTextExerciseName.text.toString()
        currentExercise.sets = editTextSets.text.toString().toInt()
        currentExercise.reps = editTextReps.text.toString().toInt()
        currentExercise.weight = editTextWeight.text.toString().toDouble()

        if(editTextExerciseName.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please Insert An Exercise Name", Toast.LENGTH_SHORT).show()
            return
        }else if(editTextSets.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please Insert a value for Sets", Toast.LENGTH_SHORT).show()
            return
        }else if(editTextReps.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please Insert a value for Reps", Toast.LENGTH_SHORT).show()
            return
        }else if(editTextWeight.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please Insert a value for Weight", Toast.LENGTH_SHORT).show()
            return
        }

        exerciseItemViewModel.update(currentExercise)

        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_exercise_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.save_exercise -> {
                saveExercise()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
