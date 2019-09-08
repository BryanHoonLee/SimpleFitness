package hoonstudio.com.fitnow

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

class AddExerciseItemActivity : AppCompatActivity() {
    private lateinit var exerciseItemViewModel: ExerciseItemViewModel

    private lateinit var editTextExerciseName: EditText
    private lateinit var editTextSets: EditText
    private lateinit var editTextReps: EditText
    private lateinit var editTextWeight: EditText

    private lateinit var categoryName: String
    private var categoryId: Long = 0

    companion object {
        val EXTRA_EXERCISE_NAME = "hoonstudio.com.fitnow.AddExerciseItemActivity.EXTRA_EXERCISE_NAME"
        val EXTRA_CATEGORY_NAME = "hoonstudio.com.fitnow.AddExerciseItemActivity.EXTRA_CATEGORY_NAME"
        val EXTRA_CATEGORY_ID = "hoonstudio.com.fitnow.AddExerciseItemActivity.EXTRA_CATEGORY_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)

        exerciseItemViewModel = ViewModelProviders.of(this).get(ExerciseItemViewModel::class.java)

        categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME)
        categoryId = intent.getLongExtra(EXTRA_CATEGORY_ID, 0)

        editTextExerciseName = findViewById(R.id.edit_text_exercise_name)
        editTextSets = findViewById(R.id.edit_text_sets)
        editTextReps = findViewById(R.id.edit_text_reps)
        editTextWeight = findViewById(R.id.edit_text_weight)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        setTitle("Add $categoryName Exercise")
    }

    private fun saveExercise() {
        var exerciseName = editTextExerciseName.text.toString()
        var sets = editTextSets.text.toString().toInt()
        var reps = editTextReps.text.toString().toInt()
        var weight = editTextWeight.text.toString().toDouble()

        if (exerciseName.trim().isEmpty()) {
            Toast.makeText(this, "Please Insert An Exercise Name", Toast.LENGTH_SHORT)
            return
        }

        var exerciseItem= ExerciseItem(0, categoryId, exerciseName, weight,sets, reps)
        exerciseItemViewModel.insert(exerciseItem)

        setResult(Activity.RESULT_OK)
        finish()


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
