package hoonstudio.com.fitnow

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_category_table")
data class ExerciseCategory(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long,
                            @ColumnInfo(name = "category_name") var categoryName: String,
                            @ColumnInfo(name = "exercise_count") var exerciseCount: Int,
                            @ColumnInfo(name = "day_monday") var dayMonday: Boolean,
                            @ColumnInfo(name = "day_tuesday") var dayTuesday: Boolean,
                            @ColumnInfo(name = "day_wednesday") var dayWednesday: Boolean,
                            @ColumnInfo(name = "day_thursday") var dayThursday: Boolean,
                            @ColumnInfo(name = "day_friday") var dayFriday: Boolean,
                            @ColumnInfo(name = "day_saturday") var daySaturday: Boolean,
                            @ColumnInfo(name = "day_sunday") var daySunday: Boolean){

}