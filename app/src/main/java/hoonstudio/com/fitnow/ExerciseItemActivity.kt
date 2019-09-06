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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ExerciseItemActivity : AppCompatActivity(), ExerciseItemAdapter.OnExerciseItemListener {
    private lateinit var exerciseItemViewModel: ExerciseItemViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseItemAdapter

//    private lateinit var buttonSetsIncrement: Button
//    private lateinit var buttonSetsDecrement: Button
//    private lateinit var buttonRepsIncrement: Button
//    private lateinit var buttonRepsDecrement: Button
//    private lateinit var buttonWeightIncrement: Button
//    private lateinit var buttonWeightDecrement: Button
//    private lateinit var textViewSets: EditText
//    private lateinit var textViewReps: EditText
//    private lateinit var textViewWeight: EditText
    private lateinit var buttonAddExercise: FloatingActionButton
    private lateinit var currentExerciseItem: ExerciseItem

    private var categoryId: Long = 0
    private lateinit var categoryName: String

    companion object{
        private val ADD_EXERCISE_REQUEST = 1
        private val EDIT_EXERCISE_REQUEST = 2

        val CATEGORY_ID_EXTRA = "hoonstudio.com.fitnow.ExerciseItemActivity.CATEGORY_ID_EXTRA"
        val CATEGORY_NAME_EXTRA = "hoonstudio.com.fitnow.ExerciseItemActivity.CATEGORY_NAME_EXTRA"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)


        Log.d("ExerciseItemActivity", "Before initView")
        initViews()
        Log.d("ExerciseItemActivity", "After initView")
        setViews()
        initRecyclerView()
        initTouchHelper()

        categoryId = intent.getLongExtra(CATEGORY_ID_EXTRA, 0)
        categoryName = intent.getStringExtra(CATEGORY_NAME_EXTRA)

        exerciseItemViewModel = ViewModelProviders.of(this).get(ExerciseItemViewModel::class.java)
        exerciseItemViewModel.getAllExerciseItemById(categoryId).observe(this, Observer<List<ExerciseItem>> {
            adapter.setExerciseItemList(it)
        })

        setTitle("${categoryName} Exercises")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == ADD_EXERCISE_REQUEST && resultCode == Activity.RESULT_OK){
            var exerciseName = data!!.getStringExtra(AddExerciseItemActivity.EXTRA_EXERCISE_NAME)

            var exerciseItem= ExerciseItem(0, categoryId, exerciseName, 0.0,0, 0)
            exerciseItemViewModel.insert(exerciseItem)

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }else if(requestCode == EDIT_EXERCISE_REQUEST && resultCode == Activity.RESULT_OK){
            Toast.makeText(this, "Edit Saved", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onExerciseItemClick(position: Int) {
        currentExerciseItem = adapter.getExerciseItemAt(position)
        startEditActivity(currentExerciseItem)
    }

    private fun initViews(){
        buttonAddExercise = findViewById<FloatingActionButton>(R.id.button_add_exercise)
    }

    private fun initTouchHelper() {
        val background = ColorDrawable()
        val deleteIcon = ContextCompat.getDrawable(this, R.drawable.ic_delete)
        val editIcon = ContextCompat.getDrawable(this, R.drawable.ic_edit)
        val editIconIntrinsicWidth = editIcon!!.intrinsicWidth
        val editIconIntrinsicHeight = editIcon!!.intrinsicHeight
        val deleteIconIntrinsicWidth = deleteIcon!!.intrinsicWidth
        val deleteIconIntrinsicHeight = deleteIcon!!.intrinsicHeight
0
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if(direction == ItemTouchHelper.LEFT){
                    initAlertBuilder(viewHolder)
                }else if(direction == ItemTouchHelper.RIGHT){
                    startEditActivity(adapter.getExerciseItemAt(viewHolder.adapterPosition))
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

                if(dX < 0) {
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
                }else{
                    val itemView = viewHolder.itemView
                    val itemHeight = itemView.bottom - itemView.top

                    background.color = ResourcesCompat.getColor(resources, R.color.edit_icon_color, null)
                    background.setBounds(
                        itemView.left,
                        itemView.top,
                        itemView.right ,
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
    private fun initAlertBuilder(viewHolder: RecyclerView.ViewHolder) {
        val builder = AlertDialog.Builder(this@ExerciseItemActivity)
        builder.setCancelable(false)
        builder.setTitle("Delete Confirmation")
        builder.setMessage("Are you sure you want to delete your ${adapter.getExerciseItemAt(viewHolder.adapterPosition).exerciseName} categoryName?")
        builder.setPositiveButton("Delete") { _, _ ->
            exerciseItemViewModel.delete(adapter.getExerciseItemAt(viewHolder.adapterPosition))
            Toast.makeText(this@ExerciseItemActivity, "Deleted", Toast.LENGTH_SHORT).show()
        }

        builder.setNeutralButton("Cancel") { _, _ ->
            adapter.notifyItemChanged(viewHolder.adapterPosition)
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun startEditActivity(exerciseItem: ExerciseItem){
        intent = Intent(this, EditExerciseItemActivity::class.java)
        var exerciseId = exerciseItem.id
        var exerciseName = exerciseItem.exerciseName
        intent.putExtra(EditExerciseItemActivity.EXTRA_EXERCISE_ID, exerciseId)
        intent.putExtra(EditExerciseItemActivity.EXTRA_EXERCISE_NAME, exerciseName)
        intent.putExtra(EditExerciseItemActivity.EXTRA_EXERCISE_CATEGORY_ID, categoryId)
        startActivityForResult(intent, EDIT_EXERCISE_REQUEST)
    }

    private fun setViews(){
        buttonAddExercise.setOnClickListener(View.OnClickListener {
            intent = Intent(this, AddExerciseItemActivity::class.java)
            intent.putExtra(AddExerciseItemActivity.EXTRA_CATEGORY_NAME, categoryName)
            startActivityForResult(intent, ADD_EXERCISE_REQUEST)
        })
    }

    private fun initRecyclerView(){
        recyclerView= findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        adapter= ExerciseItemAdapter(this)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var menuInflater = menuInflater
        menuInflater.inflate(R.menu.exercise_item_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item!!.itemId) {
        R.id.add_category -> {
            intent = Intent(this, AddExerciseItemActivity::class.java)
            startActivityForResult(intent, ADD_EXERCISE_REQUEST)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
