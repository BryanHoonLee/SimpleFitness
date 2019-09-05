package hoonstudio.com.fitnow

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditExerciseCategoryActivity : AppCompatActivity() {
    private lateinit var exerciseCategoryViewModel: ExerciseCategoryViewModel

    private lateinit var editTextCategoryName: EditText

    private lateinit var currentExerciseCategory: ExerciseCategory

    private var exerciseCategoryId: Long = 0
    private lateinit var exerciseCategoryName: String

    private val job = Job()
    protected val uiScope = CoroutineScope(Dispatchers.Main + job)

    companion object{
        val EXTRA_CATEGORY_ID = "hoonstudio.com.fitnow.EditExerciseCategoryActivity.EXTRA_CATEGORY_ID"
        val EXTRA_EXERCISE_NAME = "hoonstudio.com.fitnow.EditExerciseCategoryActivity.EXTRA_EXERCISE_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exercise_category)

        exerciseCategoryId = intent.getLongExtra(EXTRA_CATEGORY_ID, 0)
        exerciseCategoryName = intent.getStringExtra(EXTRA_EXERCISE_NAME)

        this.exerciseCategoryViewModel = ViewModelProviders.of(this).get(ExerciseCategoryViewModel::class.java)

        uiScope.launch{
            currentExerciseCategory = exerciseCategoryViewModel.getExerciseCategoryById(exerciseCategoryId)
            initView()
        }

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        setTitle("Edit $exerciseCategoryName Category")
    }

    fun initView(){
        editTextCategoryName = findViewById(R.id.edit_text_category_name)

        editTextCategoryName.setText(currentExerciseCategory.categoryName)
    }

    fun saveExercise(){
        currentExerciseCategory.categoryName = editTextCategoryName.text.toString()

        if(editTextCategoryName.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please Insert A Category Name", Toast.LENGTH_SHORT).show()
            return
        }

        exerciseCategoryViewModel.update(currentExerciseCategory)

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
