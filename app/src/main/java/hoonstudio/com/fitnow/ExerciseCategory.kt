package hoonstudio.com.fitnow

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_category_table")
data class ExerciseCategory(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long,
                            @ColumnInfo(name = "category") var category: String){

}