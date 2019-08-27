package hoonstudio.com.fitnow

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

class AddExerciseItemActivity : AppCompatActivity() {
    private lateinit var editTextExercise: EditText

    companion object{
         val EXTRA_EXERCISE_NAME = "hoonstudio.com.fitnow.EXTRA_EXERCISE_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exercise)

        editTextExercise = findViewById<EditText>(R.id.edit_text_exercise)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        setTitle("Add Exercise")
    }

    private fun saveExercise(){
        var exerciseName = editTextExercise.text.toString()

        if(exerciseName.trim().isEmpty()){
            Toast.makeText(this, "Please Insert An Exercise Name", Toast.LENGTH_SHORT)
            return
        }

        var data = Intent()
        data.putExtra(EXTRA_EXERCISE_NAME, exerciseName)

        setResult(Activity.RESULT_OK, data)
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
