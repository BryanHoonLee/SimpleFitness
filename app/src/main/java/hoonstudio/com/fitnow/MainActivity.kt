package hoonstudio.com.fitnow

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var exerciseCategoryViewModel: ExerciseCategoryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseCategoryAdapter

    companion object{
        private val ADD_EXERCISE_CATEGORY_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var buttonAddExerciseCategory = findViewById<FloatingActionButton>(R.id.button_add_category)
        buttonAddExerciseCategory.setOnClickListener(View.OnClickListener {
            intent = Intent(this, AddCategoryActivity::class.java)
            startActivityForResult(intent, ADD_EXERCISE_CATEGORY_REQUEST)
        })

       initRecyclerView()

        // you pass an activity or fragment in .of so that the view model knows which lifecycle it has to be scoped to.
        // in this case the android system will destroy this view model when this activity is finished.
        exerciseCategoryViewModel = ViewModelProviders.of(this).get(ExerciseCategoryViewModel::class.java)
        if(exerciseCategoryViewModel.isAllExerciseCategoryInit) {
            // since getAll returns live data, we can use the live data method observe.
            // takes 2 parameters. First is the lifeCycleOwner. You pass either activity or fragment. This is needed since
            // live data is lifecycle aware and it will only update the activity/fragment if it is in the foreground and also
            // clean up the reference to the activity/fragment when the activity/fragment is destroyed.
            // This avoids memory leaks and crashes.
            //
            // Second parameter takes in an observer which you can pass as an anonymous inner class.
            exerciseCategoryViewModel.getAllExerciseCategory().observe(this, Observer<List<ExerciseCategory>> {
                // update recyclerview
                Log.d("MainActivity", it.size.toString())
                adapter.setExerciseCategoryList(it)

            })
        }

        initTouchHelper()

    }

    private fun initTouchHelper(){
        val background = ColorDrawable()
        val deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)
        val intrinsicWidth = deleteIcon!!.intrinsicWidth
        val intrinsicHeight = deleteIcon!!.intrinsicHeight

        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                initAlertBuilder(viewHolder)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top

                background.color = ResourcesCompat.getColor(resources, R.color.bg_row_background, null)
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(c)

                val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
                val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
                val deleteIconRight = itemView.right - deleteIconMargin
                val deleteIconBottom = deleteIconTop + intrinsicHeight

                deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                deleteIcon.draw(c)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }


        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun initAlertBuilder(viewHolder: RecyclerView.ViewHolder){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setCancelable(false)
        builder.setTitle("Delete Confirmation")
        builder.setMessage("Are you sure you want to delete your ${adapter.getExerciseCategoryAt(viewHolder.adapterPosition).category} category?")
        builder.setPositiveButton("Delete"){dialog, which ->
            exerciseCategoryViewModel.delete(adapter.getExerciseCategoryAt(viewHolder.adapterPosition))
            Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT).show()

        }

        builder.setNeutralButton("Cancel"){_,_ ->
            adapter.notifyItemChanged(viewHolder.adapterPosition)
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun initRecyclerView(){
        recyclerView= findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        adapter= ExerciseCategoryAdapter()
        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ADD_EXERCISE_CATEGORY_REQUEST && resultCode == Activity.RESULT_OK){
            var categoryName = data!!.getStringExtra(AddCategoryActivity.EXTRA_CATEGORY_NAME)

            var exerciseCategoryActivity = ExerciseCategory(0, categoryName)
            exerciseCategoryViewModel.insert(exerciseCategoryActivity)

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT)
        }else{
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean  = when (item!!.itemId){
            R.id.delete_all_categories -> {
                exerciseCategoryViewModel.deleteAllExerciseCategory()
                Toast.makeText(this, "Deleted All Categories", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                 super.onOptionsItemSelected(item)
            }
    }
}
