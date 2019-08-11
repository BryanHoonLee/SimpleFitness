package hoonstudio.com.fitnow

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExerciseItemActivity : AppCompatActivity() {
    private lateinit var exerciseItemViewModel: ExerciseItemViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseItemAdapter

    private lateinit var buttonSetsIncrement: Button
    private lateinit var buttonSetsDecrement: Button
    private lateinit var buttonRepsIncrement: Button
    private lateinit var buttonRepsDecrement: Button
    private lateinit var buttonWeightIncrement: Button
    private lateinit var buttonWeightDecrement: Button
    private lateinit var editTextSets: EditText
    private lateinit var editTextReps: EditText
    private lateinit var editTextWeight: EditText
    private lateinit var buttonAddExercise: FloatingActionButton

    private  var categoryId: Long = 0


    companion object{
        private val ADD_EXERCISE_REQUEST = 1
        private val CATEGORY_ID_EXTRA = "hoonstudio.com.fitnow.CATEGORY_ID_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        initViews()
        initRecyclerView()

        categoryId = intent.getLongExtra(CATEGORY_ID_EXTRA, 0)

        exerciseItemViewModel = ViewModelProviders.of(this).get(ExerciseItemViewModel::class.java)
        exerciseItemViewModel.getAllExerciseItemById(categoryId).observe(this, Observer<List<ExerciseItem>> {
            adapter.setExerciseItemList(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ADD_EXERCISE_REQUEST && resultCode == Activity.RESULT_OK){
            var exerciseName = data!!.getStringExtra(AddExerciseItemActivity.EXTRA_EXERCISE_NAME)

            var exerciseItem= ExerciseItem(0, categoryId, exerciseName, 0.0,0, 0)
            exerciseItemViewModel.insert(exerciseItem)

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT)
        }else{
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT)
        }
    }

    private fun initViews(){
        buttonAddExercise = findViewById<FloatingActionButton>(R.id.button_add_exercise)
        buttonAddExercise.setOnClickListener(View.OnClickListener {
            intent = Intent(this, AddExerciseItemActivity::class.java)
            startActivityForResult(intent, ExerciseItemActivity.ADD_EXERCISE_REQUEST)
        })

        buttonRepsDecrement = findViewById(R.id.button_reps_decrement)
        buttonRepsIncrement = findViewById(R.id.button_reps_increment)
        buttonSetsDecrement = findViewById(R.id.button_sets_decrement)
        buttonSetsIncrement = findViewById(R.id.button_sets_increment)
        buttonWeightDecrement = findViewById(R.id.button_weight_decrement)
        buttonWeightIncrement = findViewById(R.id.button_weight_increment)
        editTextReps = findViewById(R.id.edit_text_reps)
        editTextSets = findViewById(R.id.edit_text_sets)
        editTextWeight = findViewById(R.id.edit_text_weight)
    }

    private fun initRecyclerView(){
        recyclerView= findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        adapter= ExerciseItemAdapter()
        recyclerView.adapter = adapter
    }
}
