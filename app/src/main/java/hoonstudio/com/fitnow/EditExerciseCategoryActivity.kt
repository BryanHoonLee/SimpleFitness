package hoonstudio.com.fitnow

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class EditExerciseCategoryActivity : AppCompatActivity() {
    private lateinit var exerciseCategoryViewModel: ExerciseCategoryViewModel

    private lateinit var editTextCategoryName: EditText
    private lateinit var buttonDayMonday: ToggleButton
    private lateinit var buttonDayTuesday: ToggleButton
    private lateinit var buttonDayWednesday: ToggleButton
    private lateinit var buttonDayThursday: ToggleButton
    private lateinit var buttonDayFriday: ToggleButton
    private lateinit var buttonDaySaturday: ToggleButton
    private lateinit var buttonDaySunday: ToggleButton

    private var booleanDayMonday: Boolean = false
    private var booleanDayTuesday: Boolean = false
    private var booleanDayWednesday: Boolean = false
    private var booleanDayThursday: Boolean = false
    private var booleanDayFriday: Boolean = false
    private var booleanDaySaturday: Boolean = false
    private var booleanDaySunday: Boolean = false

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

        buttonDayMonday = findViewById(R.id.button_day_monday)
        buttonDayTuesday = findViewById(R.id.button_day_tuesday)
        buttonDayWednesday = findViewById(R.id.button_day_wednesday)
        buttonDayThursday = findViewById(R.id.button_day_thursday)
        buttonDayFriday = findViewById(R.id.button_day_friday)
        buttonDaySaturday = findViewById(R.id.button_day_saturday)
        buttonDaySunday = findViewById(R.id.button_day_sunday)

        if(currentExerciseCategory.dayMonday){
            buttonDayMonday.toggle()
        }
        if(currentExerciseCategory.dayTuesday){
            buttonDayTuesday.toggle()
        }
        if(currentExerciseCategory.dayWednesday){
            buttonDayWednesday.toggle()
        }
        if(currentExerciseCategory.dayThursday){
            buttonDayThursday.toggle()
        }
        if(currentExerciseCategory.dayFriday){
            buttonDayFriday.toggle()
        }
        if(currentExerciseCategory.daySaturday){
            buttonDaySaturday.toggle()
        }
        if(currentExerciseCategory.daySunday){
            buttonDaySunday.toggle()
        }
    }


    fun saveExercise(){
        currentExerciseCategory.categoryName = editTextCategoryName.text.toString()

        if(editTextCategoryName.text.toString().trim().isEmpty()){
            Toast.makeText(this, "Please Insert A Category Name", Toast.LENGTH_SHORT).show()
            return
        }

        setBooleanDays()

        exerciseCategoryViewModel.update(currentExerciseCategory)

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun setBooleanDays() {
        currentExerciseCategory.dayMonday = buttonDayMonday.isChecked
        currentExerciseCategory.dayTuesday = buttonDayTuesday.isChecked
        currentExerciseCategory.dayWednesday = buttonDayWednesday.isChecked
        currentExerciseCategory.dayThursday = buttonDayThursday.isChecked
        currentExerciseCategory.dayFriday = buttonDayFriday.isChecked
        currentExerciseCategory.daySaturday = buttonDaySaturday.isChecked
        currentExerciseCategory.daySunday = buttonDaySunday.isChecked
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
