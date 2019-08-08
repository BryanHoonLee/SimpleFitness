package hoonstudio.com.fitnow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

class ExerciseItemActivity : AppCompatActivity() {
    private lateinit var exerciseItemViewModel: ExerciseItemViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_exercise)
    }
}
