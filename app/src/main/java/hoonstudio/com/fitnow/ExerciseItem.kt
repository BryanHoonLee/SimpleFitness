package hoonstudio.com.fitnow

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey


@Entity(
    tableName = "exercise_item_table",
    foreignKeys = [ForeignKey(
        entity = ExerciseCategory::class,
        parentColumns = ["id"],
        childColumns = ["exercise_item_category_id"],
        onDelete = CASCADE)])
data class ExerciseItem(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0,
                        @ColumnInfo(name = "exercise_item_category_id") var exerciseItemCategoryId: Long,
                        @ColumnInfo(name = "exercise_item_priority") var exerciseItemPriority: Int,
                        @ColumnInfo(name = "exercise_name") var exerciseName: String,
                        @ColumnInfo(name = "sets") var sets: Int,
                        @ColumnInfo(name = "reps") var reps: Int)