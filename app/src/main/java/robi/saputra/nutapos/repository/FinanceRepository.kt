package robi.saputra.nutapos.repository

import robi.saputra.nutapos.data.FinanceInDao
import robi.saputra.nutapos.models.FinanceIn

interface FinanceRepository {
    fun getAllTransaction(): List<FinanceIn>
    fun getRangeTransaction(dateFrom: String, dateTo: String): List<FinanceIn>
    fun insertTransaction(financeIn: FinanceIn): Boolean
    fun updateTransaction(financeIn: FinanceIn): Boolean
    fun deleteTransaction(financeIn: FinanceIn): Boolean
}