package hoonstudio.com.fitnow

import android.app.Activity
import android.content.Intent
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExerciseCategoryActivity : AppCompatActivity(), ExerciseCategoryAdapter.OnCategoryListener {
    private lateinit var exerciseCategoryViewModel: ExerciseCategoryViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseCategoryAdapter

    companion object {
        private val ADD_EXERCISE_CATEGORY_REQUEST = 1
        private val EDIT_EXERCISE_CATEGORY_REQUEST = 2
    }

    override fun onCategoryClick(position: Int) {
        var categoryId = adapter.getExerciseCategoryAt(position).id
        var categoryName = adapter.getExerciseCategoryAt(position).categoryName
        var intent = Intent(this, ExerciseItemActivity::class.java)
        intent.putExtra(ExerciseItemActivity.CATEGORY_NAME_EXTRA, categoryName)
        intent.putExtra(ExerciseItemActivity.CATEGORY_ID_EXTRA, categoryId)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_category)
        setTitle("Categories")


        var buttonAddExerciseCategory = findViewById<FloatingActionButton>(R.id.button_add_category)
        buttonAddExerciseCategory.setOnClickListener(View.OnClickListener {
            intent = Intent(this, AddCategoryActivity::class.java)
            startActivityForResult(intent, ADD_EXERCISE_CATEGORY_REQUEST)
        })

        initRecyclerView()

/*         you pass an activity or fragment in .of so that the view model knows which lifecycle it has to be scoped to.
         in this case the android system will destroy this view model when this activity is finished.*/
        exerciseCategoryViewModel = ViewModelProviders.of(this).get(ExerciseCategoryViewModel::class.java)
        if (exerciseCategoryViewModel.isAllExerciseCategoryInit) {
/*             since getAll returns live data, we can use the live data method observe.
             takes 2 parameters. First is the lifeCycleOwner. You pass either activity or fragment. This is needed since
             live data is lifecycle aware and it will only update the activity/fragment if it is in the foreground and also
            clean up the reference to the activity/fragment when the activity/fragment is destroyed.
             This avoids memory leaks and crashes.

             Second parameter takes in an observer which you can pass as an anonymous inner class.*/
            exerciseCategoryViewModel.getAllExerciseCategory().observe(this, Observer<MutableList<ExerciseCategory>> {


                for (i in 0 until it.size) {
                    var exerciseCount = exerciseCategoryViewModel.getExerciseCount(it[i].id)
                    exerciseCount.observe(this@ExerciseCategoryActivity, Observer { count ->
                        if (count != it[i].exerciseCount) {
                            it[i].exerciseCount = count
                            exerciseCategoryViewModel.update(it[i])
                        }
                    })
                }
                // update recyclerview
                adapter.setExerciseCategoryList(it)
            })
        }

        initTouchHelper()
    }

    /**
     * Constructs an ItemTouchHelper() that attaches to the recycler view so items can slide to the left
     * for a delete function
     */
    private fun initTouchHelper() {
        val background = ColorDrawable()
        val deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)
        val editIcon = ContextCompat.getDrawable(this, R.drawable.ic_edit)
        val editIconIntrinsicWidth = editIcon!!.intrinsicWidth
        val editIconIntrinsicHeight = editIcon!!.intrinsicHeight
        val deleteIconIntrinsicWidth = deleteIcon!!.intrinsicWidth
        val deleteIconIntrinsicHeight = deleteIcon!!.intrinsicHeight

        val itemTouchCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    if (direction == ItemTouchHelper.LEFT) {
                        initDeleteAlertBuilder(viewHolder)
                    } else if (direction == ItemTouchHelper.RIGHT) {
                        startEditActivity(adapter.getExerciseCategoryAt(viewHolder.adapterPosition))
                        adapter.notifyItemChanged(viewHolder.adapterPosition)
                    }
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

                    if (dX < 0) {
                        val itemView = viewHolder.itemView
                        val itemHeight = itemView.bottom - itemView.top

                        background.color = ResourcesCompat.getColor(resources, R.color.bg_row_background, null)
                        background.setBounds(
                            itemView.right,
                            itemView.top,
                            itemView.left,
                            itemView.bottom
                        )
                        background.draw(c)

                        val deleteIconTop = itemView.top + (itemHeight - deleteIconIntrinsicHeight) / 2
                        val deleteIconMargin = (itemHeight - deleteIconIntrinsicHeight) / 2
                        val deleteIconLeft = itemView.right - deleteIconMargin - deleteIconIntrinsicWidth
                        val deleteIconRight = itemView.right - deleteIconMargin
                        val deleteIconBottom = deleteIconTop + deleteIconIntrinsicHeight

                        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                        deleteIcon.draw(c)

                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    } else {
                        val itemView = viewHolder.itemView
                        val itemHeight = itemView.bottom - itemView.top

                        background.color = ResourcesCompat.getColor(resources, R.color.edit_icon_color, null)
                        background.setBounds(
                            itemView.left,
                            itemView.top,
                            itemView.right,
                            itemView.bottom
                        )
                        background.draw(c)

                        val editIconTop = itemView.top + (itemHeight - editIconIntrinsicHeight) / 2
                        val editIconMargin = (itemHeight - editIconIntrinsicHeight) / 2
                        val editIconLeft = itemView.left + editIconMargin
                        val editIconRight = itemView.left + editIconMargin + editIconIntrinsicWidth
                        val editIconBottom = editIconTop + deleteIconIntrinsicHeight

                        editIcon.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom)
                        editIcon.draw(c)

                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    }
                }
            }


        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    /**
     * Creates an Alert Dialog to ask for user confirmation when deleting a categoryName
     * @param viewHolder    Need the position of the item from the current viewholder
     */
    private fun initDeleteAlertBuilder(viewHolder: RecyclerView.ViewHolder) {
        val builder = AlertDialog.Builder(this@ExerciseCategoryActivity)
        builder.setCancelable(false)
        builder.setTitle("Delete Confirmation")
        builder.setMessage("Are you sure you want to delete your ${adapter.getExerciseCategoryAt(viewHolder.adapterPosition).categoryName} categoryName?")
        builder.setPositiveButton("Delete") { _, _ ->
            exerciseCategoryViewModel.delete(adapter.getExerciseCategoryAt(viewHolder.adapterPosition))
            Toast.makeText(this@ExerciseCategoryActivity, "Deleted", Toast.LENGTH_SHORT).show()

        }

        builder.setNeutralButton("Cancel") { _, _ ->
            adapter.notifyItemChanged(viewHolder.adapterPosition)
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun initDeleteAllAlertBuilder() {
        val builder = AlertDialog.Builder(this@ExerciseCategoryActivity)
        builder.setCancelable(false)
        builder.setTitle("Delete All Confirmation")
        builder.setMessage("Are you sure you want to delete all of your categories?")
        builder.setPositiveButton("Delete All") { _, _ ->
            exerciseCategoryViewModel.deleteAllExerciseCategory()
            Toast.makeText(this, "All Categories Deleted", Toast.LENGTH_SHORT).show()
        }
        builder.setNeutralButton("Cancel") { _, _ ->

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun startEditActivity(exerciseCategory: ExerciseCategory) {
        intent = Intent(this, EditExerciseCategoryActivity::class.java)
        var exerciseCategoryId = exerciseCategory.id
        var exerciseCategoryName = exerciseCategory.categoryName
        intent.putExtra(EditExerciseCategoryActivity.EXTRA_CATEGORY_ID, exerciseCategoryId)
        intent.putExtra(EditExerciseCategoryActivity.EXTRA_EXERCISE_NAME, exerciseCategoryName)
        startActivityForResult(intent, EDIT_EXERCISE_CATEGORY_REQUEST)
    }

    /**
     *
     */
    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        adapter = ExerciseCategoryAdapter(this)
        recyclerView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_EXERCISE_CATEGORY_REQUEST && resultCode == Activity.RESULT_OK) {
//            var categoryName = data!!.getStringExtra(AddCategoryActivity.EXTRA_CATEGORY_NAME)
//
//            var exerciseCategoryActivity = ExerciseCategory(0, categoryName, 0, false,
//                false, false, false, false, false,
//                false)
//            exerciseCategoryViewModel.insert(exerciseCategoryActivity)

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_EXERCISE_CATEGORY_REQUEST && resultCode == Activity.RESULT_OK) {
            Toast.makeText(this, "Edit Saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.exercise_category_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item!!.itemId) {
        R.id.add_category -> {
            intent = Intent(this, AddCategoryActivity::class.java)
            startActivityForResult(intent, ADD_EXERCISE_CATEGORY_REQUEST)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
