package hoonstudio.com.fitnow

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
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
    private lateinit var editTextTimer: EditText
    private lateinit var buttonSetsDecrement: ImageButton
    private lateinit var buttonSetsIncrement: ImageButton
    private lateinit var buttonRepsDecrement: ImageButton
    private lateinit var buttonRepsIncrement: ImageButton

    private var exerciseId: Long = 0
    private var exerciseCategoryId: Long = 0
    private lateinit var exerciseName: String
    private lateinit var currentExercise: ExerciseItem

    private val job = Job()
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

    companion object {
        val EXTRA_EXERCISE_ID = "hoonstudio.com.fitnow.EditExerciseItemActivity.EXTRA_EXERCISE_ID"
        val EXTRA_EXERCISE_NAME = "hoonstudio.com.fitnow.EditExerciseItemActivity.EXTRA_EXERCISE_NAME"
        val EXTRA_EXERCISE_CATEGORY_ID = "hoonstudio.com.fitnow.EditExerciseItemActivity.EXTRA_EXERCISE_CATEGORY_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exercise_item)

        exerciseId = intent.getLongExtra(EXTRA_EXERCISE_ID, 0)
        exerciseCategoryId = intent.getLongExtra(EXTRA_EXERCISE_CATEGORY_ID, 0)
        exerciseName = intent.getStringExtra(EXTRA_EXERCISE_NAME)

        exerciseItemViewModel = ViewModelProviders.of(this).get(ExerciseItemViewModel::class.java)

        uiScope.launch {
            currentExercise = exerciseItemViewModel.getExerciseItemById(exerciseId)
            initView()
            setView()
        }

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        setTitle("Edit ${exerciseName}")
    }

    private fun initView() {
        editTextExerciseName = findViewById(R.id.edit_text_exercise_name)
        editTextSets = findViewById(R.id.edit_text_sets)
        editTextReps = findViewById(R.id.edit_text_reps)
        editTextWeight = findViewById(R.id.edit_text_weight)
        editTextTimer = findViewById(R.id.edit_text_timer)
        buttonSetsDecrement = findViewById(R.id.button_sets_decrement)
        buttonSetsIncrement = findViewById(R.id.button_sets_increment)
        buttonRepsDecrement = findViewById(R.id.button_reps_decrement)
        buttonRepsIncrement = findViewById(R.id.button_reps_increment)


        editTextExerciseName.setText(currentExercise.exerciseName)
        editTextSets.setText(currentExercise.sets.toString())
        editTextReps.setText(currentExercise.reps.toString())
        editTextWeight.setText(currentExercise.weight.toString())
        editTextTimer.setText(currentExercise.timer.toString())
    }

    private fun setView() {
        var current: Int = 0
        buttonSetsIncrement.setOnClickListener(View.OnClickListener {
            current = editTextSets.text.toString().toInt() + 1
            if(current < 0){
                editTextSets.setText(0.toString())
            }else{
                editTextSets.setText(current.toString())

            }
        })
        buttonSetsDecrement.setOnClickListener(View.OnClickListener {
            current = editTextSets.text.toString().toInt() - 1
            if(current < 0){
                editTextSets.setText(0.toString())
            }else{
                editTextSets.setText(current.toString())
            }
        })
        buttonRepsIncrement.setOnClickListener(View.OnClickListener {
            current = editTextReps.text.toString().toInt() + 1
            if(current < 0){
                editTextReps.setText(0.toString())
            }else{
                editTextReps.setText(current.toString())
            }
        })
        buttonRepsDecrement.setOnClickListener(View.OnClickListener {
            current = editTextReps.text.toString().toInt() - 1
            if(current < 0){
                editTextReps.setText(0.toString())
            }else{
                editTextReps.setText(current.toString())
            }
        })
    }


    private fun saveExercise() {
        currentExercise.exerciseName = editTextExerciseName.text.toString()
        currentExercise.sets = editTextSets.text.toString().toInt()
        currentExercise.reps = editTextReps.text.toString().toInt()
        currentExercise.weight = editTextWeight.text.toString().toDouble()
        currentExercise.timer = editTextTimer.text.toString().toInt()

        if (editTextExerciseName.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Insert An Exercise Name", Toast.LENGTH_SHORT).show()
            return
        } else if (editTextSets.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Insert a value for Sets", Toast.LENGTH_SHORT).show()
            return
        } else if (editTextReps.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Insert a value for Reps", Toast.LENGTH_SHORT).show()
            return
        } else if (editTextWeight.text.toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Insert a value for Weight", Toast.LENGTH_SHORT).show()
            return
        } else if (editTextTimer.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please Insert a value for Timer", Toast.LENGTH_SHORT).show()
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
