package robi.saputra.nutapos.data

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import robi.saputra.nutapos.models.FinanceIn

@Dao
interface FinanceInDao {
    @Query("SELECT * FROM finance_in")
    fun getListFinanceIn(): List<FinanceIn>

    @Query("SELECT * FROM finance_in WHERE date BETWEEN :dateFrom AND :dateTo;")
    fun getRangeOfDate(dateFrom: Long, dateTo: Long): List<FinanceIn>

    @Insert
    fun insertFinanceIn(vararg financeIn: FinanceIn)

    @Update
    fun updateFinanceIn(vararg financeIn: FinanceIn)

    @Delete
    fun deleteFinanceIn(vararg financeIn: FinanceIn)
}