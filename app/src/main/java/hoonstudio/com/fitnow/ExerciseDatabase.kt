package hoonstudio.com.fitnow

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = arrayOf(ExerciseCategory::class, ExerciseItem::class), version = 12)
abstract class ExerciseDatabase: RoomDatabase(){

    companion object {

        private lateinit var instance: ExerciseDatabase

        fun getInstance(context: Context): ExerciseDatabase?{
            if(!this::instance.isInitialized){
                synchronized(ExerciseDatabase::class){
                    instance = Room.databaseBuilder(context.applicationContext,
                        ExerciseDatabase::class.java, "exerciseCategory.db")
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        // returns a value of anonymous type object that is a slight modification of RoomDatabase.Callback()
        // without having to create a subclass for it.
        // .Callback() is an inner class of RoomDatabase hence the .
        // having a value = object [...] {} will assign the end value of 'object' to the roomCallback variable.
        private val roomCallback = object: RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDb(instance).initDb()
            }
        }

        private class PopulateDb internal constructor(private val exerciseCategoryDatabase: ExerciseDatabase){
            private val scope = CoroutineScope(Dispatchers.Default)
            private val exerciseCategoryDao = exerciseCategoryDatabase.exerciseCategoryDao()


            fun initDb() = scope.launch{
             //   val exerciseList: MutableList<ExerciseItem> = ArrayList<ExerciseItem>()
//                exerciseList.add(ExerciseItem( "Barbell Curls", 5, 5))
//                exerciseList.add(ExerciseItem( "Dumbbell Curls", 5, 5))
//                exerciseList.add(ExerciseItem( "Pull Ups", 5, 5))
//
//                val exerciseList2: MutableList<ExerciseItem> = ArrayList<ExerciseItem>()
//                exerciseList2.add(ExerciseItem("Flat Bench Press", 4, 5))
//                exerciseList2.add(ExerciseItem("Dumbbell Bench Press", 4, 5))
//                exerciseList2.add(ExerciseItem("Butterfly Press", 4, 5))
//
//                exerciseCategoryDao.insert(ExerciseCategory(0,"Bicep", 0,
//                    true,false,true,false,true,
//                    false,true))
//                exerciseCategoryDao.insert(ExerciseCategory( 0,"Tricep", 0,
//                    false,true,false,true,false,
//                    true,false))
            }
        }
    }

    abstract fun exerciseCategoryDao():ExerciseCategoryDao
    abstract fun exerciseItemDao():ExerciseItemDao
}