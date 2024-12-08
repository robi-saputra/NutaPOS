package robi.saputra.nutapos.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import robi.saputra.nutapos.models.FinanceIn

@Database(entities = [FinanceIn::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun FinanceInDao(): FinanceInDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finance_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            Log.d("Room Query", "Database opened.")
                        }

                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("Room Query", "Database created.")
                        }
                    })
                    .build()
            }
            return instance!!
        }
    }
}