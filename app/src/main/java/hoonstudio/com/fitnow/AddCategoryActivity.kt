package hoonstudio.com.fitnow

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProviders

class AddCategoryActivity : AppCompatActivity() {
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

    companion object {
        val EXTRA_CATEGORY_NAME = "hoonstudio.com.fitnow.AddCategoryActivity.EXTRA_CATEGORY_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        initView()

        exerciseCategoryViewModel = ViewModelProviders.of(this).get(ExerciseCategoryViewModel::class.java)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        setTitle("Add Exercise Category")
    }

    private fun saveExerciseCategory() {
        var categoryName = editTextCategoryName.text.toString()

        if (categoryName.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Category Name", Toast.LENGTH_SHORT)
            return
        }

        setBooleanDays()

        var exerciseCategoryActivity = ExerciseCategory(
            0, categoryName, 0, booleanDayMonday,
            booleanDayTuesday, booleanDayWednesday, booleanDayThursday, booleanDayFriday, booleanDaySaturday,
            booleanDaySunday
        )
        exerciseCategoryViewModel.insert(exerciseCategoryActivity)

        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun setBooleanDays() {
        if (buttonDayMonday.isChecked) {
            booleanDayMonday = true
        }
        if (buttonDayTuesday.isChecked) {
            booleanDayTuesday = true
        }
        if (buttonDayWednesday.isChecked) {
            booleanDayWednesday = true
        }
        if (buttonDayThursday.isChecked) {
            booleanDayThursday = true
        }
        if (buttonDayFriday.isChecked) {
            booleanDayFriday = true
        }
        if (buttonDaySaturday.isChecked) {
            booleanDaySaturday = true
        }
        if (buttonDaySunday.isChecked) {
            booleanDaySunday = true
        }
    }

    private fun initView() {
        editTextCategoryName = findViewById(R.id.edit_text_category_name)
        buttonDayMonday = findViewById(R.id.button_day_monday)
        buttonDayTuesday = findViewById(R.id.button_day_tuesday)
        buttonDayWednesday = findViewById(R.id.button_day_wednesday)
        buttonDayThursday = findViewById(R.id.button_day_thursday)
        buttonDayFriday = findViewById(R.id.button_day_friday)
        buttonDaySaturday = findViewById(R.id.button_day_saturday)
        buttonDaySunday = findViewById(R.id.button_day_sunday)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_category_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.save_category -> {
                saveExerciseCategory()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
