package hoonstudio.com.fitnow

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast

class AddCategoryActivity : AppCompatActivity() {
    private lateinit var editTextCategoryName: EditText

    companion object{
         val EXTRA_CATEGORY_NAME = "hoonstudio.com.fitnow.AddCategoryActivity.EXTRA_CATEGORY_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        editTextCategoryName = findViewById<EditText>(R.id.edit_text_category_name)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_close)
        setTitle("Add Exercise Category")
    }

    private fun saveExerciseCategory(){
        var categoryName = editTextCategoryName.text.toString()

        if(categoryName.trim().isEmpty()){
            Toast.makeText(this, "Please insert a Category Name", Toast.LENGTH_SHORT)
            return
        }

        var data = Intent()
        data.putExtra(EXTRA_CATEGORY_NAME, categoryName)

        setResult(Activity.RESULT_OK, data)
        finish()


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
